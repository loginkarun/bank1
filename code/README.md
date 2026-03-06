# Shopping Cart API - SpringBoot Application

## Overview
This is a complete SpringBoot REST API for shopping cart operations, generated from LLD specifications.

## Project Details
- **Project Name**: myproject
- **Spring Boot Version**: 3.5.9
- **Java Version**: 21
- **Build Tool**: Maven

## Features
- Add items to cart
- View cart contents
- Update item quantities
- Remove items from cart
- Clear entire cart
- Product stock validation
- JWT/Basic authentication
- CORS enabled for Angular integration
- Swagger/OpenAPI documentation
- Comprehensive unit tests
- JaCoCo code coverage

## API Endpoints

### Cart Operations
- `POST /api/cart/add` - Add item to cart
- `GET /api/cart` - Get cart contents
- `DELETE /api/cart/remove` - Remove item from cart
- `PUT /api/cart/update` - Update item quantity
- `DELETE /api/cart/clear` - Clear cart

## Technology Stack
- Spring Boot 3.5.9
- Spring Data JPA
- Spring Security
- H2 Database (in-memory)
- Lombok
- SpringDoc OpenAPI (Swagger)
- JUnit 5
- Mockito
- JaCoCo

## Prerequisites
- JDK 21
- Maven 3.6+

## Running the Application

### Local Development
```bash
cd code
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Credentials
- Username: `admin`
- Password: `admin123`

## API Documentation
Once the application is running, access:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## H2 Console
Access H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:cartdb`
- Username: `sa`
- Password: (leave empty)

## Testing

### Run Tests
```bash
cd code
mvn test
```

### Generate Coverage Report
```bash
cd code
mvn jacoco:report
```

Coverage report will be available at: `code/target/site/jacoco/index.html`

## Sample Products
The application initializes with 5 sample products:
1. Wireless Mouse - $29.99 (Stock: 100)
2. Mechanical Keyboard - $89.99 (Stock: 50)
3. USB-C Cable - $12.99 (Stock: 200)
4. Laptop Stand - $45.00 (Stock: 75)
5. Webcam HD - $65.50 (Stock: 30)

## CORS Configuration
CORS is enabled for Angular integration:
- Allowed Origin: `http://localhost:4200`
- Allowed Methods: All
- Allowed Headers: All
- Credentials: Enabled

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/
│   │   │   ├── models/
│   │   │   │   ├── dtos/
│   │   │   │   ├── entities/
│   │   │   │   └── repositories/
│   │   │   ├── services/
│   │   │   │   ├── impl/
│   │   │   │   └── interfaces/
│   │   │   ├── config/
│   │   │   ├── exceptions/
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/myproject/
└── pom.xml
```

## Error Handling
The API uses standardized error responses:
- 400: Validation errors
- 401: Unauthorized
- 404: Resource not found
- 409: Out of stock
- 500: Internal server error

## Security
- HTTP Basic Authentication
- JWT token support (configured)
- HTTPS ready
- CSRF protection (disabled for REST API)

## Monitoring
Actuator endpoints available at:
- Health: `/actuator/health`
- Info: `/actuator/info`
- Metrics: `/actuator/metrics`

## GitHub Actions
CI/CD pipeline configured with:
- Automated build on push
- Unit test execution
- Code coverage reporting
- Test results artifacts

## License
Generated for demonstration purposes.

## Support
For issues or questions, please refer to the LLD documentation.
