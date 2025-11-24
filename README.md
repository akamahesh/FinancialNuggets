# FinancialNuggets

A comprehensive Spring Boot application for managing and analyzing Asset Management Company (AMC) data, including schemes, holdings, and employee information. The application provides RESTful APIs and a web interface for tracking mutual fund holdings over time.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Development](#development)
- [Testing](#testing)
- [Future Features](#future-features)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

FinancialNuggets is a financial data management system designed to track and analyze mutual fund holdings. It provides:

- **AMC Management**: Manage Asset Management Companies and their details
- **Scheme Management**: Track mutual fund schemes associated with AMCs
- **Holdings Tracking**: Monitor stock holdings within schemes across different reporting periods
- **Employee Management**: Manage employee information
- **Data Import**: CSV file upload and parsing for bulk data import
- **Web Interface**: Thymeleaf-based UI with HTMX for dynamic interactions

## ✨ Features

### Core Features

- ✅ **AMC CRUD Operations**: Create, read, update, and delete AMC records
- ✅ **Scheme Management**: Manage schemes linked to AMCs with detailed information
- ✅ **Holdings Management**: Track holdings with ISIN, quantity, market value, and percentage of AUM
- ✅ **Time-Series Data**: Holdings tracked by reporting date for historical analysis
- ✅ **Employee Management**: Employee data management with versioned APIs
- ✅ **CSV Data Import**: Bulk import holdings data via CSV files
- ✅ **RESTful APIs**: Comprehensive REST API for all entities
- ✅ **Web UI**: User-friendly web interface for data management
- ✅ **Exception Handling**: Global exception handling with proper error messages

### Upcoming Features

- 🚧 **Holdings Progression Tracking**: Analyze how holdings change over time (see [HOLDINGS_PROGRESSION_FEATURE.md](./HOLDINGS_PROGRESSION_FEATURE.md))
- 🚧 **Progression Analytics**: Track increasing/decreasing positions across reporting periods
- 🚧 **Date Range Queries**: Query holdings within specific date ranges
- 🚧 **Comparison Tools**: Compare holdings between different reporting dates

## 🛠 Technology Stack

### Backend
- **Java 21**: Modern Java features and performance
- **Spring Boot 3.5.7**: Application framework
- **Spring Data JPA**: Database abstraction layer
- **Spring Web**: RESTful web services
- **Spring Boot Actuator**: Application monitoring

### Frontend
- **Thymeleaf**: Server-side template engine
- **HTMX 4.0.1**: Dynamic HTML interactions without JavaScript
- **Bootstrap** (likely): UI framework

### Database
- **MySQL 8**: Primary production database
- **H2 Database**: In-memory database for development/testing
- **PostgreSQL**: Alternative database support

### Build & Tools
- **Maven**: Dependency management and build tool
- **Lombok**: Reduces boilerplate code
- **OpenCSV 5.9**: CSV parsing and processing
- **Jackson**: JSON/XML processing

## 📁 Project Structure

```
FinancialNuggets/
├── src/
│   ├── main/
│   │   ├── java/com/maheshbhatt/financialnuggets/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/           # REST and UI controllers
│   │   │   ├── entity/               # JPA entities
│   │   │   ├── exception/            # Custom exceptions and handlers
│   │   │   ├── model/                # DTOs and data models
│   │   │   ├── repository/           # JPA repositories
│   │   │   ├── service/              # Business logic layer
│   │   │   │   └── impl/             # Service implementations
│   │   │   └── utils/                # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/            # Thymeleaf templates
│   └── test/                          # Test classes
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

### Key Components

- **Entities**: `AmcEntity`, `SchemeEntity`, `HoldingEntity`, `EmployeeEntity`
- **DTOs**: `AmcDTO`, `SchemeDTO`, `HoldingDTO`, `Employee`
- **Controllers**: REST controllers (`*Controller.java`) and UI controllers (`ui/*UIController.java`)
- **Services**: Business logic layer with implementations
- **Repositories**: Data access layer using Spring Data JPA

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (for production) or **H2 Database** (for development)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd FinancialNuggets
```

### 2. Database Setup

#### MySQL Setup (Production)

```sql
CREATE DATABASE fn-db;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON fn-db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

#### H2 Database (Development)

H2 is configured for in-memory testing. No setup required.

### 3. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/fn-db
spring.datasource.username=root
spring.datasource.password=root

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run the main class:
```bash
java -jar target/FinancialNuggets-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## ⚙️ Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Application Name
spring.application.name=FinancialNuggets

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/fn-db
spring.datasource.username=root
spring.datasource.password=root

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# File Upload Limits
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# H2 Console (Development)
spring.h2.console.enabled=true
```

### Environment-Specific Configuration

For different environments, create:
- `application-dev.properties` (Development)
- `application-prod.properties` (Production)
- `application-test.properties` (Testing)

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### AMC Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/amcs` | Get all AMCs |
| GET | `/api/v1/amcs/{id}` | Get AMC by ID |
| POST | `/api/v1/amcs` | Create new AMC |
| DELETE | `/api/v1/amcs/{id}` | Delete AMC by ID |
| DELETE | `/api/v1/amcs/all` | Delete all AMCs |

### Scheme Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/schemes` | Get all schemes |
| GET | `/api/v1/schemes/{id}` | Get scheme by ID |
| GET | `/api/v1/schemes/amc/{amcId}` | Get schemes by AMC ID |
| POST | `/api/v1/schemes` | Create new scheme |
| DELETE | `/api/v1/schemes/{id}` | Delete scheme by ID |

### Holdings Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/holdings` | Get all holdings |
| GET | `/api/v1/holdings/{id}` | Get holding by ID |
| GET | `/api/v1/holdings/amc/{amcId}` | Get holdings by AMC ID |
| GET | `/api/v1/holdings/scheme/{schemeCode}` | Get holdings by scheme code |
| GET | `/api/v1/holdings/amc/{amcId}/scheme/{schemeCode}` | Get holdings by AMC and scheme |
| GET | `/api/v1/holdings/reporting-date/{reportingDate}` | Get holdings by reporting date |
| POST | `/api/v1/holdings` | Create new holding |
| PUT | `/api/v1/holdings/{id}` | Update holding |
| DELETE | `/api/v1/holdings/{id}` | Delete holding by ID |

### Example API Requests

#### Create AMC
```bash
curl -X POST http://localhost:8080/api/v1/amcs \
  -H "Content-Type: application/json" \
  -d '{
    "amcName": "Example AMC",
    "amcId": "AMC001",
    "shortName": "EXAMC",
    "description": "Example Asset Management Company"
  }'
```

#### Get Holdings by Scheme
```bash
curl http://localhost:8080/api/v1/holdings/scheme/SCHEME001
```

#### Get Holdings by Reporting Date
```bash
curl http://localhost:8080/api/v1/holdings/reporting-date/2024-06-30
```

## 🗄 Database Schema

### Entities

#### AMC (Asset Management Company)
- `id` (Long, Primary Key)
- `amcName` (String, Unique)
- `amcId` (String)
- `shortName` (String)
- `description` (String)
- `imgUrl` (String)
- `websiteUrl` (String)
- `createAt` (Date)
- `updateAt` (Date)

#### Scheme
- `id` (Long, Primary Key)
- `amc` (AmcEntity, Foreign Key)
- `schemeCode` (String, Unique)
- `name` (String)
- `type` (String)
- `category` (String)
- `launchDate` (LocalDate)
- `closureDate` (LocalDate)

#### Holding
- `id` (Long, Primary Key)
- `amcId` (Long)
- `schemeCode` (String)
- `name` (String)
- `isin` (String)
- `quantity` (Long)
- `marketValue` (BigDecimal)
- `percentageOfAum` (BigDecimal)
- `reportingDate` (LocalDate)
- **Unique Constraint**: `(isin, reportingDate)`

#### Employee
- Employee entity with standard fields

### Relationships

- **AMC → Schemes**: One-to-Many
- **Scheme → Holdings**: Implicit (via schemeCode)
- **AMC → Holdings**: Implicit (via amcId)

## 💻 Development

### Running in Development Mode

```bash
mvn spring-boot:run
```

Spring Boot DevTools will automatically reload on code changes.

### Code Style

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Implement proper exception handling
- Add JavaDoc comments for public methods

### Adding New Features

1. Create entity in `entity/` package
2. Create repository in `repository/` package
3. Create DTO in `model/` package
4. Create service interface in `service/` package
5. Implement service in `service/impl/` package
6. Create REST controller in `controller/` package
7. Add exception handling if needed

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=HoldingRepositoryTest
```

### Test Structure

- Unit tests for services
- Integration tests for repositories
- API tests for controllers

## 🚧 Future Features

### Holdings Progression Feature

A comprehensive feature for tracking holdings progression over time is in development. See [HOLDINGS_PROGRESSION_FEATURE.md](./HOLDINGS_PROGRESSION_FEATURE.md) for details.

**Planned Features:**
- Track holdings changes across reporting dates
- Query holdings by date ranges
- Compare holdings between periods
- Analyze position increases/decreases
- Generate progression summaries

**Status**: Phase 1 (Repository Layer) - ✅ Complete

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Write clean, maintainable code
- Add tests for new features
- Update documentation
- Follow existing code patterns
- Use meaningful commit messages

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

**Mahesh Bhatt**
- Email: maheshbhatt.dev@gmail.com

## 🙏 Acknowledgments

- Spring Boot community
- All contributors and users

## 📞 Support

For issues, questions, or contributions, please open an issue on the repository.

---

**Note**: This is an active development project. Features and APIs may change. Please refer to the latest documentation for current functionality.
