package com.maheshbhatt.financialnuggets.repository;

import com.maheshbhatt.financialnuggets.entity.HoldingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HoldingRepositoryTest {

    @Autowired
    private HoldingRepository holdingRepository;

    private HoldingEntity holding1;
    private HoldingEntity holding2;
    private HoldingEntity holding3;
    private HoldingEntity holding4;
    private HoldingEntity holding5;

    private LocalDate juneDate;
    private LocalDate julyDate;
    private LocalDate augustDate;

    @BeforeEach
    void setUp() {
        // Clear repository before each test
        holdingRepository.deleteAll();

        // Set up test dates
        juneDate = LocalDate.of(2024, 6, 30);
        julyDate = LocalDate.of(2024, 7, 31);
        augustDate = LocalDate.of(2024, 8, 31);

        // Create test holdings
        // Holding 1: Stock A, Scheme 1, AMC 1, June
        holding1 = new HoldingEntity();
        holding1.setAmcId(1L);
        holding1.setSchemeCode("SCHEME001");
        holding1.setName("Stock A");
        holding1.setIsin("INE1234567890");
        holding1.setQuantity(10L);
        holding1.setMarketValue(new BigDecimal("10000.00"));
        holding1.setPercentageOfAum(new BigDecimal("5.00"));
        holding1.setReportingDate(juneDate);

        // Holding 2: Stock A, Scheme 1, AMC 1, July
        holding2 = new HoldingEntity();
        holding2.setAmcId(1L);
        holding2.setSchemeCode("SCHEME001");
        holding2.setName("Stock A");
        holding2.setIsin("INE1234567890");
        holding2.setQuantity(12L);
        holding2.setMarketValue(new BigDecimal("12000.00"));
        holding2.setPercentageOfAum(new BigDecimal("6.00"));
        holding2.setReportingDate(julyDate);

        // Holding 3: Stock A, Scheme 1, AMC 1, August
        holding3 = new HoldingEntity();
        holding3.setAmcId(1L);
        holding3.setSchemeCode("SCHEME001");
        holding3.setName("Stock A");
        holding3.setIsin("INE1234567890");
        holding3.setQuantity(14L);
        holding3.setMarketValue(new BigDecimal("14000.00"));
        holding3.setPercentageOfAum(new BigDecimal("7.00"));
        holding3.setReportingDate(augustDate);

        // Holding 4: Stock B, Scheme 1, AMC 1, July
        holding4 = new HoldingEntity();
        holding4.setAmcId(1L);
        holding4.setSchemeCode("SCHEME001");
        holding4.setName("Stock B");
        holding4.setIsin("INE9876543210");
        holding4.setQuantity(20L);
        holding4.setMarketValue(new BigDecimal("20000.00"));
        holding4.setPercentageOfAum(new BigDecimal("10.00"));
        holding4.setReportingDate(julyDate);

        // Holding 5: Stock A, Scheme 2, AMC 1, June
        holding5 = new HoldingEntity();
        holding5.setAmcId(1L);
        holding5.setSchemeCode("SCHEME002");
        holding5.setName("Stock A");
        holding5.setIsin("INE1234567890");
        holding5.setQuantity(5L);
        holding5.setMarketValue(new BigDecimal("5000.00"));
        holding5.setPercentageOfAum(new BigDecimal("3.00"));
        holding5.setReportingDate(juneDate);

        // Save all holdings
        holdingRepository.save(holding1);
        holdingRepository.save(holding2);
        holdingRepository.save(holding3);
        holdingRepository.save(holding4);
        holdingRepository.save(holding5);
    }

    @Test
    void testFindByIsinOrderByReportingDateAsc_Task1_1() {
        // Test: Get all holdings for a specific ISIN across all dates
        List<HoldingEntity> holdings = holdingRepository.findByIsinOrderByReportingDateAsc("INE1234567890");

        // Verify results
        assertNotNull(holdings);
        assertEquals(4, holdings.size(), "Should find 4 holdings for Stock A (3 in Scheme 1 + 1 in Scheme 2)");

        // Verify ordering (earliest first)
        assertEquals(juneDate, holdings.get(0).getReportingDate());
        assertEquals(juneDate, holdings.get(1).getReportingDate());
        assertEquals(julyDate, holdings.get(2).getReportingDate());
        assertEquals(augustDate, holdings.get(3).getReportingDate());

        // Verify ISIN matches
        holdings.forEach(holding -> assertEquals("INE1234567890", holding.getIsin()));
    }

    @Test
    void testFindByIsinOrderByReportingDateAsc_NoResults() {
        // Test: Query for non-existent ISIN
        List<HoldingEntity> holdings = holdingRepository.findByIsinOrderByReportingDateAsc("INE0000000000");

        assertNotNull(holdings);
        assertTrue(holdings.isEmpty());
    }

    @Test
    void testFindByIsinAndReportingDateBetweenOrderByReportingDateAsc_Task1_2() {
        // Test: Get holdings for ISIN within a date range
        LocalDate startDate = julyDate;
        LocalDate endDate = augustDate;

        List<HoldingEntity> holdings = holdingRepository.findByIsinAndReportingDateBetweenOrderByReportingDateAsc(
                "INE1234567890", startDate, endDate);

        // Verify results
        assertNotNull(holdings);
        assertEquals(2, holdings.size(), "Should find 2 holdings (July and August)");

        // Verify ordering (earliest first)
        assertEquals(julyDate, holdings.get(0).getReportingDate());
        assertEquals(augustDate, holdings.get(1).getReportingDate());

        // Verify date range
        holdings.forEach(holding -> {
            assertTrue(holding.getReportingDate().isAfter(startDate.minusDays(1)) ||
                    holding.getReportingDate().isEqual(startDate));
            assertTrue(holding.getReportingDate().isBefore(endDate.plusDays(1)) ||
                    holding.getReportingDate().isEqual(endDate));
        });
    }

    @Test
    void testFindByIsinAndReportingDateBetweenOrderByReportingDateAsc_SingleDate() {
        // Test: Date range with same start and end date
        List<HoldingEntity> holdings = holdingRepository.findByIsinAndReportingDateBetweenOrderByReportingDateAsc(
                "INE1234567890", julyDate, julyDate);

        assertNotNull(holdings);
        assertEquals(1, holdings.size());
        assertEquals(julyDate, holdings.get(0).getReportingDate());
    }

    @Test
    void testFindBySchemeCodeAndReportingDateBetweenOrderByReportingDateAsc_Task1_3() {
        // Test: Get all holdings for a scheme within a date range
        LocalDate startDate = juneDate;
        LocalDate endDate = julyDate;

        List<HoldingEntity> holdings = holdingRepository.findBySchemeCodeAndReportingDateBetweenOrderByReportingDateAsc(
                "SCHEME001", startDate, endDate);

        // Verify results
        assertNotNull(holdings);
        assertEquals(3, holdings.size(), "Should find 3 holdings for SCHEME001 (June, July - Stock A & B)");

        // Verify scheme code
        holdings.forEach(holding -> assertEquals("SCHEME001", holding.getSchemeCode()));

        // Verify ordering
        assertTrue(holdings.get(0).getReportingDate().isBefore(holdings.get(2).getReportingDate()) ||
                holdings.get(0).getReportingDate().isEqual(holdings.get(2).getReportingDate()));
    }

    @Test
    void testFindBySchemeCodeAndReportingDateBetweenOrderByReportingDateAsc_AllDates() {
        // Test: Get all holdings for a scheme across all dates
        LocalDate startDate = juneDate;
        LocalDate endDate = augustDate;

        List<HoldingEntity> holdings = holdingRepository.findBySchemeCodeAndReportingDateBetweenOrderByReportingDateAsc(
                "SCHEME001", startDate, endDate);

        assertNotNull(holdings);
        assertEquals(4, holdings.size(), "Should find 4 holdings for SCHEME001 (June, July x2, August)");

        // Verify all reporting dates are within range
        holdings.forEach(holding -> {
            assertTrue(holding.getReportingDate().isAfter(startDate.minusDays(1)) ||
                    holding.getReportingDate().isEqual(startDate));
            assertTrue(holding.getReportingDate().isBefore(endDate.plusDays(1)) ||
                    holding.getReportingDate().isEqual(endDate));
        });
    }

    @Test
    void testFindByAmcIdAndReportingDateBetweenOrderByReportingDateAsc_Task1_4() {
        // Test: Get all holdings for an AMC within a date range
        LocalDate startDate = juneDate;
        LocalDate endDate = julyDate;

        List<HoldingEntity> holdings = holdingRepository.findByAmcIdAndReportingDateBetweenOrderByReportingDateAsc(
                1L, startDate, endDate);

        // Verify results
        assertNotNull(holdings);
        assertEquals(4, holdings.size(), "Should find 4 holdings for AMC 1 (3 in June + 1 in July)");

        // Verify AMC ID
        holdings.forEach(holding -> assertEquals(1L, holding.getAmcId()));

        // Verify date range
        holdings.forEach(holding -> {
            assertTrue(holding.getReportingDate().isAfter(startDate.minusDays(1)) ||
                    holding.getReportingDate().isEqual(startDate));
            assertTrue(holding.getReportingDate().isBefore(endDate.plusDays(1)) ||
                    holding.getReportingDate().isEqual(endDate));
        });
    }

    @Test
    void testFindByAmcIdAndReportingDateBetweenOrderByReportingDateAsc_AllDates() {
        // Test: Get all holdings for an AMC across all dates
        LocalDate startDate = juneDate;
        LocalDate endDate = augustDate;

        List<HoldingEntity> holdings = holdingRepository.findByAmcIdAndReportingDateBetweenOrderByReportingDateAsc(
                1L, startDate, endDate);

        assertNotNull(holdings);
        assertEquals(5, holdings.size(), "Should find all 5 holdings for AMC 1");

        // Verify ordering
        LocalDate previousDate = null;
        for (HoldingEntity holding : holdings) {
            if (previousDate != null) {
                assertTrue(holding.getReportingDate().isAfter(previousDate) ||
                        holding.getReportingDate().isEqual(previousDate),
                        "Holdings should be ordered by reportingDate ascending");
            }
            previousDate = holding.getReportingDate();
        }
    }

    @Test
    void testFindDistinctReportingDatesOrderByReportingDateDesc_Task1_5() {
        // Test: Get distinct reporting dates ordered by date descending
        List<LocalDate> reportingDates = holdingRepository.findDistinctReportingDatesOrderByReportingDateDesc();

        // Verify results
        assertNotNull(reportingDates);
        assertEquals(3, reportingDates.size(), "Should find 3 distinct dates (June, July, August)");

        // Verify ordering (newest first - DESC)
        assertEquals(augustDate, reportingDates.get(0));
        assertEquals(julyDate, reportingDates.get(1));
        assertEquals(juneDate, reportingDates.get(2));

        // Verify all dates are unique
        assertEquals(reportingDates.size(), reportingDates.stream().distinct().count(),
                "All dates should be unique");
    }

    @Test
    void testFindDistinctReportingDatesOrderByReportingDateDesc_EmptyRepository() {
        // Test: Empty repository should return empty list
        holdingRepository.deleteAll();

        List<LocalDate> reportingDates = holdingRepository.findDistinctReportingDatesOrderByReportingDateDesc();

        assertNotNull(reportingDates);
        assertTrue(reportingDates.isEmpty());
    }

    @Test
    void testAllMethodsIntegration() {
        // Integration test: Test all methods together with complex scenario
        // Verify Task 1.1: All holdings for Stock A
        List<HoldingEntity> stockAHoldings = holdingRepository.findByIsinOrderByReportingDateAsc("INE1234567890");
        assertEquals(4, stockAHoldings.size());

        // Verify Task 1.2: Stock A in date range
        List<HoldingEntity> stockAInRange = holdingRepository.findByIsinAndReportingDateBetweenOrderByReportingDateAsc(
                "INE1234567890", juneDate, julyDate);
        assertEquals(3, stockAInRange.size());

        // Verify Task 1.3: Scheme holdings in date range
        List<HoldingEntity> schemeHoldings = holdingRepository.findBySchemeCodeAndReportingDateBetweenOrderByReportingDateAsc(
                "SCHEME001", juneDate, augustDate);
        assertEquals(4, schemeHoldings.size());

        // Verify Task 1.4: AMC holdings in date range
        List<HoldingEntity> amcHoldings = holdingRepository.findByAmcIdAndReportingDateBetweenOrderByReportingDateAsc(
                1L, juneDate, augustDate);
        assertEquals(5, amcHoldings.size());

        // Verify Task 1.5: Distinct dates
        List<LocalDate> dates = holdingRepository.findDistinctReportingDatesOrderByReportingDateDesc();
        assertEquals(3, dates.size());
    }
}
