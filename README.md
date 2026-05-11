# Gourmet Bookings Backend API

Spring Boot backend application for the Gourmet Bookings order management system with MySQL database.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- MySQL database named `gourmet_bookings`

## Database Setup

1. Create MySQL database:
   ```sql
   CREATE DATABASE gourmet_bookings CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Update database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gourmet_bookings?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. The application will automatically create the database schema on first startup using the migration script.

## Getting Started

1. Navigate to the project directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Orders
- `GET /api/orders` - Get all active orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/number/{orderNumber}` - Get order by order number
- `GET /api/orders/status/{statusId}` - Get orders by status
- `GET /api/orders/reseller/{resellerId}` - Get orders by reseller
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Soft delete order
- `POST /api/orders/{id}/submit` - Submit order
- `POST /api/orders/{id}/cancel` - Cancel order

### Services
- `GET /api/services` - Get all active services
- `GET /api/services/{id}` - Get service by ID
- `GET /api/services/area/{areaId}` - Get services by area
- `GET /api/services/type/{serviceTypeId}` - Get services by type
- `GET /api/services/area/{areaId}/type/{serviceTypeId}` - Get services by area and type
- `GET /api/services/private` - Get private services
- `POST /api/services` - Create new service
- `PUT /api/services/{id}` - Update service

## Database Schema

The application implements a comprehensive order management schema with the following main entities:

### Management & Catalog
- **roles, users** - User management
- **resellers, reseller_contacts, agents** - Partner management
- **guides** - Service providers
- **areas, service_types, services** - Service catalog
- **distance_bands, special_request_types** - Configuration data

### Orders
- **orders** - Main order entity with full lifecycle
- **order_services** - Primary service bookings
- **order_additional_services** - Pickup/dropoff/handoff services
- **order_special_requests** - Special requirement mappings
- **order_status_history** - Status change tracking
- **order_financial_lines** - Financial breakdown

### Operations
- **allotments, allotment_reservations** - Capacity management
- **assignments** - Guide assignments
- **payments** - Payment tracking
- **audit_logs** - System audit trail

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── controller/     # REST controllers
│   │   │       ├── dto/           # Data transfer objects
│   │   │       ├── entity/        # JPA entities
│   │   │       ├── repository/    # Spring Data repositories
│   │   │       ├── service/       # Business logic layer
│   │   │       └── DemoApplication.java
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/     # Database migration scripts
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/example/demo/
├── pom.xml
└── README.md
```

## Technologies Used

- **Spring Boot 3.2.5** - Application framework
- **Spring Data JPA** - Database ORM
- **Spring Web** - REST API framework
- **Spring Validation** - Input validation
- **MySQL 8.0** - Database
- **Lombok** - Code generation
- **Maven** - Build tool

## Features

- **Order Management**: Complete order lifecycle from creation to completion
- **Multi-step Order Process**: Support for the 4-step New Order modal workflow
- **Service Catalog**: Flexible service management with areas and types
- **Financial Tracking**: Multi-currency financial line management
- **Audit Trail**: Complete audit logging for all operations
- **Soft Deletes**: Data preservation with soft delete functionality
- **Status Management**: Configurable order status workflow
- **Capacity Management**: Allotment and reservation system
- **Guide Assignment**: Service provider management and assignment

## Development Notes

- Uses snake_case database naming convention
- BIGINT UNSIGNED primary keys for scalability
- UTF8MB4 charset for full Unicode support
- Comprehensive indexing for performance
- Foreign key constraints with appropriate delete rules
- JSON column support for flexible metadata storage
