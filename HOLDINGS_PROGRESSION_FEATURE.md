# Holdings Progression Feature - Task Outline

## Overview

This feature allows tracking how AMC holdings change over time by reporting date. Users can see the progression of positions (quantity, market value, percentage) across different months/reporting periods.

**Example Use Case:**

- June: Scheme has 10 quantity of Stock A
- July: Scheme has 12 quantity of Stock A
- August: Scheme has 14 quantity of Stock A
- **Analysis:** AMC is increasing position in Stock A

---

## Current State Analysis

✅ **Already Implemented:**

- `HoldingEntity` has `reportingDate` field (LocalDate)
- Unique constraint on `(isin, reportingDate)` - supports multiple entries for same stock at different dates
- Repository has basic query methods by reporting date
- Basic CRUD operations exist
- `HoldingDTO` contains all necessary fields for progression: `reportingDate`, `quantity`, `marketValue`, `percentageOfAum`, `isin`, `name`

❌ **Missing:**

- Time-series progression queries (by ISIN, scheme, AMC with date ranges)
- Comparison between different reporting dates
- Progression analysis (growth/decline tracking)
- APIs for historical progression data

## Design Philosophy - Simplified Approach

**Key Insight:** `HoldingDTO` already has all fields needed for progression! Querying by date range and sorting by `reportingDate` gives us progression data.

**Approach:**

- ✅ **Use existing `HoldingDTO`** - No need to duplicate data structure
- ✅ **Return `List<HoldingDTO>`** - Client gets sorted list, can calculate changes if needed
- ✅ **Use `Map<String, List<HoldingDTO>>`** - For grouped data (key=ISIN)
- ⚠️ **Optional DTOs only when:** Summary statistics or pre-calculated changes are needed

---

## Task Breakdown

### Phase 1: Repository Layer - Data Access

#### Task 1.1: Add Repository Method - Get Holdings by ISIN Across Dates

**Description:** Query all holdings for a specific stock (ISIN) across all reporting dates
**Implementation:**

- Method: `findByIsinOrderByReportingDateAsc(String isin)`
- Returns: List of holdings sorted by reporting date (earliest first)
  **Files:** `HoldingRepository.java`

---

#### Task 1.2: Add Repository Method - Get Holdings by ISIN and Date Range

**Description:** Query holdings for a specific stock within a date range
**Implementation:**

- Method: `findByIsinAndReportingDateBetween(String isin, LocalDate startDate, LocalDate endDate)`
- Returns: List of holdings in the date range, ordered by date
  **Files:** `HoldingRepository.java`

---

#### Task 1.3: Add Repository Method - Get Holdings by Scheme and Date Range

**Description:** Query all holdings for a scheme within a date range
**Implementation:**

- Method: `findBySchemeCodeAndReportingDateBetween(String schemeCode, LocalDate startDate, LocalDate endDate)`
- Returns: List of holdings grouped by reporting date
  **Files:** `HoldingRepository.java`

---

#### Task 1.4: Add Repository Method - Get Holdings by AMC and Date Range

**Description:** Query all holdings for an AMC within a date range
**Implementation:**

- Method: `findByAmcIdAndReportingDateBetween(Long amcId, LocalDate startDate, LocalDate endDate)`
- Returns: List of holdings across all schemes for the AMC
  **Files:** `HoldingRepository.java`

---

#### Task 1.5: Add Repository Method - Get Distinct Reporting Dates

**Description:** Get all available reporting dates for querying available time periods
**Implementation:**

- Method: `findDistinctReportingDatesOrderByReportingDateDesc()`
- Returns: List of LocalDate (distinct reporting dates)
  **Files:** `HoldingRepository.java`

---

### Phase 2: Model Layer - DTOs for Progression

**Design Decision:** Use existing `HoldingDTO` as the primary data structure for progression, since it already contains all necessary fields (`reportingDate`, `quantity`, `marketValue`, `percentageOfAum`, `isin`, `name`). Only create additional DTOs where calculated/aggregated data is needed.

---

#### Task 2.1: Create HoldingChangeDTO (Optional Enhancement)

**Description:** DTO to represent calculated changes between two reporting periods
**When to Use:** Only if client needs pre-calculated change metrics (alternatively, client can calculate from `List<HoldingDTO>`)
**Fields:**

- `quantityChange` (Long)
- `quantityChangePercentage` (BigDecimal)
- `marketValueChange` (BigDecimal)
- `marketValueChangePercentage` (BigDecimal)
- `percentageChange` (BigDecimal)
  **Files:** `model/HoldingChangeDTO.java`
  **Status:** ⚠️ **OPTIONAL** - Can be skipped if client handles calculations

---

#### Task 2.2: Create ProgressionSummaryDTO (For Aggregated Views)

**Description:** Summary statistics for progression analysis - needed for scheme/AMC-level views
**Fields:**

- `totalStocks` (Integer)
- `increasingPositions` (Integer) - stocks with increasing quantity
- `decreasingPositions` (Integer) - stocks with decreasing quantity
- `newPositions` (Integer) - stocks added in latest period
- `exitedPositions` (Integer) - stocks removed in latest period
  **Files:** `model/ProgressionSummaryDTO.java`
  **Status:** ✅ **REQUIRED** - For summary/aggregation features

---

#### Task 2.3: Create ProgressionResponseWrapperDTO (For Grouped Data)

**Description:** Wrapper DTO when grouping holdings by ISIN for scheme-level progression
**Fields:**

- `isin` (String)
- `name` (String)
- `holdings` (List<HoldingDTO>) - sorted by reportingDate
- `firstReportingDate` (LocalDate)
- `lastReportingDate` (LocalDate)
  **Files:** `model/ProgressionResponseWrapperDTO.java`
  **Status:** ⚠️ **OPTIONAL** - Can use `Map<String, List<HoldingDTO>>` instead (key=ISIN)

---

**Simplified Approach:**

- **Basic Progression:** Return `List<HoldingDTO>` sorted by `reportingDate` ✅
- **With Calculations:** Add `HoldingChangeDTO` as optional field in response (or calculate client-side)
- **Aggregated Views:** Use `Map<String, List<HoldingDTO>>` where key = ISIN (or create wrapper DTO for clarity)
- **Summaries:** Only `ProgressionSummaryDTO` is truly needed for aggregated stats

---

### Phase 3: Service Layer - Business Logic

#### Task 3.1: Add Service Method - Get Progression by ISIN

**Description:** Get progression history for a specific stock (ISIN)
**Implementation:**

- Method: `getProgressionByIsin(String isin, LocalDate startDate, LocalDate endDate): List<HoldingDTO>`
- Logic: Query holdings by ISIN in date range, return sorted by reportingDate
- **Returns:** `List<HoldingDTO>` - sorted by reportingDate (earliest first)
  **Files:** `HoldingService.java`, `impl/HoldingServiceImpl.java`

---

#### Task 3.2: Add Service Method - Get Progression by ISIN and Scheme

**Description:** Get progression for a stock within a specific scheme
**Implementation:**

- Method: `getProgressionByIsinAndScheme(String isin, String schemeCode, LocalDate startDate, LocalDate endDate): List<HoldingDTO>`
- Logic: Filter by both ISIN and scheme code, return sorted by reportingDate
- **Returns:** `List<HoldingDTO>` - sorted by reportingDate
  **Files:** `HoldingService.java`, `impl/HoldingServiceImpl.java`

---

#### Task 3.3: Add Service Method - Get All Holdings Progression for Scheme

**Description:** Get progression for all holdings in a scheme across dates
**Implementation:**

- Method: `getSchemeHoldingsProgression(String schemeCode, LocalDate startDate, LocalDate endDate): Map<String, List<HoldingDTO>>`
- Logic: Query all holdings for scheme, group by ISIN (key=ISIN, value=List sorted by date)
- **Returns:** `Map<String, List<HoldingDTO>>` where key = ISIN, value = holdings sorted by reportingDate
- **Alternative:** If summary needed, return custom DTO with Map + ProgressionSummaryDTO
  **Files:** `HoldingService.java`, `impl/HoldingServiceImpl.java`

---

#### Task 3.4: Add Service Method - Get All Holdings Progression for AMC

**Description:** Get progression for all holdings across all schemes for an AMC
**Implementation:**

- Method: `getAmcHoldingsProgression(Long amcId, LocalDate startDate, LocalDate endDate): Map<String, Map<String, List<HoldingDTO>>>`
- Logic: Query holdings by AMC, group by schemeCode then ISIN
- **Returns:** `Map<String, Map<String, List<HoldingDTO>>>` where outer key = schemeCode, inner key = ISIN
- **Alternative:** Return `List<HoldingDTO>` with all holdings sorted by schemeCode then reportingDate
  **Files:** `HoldingService.java`, `impl/HoldingServiceImpl.java`

---

#### Task 3.5: Add Service Method - Calculate Holding Changes (Optional)

**Description:** Helper method to calculate changes between two reporting periods
**Implementation:**

- Method: `calculateHoldingChange(HoldingDTO previous, HoldingDTO current): HoldingChangeDTO`
- Logic: Calculate quantity change, market value change, percentage change with percentage calculations
- **Status:** ⚠️ **OPTIONAL** - Only needed if providing pre-calculated changes, otherwise client calculates
  **Files:** `impl/HoldingServiceImpl.java`

---

#### Task 3.6: Add Service Method - Get Available Reporting Dates

**Description:** Get all available reporting dates in the system
**Implementation:**

- Method: `getAvailableReportingDates()`
- Logic: Query distinct reporting dates, return sorted list
  **Files:** `HoldingService.java`, `impl/HoldingServiceImpl.java`

---

#### Task 3.7: Add Service Method - Compare Two Reporting Dates

**Description:** Compare holdings between two specific reporting dates
**Implementation:**

- Method: `compareReportingDates(LocalDate date1, LocalDate date2, String schemeCode, Long amcId): Map<String, Object>`
- Logic: Get holdings for both dates, identify new/exited/updated positions
- **Returns:** Map with keys: "date1Holdings" (List<HoldingDTO>), "date2Holdings" (List<HoldingDTO>), "summary" (ProgressionSummaryDTO)
- **Alternative:** Create ComparisonResultDTO if structured response preferred
  **Files:** `HoldingService.java`, `impl/HoldingServiceImpl.java`

---

### Phase 4: Controller Layer - API Endpoints

#### Task 4.1: Add REST Endpoint - Get Progression by ISIN

**Description:** API endpoint to get progression for a specific stock
**Endpoint:** `GET /api/v1/holdings/progression/isin/{isin}`
**Query Params:**

- `startDate` (optional): LocalDate
- `endDate` (optional): LocalDate
- `schemeCode` (optional): String
  **Response:** `List<HoldingDTO>` - sorted by reportingDate (earliest first)
  **Files:** `HoldingController.java`

---

#### Task 4.2: Add REST Endpoint - Get Scheme Holdings Progression

**Description:** API endpoint to get all holdings progression for a scheme
**Endpoint:** `GET /api/v1/holdings/progression/scheme/{schemeCode}`
**Query Params:**

- `startDate` (optional): LocalDate
- `endDate` (optional): LocalDate
- `includeSummary` (optional): Boolean (default: false)
  **Response:**
- If `includeSummary=false`: `Map<String, List<HoldingDTO>>` (key=ISIN)
- If `includeSummary=true`: Custom wrapper with Map + `ProgressionSummaryDTO`
  **Files:** `HoldingController.java`

---

#### Task 4.3: Add REST Endpoint - Get AMC Holdings Progression

**Description:** API endpoint to get all holdings progression for an AMC
**Endpoint:** `GET /api/v1/holdings/progression/amc/{amcId}`
**Query Params:**

- `startDate` (optional): LocalDate
- `endDate` (optional): LocalDate
- `schemeCode` (optional): String (filter by specific scheme)
- `groupByScheme` (optional): Boolean (default: false)
  **Response:**
- If `groupByScheme=false`: `List<HoldingDTO>` (all holdings, sorted)
- If `groupByScheme=true`: `Map<String, Map<String, List<HoldingDTO>>>` (outer key=schemeCode, inner key=ISIN)
  **Files:** `HoldingController.java`

---

#### Task 4.4: Add REST Endpoint - Compare Two Reporting Dates

**Description:** API endpoint to compare holdings between two dates
**Endpoint:** `GET /api/v1/holdings/progression/compare`
**Query Params:**

- `date1` (required): LocalDate
- `date2` (required): LocalDate
- `schemeCode` (optional): String
- `amcId` (optional): Long
  **Response:** Map with "date1Holdings", "date2Holdings", "summary" keys (or custom ComparisonResultDTO)
  **Files:** `HoldingController.java`

---

#### Task 4.5: Add REST Endpoint - Get Available Reporting Dates

**Description:** API endpoint to get all available reporting dates
**Endpoint:** `GET /api/v1/holdings/progression/dates`
**Response:** `List<LocalDate>`
**Files:** `HoldingController.java`

---

### Phase 5: UI Layer (Optional - Frontend Views)

#### Task 5.1: Create Progression View for Scheme

**Description:** HTML page showing holdings progression for a scheme
**Features:**

- Date range selector
- Table/chart showing progression
- Filter by ISIN or stock name
- Export functionality
  **Files:** `templates/holding/progression.html`

---

#### Task 5.2: Create UI Controller Endpoint for Progression

**Description:** Controller method to serve progression view
**Endpoint:** `GET /holdings/progression/scheme/{schemeCode}`
**Files:** `controller/ui/HoldingUIController.java`

---

#### Task 5.3: Create Progression Chart/Graph View

**Description:** Visual representation of holdings progression over time
**Features:**

- Line chart for quantity progression
- Bar chart for market value changes
- Timeline view
  **Files:** `templates/holding/progression-chart.html`

---

### Phase 6: Utilities & Helper Methods

#### Task 6.1: Create Progression Calculation Utility

**Description:** Utility class for calculating progression metrics
**Methods:**

- Calculate percentage change
- Calculate absolute change
- Determine trend (increasing/decreasing/stable)
- Identify significant changes (threshold-based)
  **Files:** `utils/ProgressionUtils.java`

---

#### Task 6.2: Add Validation for Date Ranges

**Description:** Validate date range inputs in service layer
**Logic:**

- Ensure startDate <= endDate
- Validate dates are not null
- Check dates are within reasonable range
  **Files:** `impl/HoldingServiceImpl.java`

---

### Phase 7: Testing & Documentation

#### Task 7.1: Add Unit Tests for Repository Methods

**Description:** Test new repository query methods
**Files:** `test/java/.../repository/HoldingRepositoryTest.java`

---

#### Task 7.2: Add Unit Tests for Service Methods

**Description:** Test progression calculation logic
**Files:** `test/java/.../service/HoldingServiceTest.java`

---

#### Task 7.3: Add Integration Tests for API Endpoints

**Description:** Test REST endpoints with test data
**Files:** `test/java/.../controller/HoldingControllerTest.java`

---

#### Task 7.4: Update API Documentation

**Description:** Document new endpoints (Swagger/OpenAPI or README)
**Files:** API documentation files

---

## Implementation Order Recommendation

1. **Start with Phase 1** (Repository Layer) - Foundation for data access
2. **Then Phase 2** (Model Layer) - DTOs needed by services
3. **Then Phase 3** (Service Layer) - Business logic
4. **Then Phase 4** (Controller Layer) - API exposure
5. **Phase 5** (UI) - Optional, can be done later
6. **Phase 6** (Utilities) - Can be done in parallel with Phase 3
7. **Phase 7** (Testing) - Should be done alongside each phase

---

## Questions & Clarifications Needed

1. **Date Range Defaults:** What should be the default date range if not provided?

   - Last 12 months?
   - All available data?
   - Last 3 reporting dates?

2. **Filtering Options:** Should progression support filtering by:

   - Stock name/industry?
   - Minimum quantity threshold?
   - Percentage change threshold?

3. **Aggregation Level:** For AMC-level progression, should we:

   - Show aggregated totals across all schemes?
   - Show per-scheme breakdown?
   - Both options?

4. **Performance:** For large datasets, should we:

   - Add pagination?
   - Implement caching?
   - Limit date range queries?

5. **Sorting:** How should progression data be sorted by default?
   - By reporting date (ascending)?
   - By quantity change (descending)?
   - Configurable?

---

## Notes

- All tasks are designed to be small and independent where possible
- Each task can be implemented and tested individually
- Tasks can be requested one at a time for implementation
- Some tasks depend on others (noted in implementation order)
