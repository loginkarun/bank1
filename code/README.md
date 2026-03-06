# myproject - Shopping Cart Management System

## Overview
A complete SpringBoot REST API for managing shopping carts with full CRUD operations.

## Technology Stack
- **Java**: JDK 21
- **Spring Boot**: 3.5.9
- **Database**: H2 (In-Memory)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Code Coverage**: JaCoCo
- **API Documentation**: Swagger/OpenAPI

## Features
- ✅ Add items to cart
- ✅ View cart contents
- ✅ Remove items from cart
- ✅ Update item quantities
- ✅ Clear entire cart
- ✅ Product stock validation
- ✅ Comprehensive error handling
- ✅ CORS enabled for Angular integration
- ✅ Full test coverage

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/          # REST Controllers
│   │   │   ├── models/
│   │   │   │   ├── dtos/            # Data Transfer Objects
│   │   │   │   ├── entities/        # JPA Entities
│   │   │   │   └── repositories/    # JPA Repositories
│   │   │   ├── services/
│   │   │   │   ├── impl/            # Service Implementations
│   │   │   │   └── interfaces/      # Service Interfaces
│   │   │   ├── config/              # Configuration Classes
│   │   │   ├── exceptions/          # Custom Exceptions
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                        # Unit Tests
└── pom.xml
```

## API Endpoints

### Cart Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cart/add` | Add item to cart |
| GET | `/api/cart` | Get cart contents |
| DELETE | `/api/cart/remove` | Remove item from cart |
| PUT | `/api/cart/update` | Update item quantity |
| DELETE | `/api/cart/clear` | Clear entire cart |

## Getting Started

### Prerequisites
- JDK 21
- Maven 3.6+

### Build and Run

```bash
# Navigate to code directory
cd code

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access Swagger UI
Once the application is running, access the API documentation at:
```
http://localhost:8080/api/swagger-ui.html
```

### Access H2 Console
For database inspection:
```
http://localhost:8080/api/h2-console
```
- JDBC URL: `jdbc:h2:mem:cartdb`
- Username: `sa`
- Password: (leave empty)

## Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean verify

# View coverage report
open target/site/jacoco/index.html
```

## Sample Products
The application initializes with sample products:
1. Wireless Mouse - $29.99 (Stock: 100)
2. Mechanical Keyboard - $89.99 (Stock: 50)
3. USB-C Hub - $49.99 (Stock: 75)
4. Laptop Stand - $39.99 (Stock: 30)
5. Webcam HD - $79.99 (Stock: 20)

## API Usage Examples

### Add Item to Cart
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -H "X-User-ID: 550e8400-e29b-41d4-a716-446655440000" \
  -d '{
    "productId": "<product-uuid>",
    "quantity": 2
  }'
```

### Get Cart
```bash
curl -X GET http://localhost:8080/api/cart \
  -H "X-User-ID: 550e8400-e29b-41d4-a716-446655440000"
```

### Update Item Quantity
```bash
curl -X PUT http://localhost:8080/api/cart/update \
  -H "Content-Type: application/json" \
  -H "X-User-ID: 550e8400-e29b-41d4-a716-446655440000" \
  -d '{
    "productId": "<product-uuid>",
    "quantity": 5
  }'
```

### Remove Item
```bash
curl -X DELETE http://localhost:8080/api/cart/remove \
  -H "Content-Type: application/json" \
  -H "X-User-ID: 550e8400-e29b-41d4-a716-446655440000" \
  -d '{
    "productId": "<product-uuid>"
  }'
```

### Clear Cart
```bash
curl -X DELETE http://localhost:8080/api/cart/clear \
  -H "X-User-ID: 550e8400-e29b-41d4-a716-446655440000"
```

## Error Handling
The API returns standardized error responses:

```json
{
  "timestamp": "2024-01-15T12:00:00Z",
  "traceId": "trace_xyz789",
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product not found with ID: ...",
  "details": []
}
```

### Error Codes
- `PRODUCT_NOT_FOUND` (404): Product does not exist
- `OUT_OF_STOCK` (409): Insufficient stock
- `CART_NOT_FOUND` (404): Cart not found
- `INVALID_QUANTITY` (400): Invalid quantity provided
- `VALIDATION_ERROR` (400): Input validation failed

## CORS Configuration
CORS is pre-configured for Angular development:
- Allowed Origin: `http://localhost:4200`
- Allowed Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
- Credentials: Enabled

## Security
- JWT/Session token support configured
- All endpoints accessible for demo (authentication disabled)
- Production deployment should enable authentication

## GitHub Actions
Automated build and test pipeline configured:
- Builds on JDK 21
- Runs all tests
- Generates coverage reports
- Uploads artifacts

## License
This project is generated for demonstration purposes.

## Contact
For questions or issues, please refer to the project documentation.