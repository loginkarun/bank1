# Shopping Cart API - Test Case Documentation

## Project Information
- **Project Name**: myproject - Shopping Cart Management System
- **Base URL**: http://localhost:8080
- **API Version**: 1.0.0
- **Test Suite Version**: 1.0
- **Last Updated**: 2024-01-15

---

## Table of Contents
1. [Test Environment Setup](#test-environment-setup)
2. [Positive Test Cases](#positive-test-cases)
3. [Negative Test Cases](#negative-test-cases)
4. [Edge Case Test Cases](#edge-case-test-cases)
5. [Test Data](#test-data)
6. [Prerequisites](#prerequisites)

---

## Test Environment Setup

### Prerequisites
- Java 21 installed
- Maven 3.6+ installed
- Application running on http://localhost:8080
- H2 database initialized with sample products
- Basic authentication credentials: admin/admin123

### Sample Products Available
1. Wireless Mouse - $29.99 (Stock: 100)
2. Mechanical Keyboard - $89.99 (Stock: 50)
3. USB-C Cable - $12.99 (Stock: 200)
4. Laptop Stand - $45.00 (Stock: 75)
5. Webcam HD - $65.50 (Stock: 30)

---

## Positive Test Cases

### TC001: Add Item to Cart - Success

**Test Case ID**: TC001  
**Priority**: High  
**Category**: Positive Test  
**Endpoint**: POST /api/cart/add

**Description**: Verify that a user can successfully add a product to their shopping cart with valid quantity.

**Preconditions**:
- Application is running
- User is authenticated
- Product exists in database
- Product has sufficient stock

**Test Data**:
```json
{
  "productId": "<valid-product-uuid>",
  "quantity": 2
}
```

**Test Steps**:
1. Authenticate with valid credentials (admin/admin123)
2. Send POST request to /api/cart/add with valid product ID and quantity
3. Verify response status code
4. Verify response body structure
5. Verify cart contains the added item

**Expected Result**:
- Status Code: 201 Created
- Response contains:
  - Cart ID (UUID)
  - User ID (UUID)
  - Items array with added product
  - Total price calculated correctly
  - Created and updated timestamps
- Item details include:
  - Product ID
  - Product name
  - Quantity: 2
  - Price per unit
  - Subtotal (price × quantity)

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Response time should be under 500ms

---

### TC002: Get Cart - Success

**Test Case ID**: TC002  
**Priority**: High  
**Category**: Positive Test  
**Endpoint**: GET /api/cart

**Description**: Verify that a user can retrieve their current shopping cart contents.

**Preconditions**:
- Application is running
- User is authenticated
- User has an existing cart (may be empty)

**Test Steps**:
1. Authenticate with valid credentials
2. Send GET request to /api/cart
3. Verify response status code
4. Verify response body structure
5. Verify all cart items are returned
6. Verify total is calculated correctly

**Expected Result**:
- Status Code: 200 OK
- Response contains:
  - Cart ID
  - User ID
  - Items array (may be empty)
  - Total price
  - Timestamps
- If cart has items:
  - Each item has complete details
  - Subtotals are correct
  - Total equals sum of all subtotals

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Response time should be under 300ms

---

### TC003: Update Item Quantity - Success

**Test Case ID**: TC003  
**Priority**: High  
**Category**: Positive Test  
**Endpoint**: PUT /api/cart/update

**Description**: Verify that a user can update the quantity of an existing item in their cart.

**Preconditions**:
- Application is running
- User is authenticated
- Cart contains the product to be updated
- New quantity is within stock limits

**Test Data**:
```json
{
  "productId": "<existing-product-uuid>",
  "quantity": 5
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Add an item to cart (if not already present)
3. Send PUT request to /api/cart/update with new quantity
4. Verify response status code
5. Verify item quantity is updated
6. Verify subtotal is recalculated
7. Verify cart total is recalculated

**Expected Result**:
- Status Code: 200 OK
- Response contains updated cart
- Item quantity changed to 5
- Item subtotal = price × 5
- Cart total reflects the change

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC004: Remove Item from Cart - Success

**Test Case ID**: TC004  
**Priority**: High  
**Category**: Positive Test  
**Endpoint**: DELETE /api/cart/remove

**Description**: Verify that a user can remove a specific product from their cart.

**Preconditions**:
- Application is running
- User is authenticated
- Cart contains the product to be removed

**Test Data**:
```json
{
  "productId": "<existing-product-uuid>"
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Add an item to cart (if not already present)
3. Send DELETE request to /api/cart/remove with product ID
4. Verify response status code
5. Verify item is removed from cart
6. Verify cart total is recalculated

**Expected Result**:
- Status Code: 200 OK
- Response contains updated cart
- Specified item is no longer in items array
- Cart total is reduced by removed item's subtotal
- Other items remain unchanged

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC005: Clear Cart - Success

**Test Case ID**: TC005  
**Priority**: Medium  
**Category**: Positive Test  
**Endpoint**: DELETE /api/cart/clear

**Description**: Verify that a user can clear all items from their cart at once.

**Preconditions**:
- Application is running
- User is authenticated
- Cart exists (may or may not contain items)

**Test Steps**:
1. Authenticate with valid credentials
2. Add multiple items to cart (if not already present)
3. Send DELETE request to /api/cart/clear
4. Verify response status code
5. Verify all items are removed
6. Verify cart total is zero

**Expected Result**:
- Status Code: 200 OK
- Response contains empty cart
- Items array is empty
- Total is 0.00
- Cart ID and user ID remain the same

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

## Negative Test Cases

### TC006: Add Item - Invalid Product ID

**Test Case ID**: TC006  
**Priority**: High  
**Category**: Negative Test  
**Endpoint**: POST /api/cart/add

**Description**: Verify that the system returns appropriate error when attempting to add a non-existent product.

**Preconditions**:
- Application is running
- User is authenticated

**Test Data**:
```json
{
  "productId": "00000000-0000-0000-0000-000000000000",
  "quantity": 2
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Send POST request with non-existent product ID
3. Verify error response

**Expected Result**:
- Status Code: 404 Not Found
- Error response contains:
  - timestamp
  - traceId
  - errorCode: "PRODUCT_NOT_FOUND"
  - message: "Product not found with ID: ..."
  - details array (may be empty)

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC007: Add Item - Invalid Quantity (Zero)

**Test Case ID**: TC007  
**Priority**: High  
**Category**: Negative Test  
**Endpoint**: POST /api/cart/add

**Description**: Verify that the system rejects requests with zero quantity.

**Preconditions**:
- Application is running
- User is authenticated

**Test Data**:
```json
{
  "productId": "<valid-product-uuid>",
  "quantity": 0
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Send POST request with quantity = 0
3. Verify validation error response

**Expected Result**:
- Status Code: 400 Bad Request
- Error response contains:
  - errorCode: "VALIDATION_ERROR"
  - message: "Input validation failed"
  - details array with field-level errors:
    - field: "quantity"
    - issue: "Quantity must be at least 1"

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC008: Add Item - Invalid Quantity (Negative)

**Test Case ID**: TC008  
**Priority**: High  
**Category**: Negative Test  
**Endpoint**: POST /api/cart/add

**Description**: Verify that the system rejects requests with negative quantity.

**Preconditions**:
- Application is running
- User is authenticated

**Test Data**:
```json
{
  "productId": "<valid-product-uuid>",
  "quantity": -5
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Send POST request with negative quantity
3. Verify validation error response

**Expected Result**:
- Status Code: 400 Bad Request
- Error response contains:
  - errorCode: "VALIDATION_ERROR"
  - message: "Input validation failed"
  - details array with validation error for quantity field

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC009: Add Item - Out of Stock

**Test Case ID**: TC009  
**Priority**: High  
**Category**: Negative Test  
**Endpoint**: POST /api/cart/add

**Description**: Verify that the system prevents adding more items than available in stock.

**Preconditions**:
- Application is running
- User is authenticated
- Product exists with limited stock

**Test Data**:
```json
{
  "productId": "<valid-product-uuid>",
  "quantity": 999999
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Send POST request with quantity exceeding stock
3. Verify out of stock error response

**Expected Result**:
- Status Code: 409 Conflict
- Error response contains:
  - errorCode: "OUT_OF_STOCK"
  - message: "Product ... is out of stock. Requested: 999999, Available: ..."
  - Details about requested vs available quantity

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC010: Get Cart - Unauthorized

**Test Case ID**: TC010  
**Priority**: High  
**Category**: Negative Test - Security  
**Endpoint**: GET /api/cart

**Description**: Verify that unauthenticated users cannot access cart data.

**Preconditions**:
- Application is running
- No authentication credentials provided

**Test Steps**:
1. Send GET request to /api/cart without authentication
2. Verify unauthorized error response

**Expected Result**:
- Status Code: 401 Unauthorized
- Response may contain WWW-Authenticate header
- No cart data is returned

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC011: Remove Item - Product Not in Cart

**Test Case ID**: TC011  
**Priority**: Medium  
**Category**: Negative Test  
**Endpoint**: DELETE /api/cart/remove

**Description**: Verify appropriate error when attempting to remove a product that is not in the cart.

**Preconditions**:
- Application is running
- User is authenticated
- Cart exists but does not contain the specified product

**Test Data**:
```json
{
  "productId": "<valid-but-not-in-cart-uuid>"
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Ensure product is not in cart
3. Send DELETE request to remove the product
4. Verify error response

**Expected Result**:
- Status Code: 404 Not Found
- Error response contains:
  - errorCode: "CART_NOT_FOUND" or similar
  - message indicating product not found in cart

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC012: Update Item - Missing Required Fields

**Test Case ID**: TC012  
**Priority**: Medium  
**Category**: Negative Test - Validation  
**Endpoint**: PUT /api/cart/update

**Description**: Verify that the system validates required fields in update requests.

**Preconditions**:
- Application is running
- User is authenticated

**Test Data**:
```json
{
  "productId": "<valid-product-uuid>"
}
```
(Missing quantity field)

**Test Steps**:
1. Authenticate with valid credentials
2. Send PUT request without quantity field
3. Verify validation error response

**Expected Result**:
- Status Code: 400 Bad Request
- Error response contains:
  - errorCode: "VALIDATION_ERROR"
  - details array indicating missing quantity field

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

## Edge Case Test Cases

### TC013: Add Item - Maximum Quantity

**Test Case ID**: TC013  
**Priority**: Medium  
**Category**: Edge Case  
**Endpoint**: POST /api/cart/add

**Description**: Verify system behavior when adding items with maximum available stock quantity.

**Preconditions**:
- Application is running
- User is authenticated
- Product has known stock quantity

**Test Data**:
```json
{
  "productId": "<valid-product-uuid>",
  "quantity": 100
}
```
(Assuming product has exactly 100 in stock)

**Test Steps**:
1. Authenticate with valid credentials
2. Identify product with known stock (e.g., 100 units)
3. Send POST request with quantity equal to stock
4. Verify successful addition

**Expected Result**:
- Status Code: 201 Created
- Item added successfully with maximum quantity
- Cart total reflects full stock value
- Subsequent attempts to add more should fail with OUT_OF_STOCK

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC014: Update Item - Quantity to 1

**Test Case ID**: TC014  
**Priority**: Low  
**Category**: Edge Case  
**Endpoint**: PUT /api/cart/update

**Description**: Verify that item quantity can be updated to minimum valid value (1).

**Preconditions**:
- Application is running
- User is authenticated
- Cart contains item with quantity > 1

**Test Data**:
```json
{
  "productId": "<existing-product-uuid>",
  "quantity": 1
}
```

**Test Steps**:
1. Authenticate with valid credentials
2. Add item with quantity > 1
3. Send PUT request to update quantity to 1
4. Verify successful update

**Expected Result**:
- Status Code: 200 OK
- Item quantity updated to 1
- Subtotal equals single unit price
- Cart total recalculated correctly

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

### TC015: Clear Empty Cart

**Test Case ID**: TC015  
**Priority**: Low  
**Category**: Edge Case  
**Endpoint**: DELETE /api/cart/clear

**Description**: Verify system behavior when clearing an already empty cart.

**Preconditions**:
- Application is running
- User is authenticated
- Cart exists but is empty

**Test Steps**:
1. Authenticate with valid credentials
2. Ensure cart is empty (clear if needed)
3. Send DELETE request to /api/cart/clear
4. Verify response

**Expected Result**:
- Status Code: 200 OK
- Response contains empty cart
- Items array remains empty
- Total remains 0.00
- No errors occur

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

---

## Test Data

### Valid Test Users
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN, USER |

### Sample Product IDs
(To be populated after application startup from H2 database)

| Product Name | Price | Stock | Product ID |
|--------------|-------|-------|------------|
| Wireless Mouse | $29.99 | 100 | [UUID from DB] |
| Mechanical Keyboard | $89.99 | 50 | [UUID from DB] |
| USB-C Cable | $12.99 | 200 | [UUID from DB] |
| Laptop Stand | $45.00 | 75 | [UUID from DB] |
| Webcam HD | $65.50 | 30 | [UUID from DB] |

### Test User IDs
| User | User ID (UUID) |
|------|----------------|
| Test User 1 | 550e8400-e29b-41d4-a716-446655440000 |

---

## Prerequisites

### Environment Setup
1. **Java 21** installed and configured
2. **Maven 3.6+** installed
3. **Postman** or **Newman** for test execution
4. **Git** for version control

### Application Setup
```bash
# Clone repository
git clone <repository-url>

# Navigate to code directory
cd code

# Build application
mvn clean install

# Run application
mvn spring-boot:run

# Verify application is running
curl http://localhost:8080/actuator/health
```

### Database Setup
- H2 in-memory database is auto-configured
- Sample data is loaded on startup via DataInitializer
- Access H2 Console: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:cartdb
  - Username: sa
  - Password: (empty)

### Postman Collection Import
1. Open Postman
2. Click Import
3. Select `test/postman/collection.json`
4. Select `test/postman/environment.json`
5. Update environment variables with actual product IDs from database

---

## Test Execution Instructions

### Manual Execution (Postman)
1. Start the application
2. Import collection and environment
3. Update productId variable with actual UUID from database
4. Run collection using Collection Runner
5. Review test results

### Automated Execution (Newman)
```bash
# Install Newman
npm install -g newman

# Run collection
newman run test/postman/collection.json \n  -e test/postman/environment.json \n  --reporters cli,json,html \n  --reporter-html-export test/reports/newman-report.html
```

---

## Test Coverage Summary

### Endpoint Coverage
| Endpoint | Method | Test Cases | Coverage |
|----------|--------|------------|----------|
| /api/cart/add | POST | 7 | 100% |
| /api/cart | GET | 2 | 100% |
| /api/cart/update | PUT | 3 | 100% |
| /api/cart/remove | DELETE | 2 | 100% |
| /api/cart/clear | DELETE | 2 | 100% |

### Test Type Distribution
- **Positive Tests**: 5 (33%)
- **Negative Tests**: 7 (47%)
- **Edge Cases**: 3 (20%)
- **Total**: 15 test cases

### Scenario Coverage
- ✅ Happy path scenarios
- ✅ Validation errors
- ✅ Business rule violations (out of stock)
- ✅ Authentication/Authorization
- ✅ Not found scenarios
- ✅ Edge cases (min/max values)
- ✅ Empty state handling

---

## Notes

1. **Product IDs**: Must be retrieved from H2 database after application startup. Use H2 Console or GET /api/products endpoint if available.

2. **Authentication**: All endpoints require Basic Authentication with credentials admin/admin123.

3. **User ID**: For testing purposes, user ID is derived from username. In production, it would come from JWT token.

4. **Stock Management**: Tests assume initial stock levels. Running tests multiple times may affect stock availability.

5. **Test Isolation**: For best results, restart application between test runs to reset database state.

6. **Response Times**: Expected response times are guidelines. Actual times may vary based on system resources.

7. **Error Codes**: All error responses follow standardized format with errorCode, message, timestamp, and traceId.

---

## Appendix

### Error Code Reference
| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| PRODUCT_NOT_FOUND | 404 | Product does not exist |
| OUT_OF_STOCK | 409 | Insufficient stock available |
| CART_NOT_FOUND | 404 | Cart not found for user |
| INVALID_QUANTITY | 400 | Invalid quantity value |
| VALIDATION_ERROR | 400 | Input validation failed |
| INTERNAL_SERVER_ERROR | 500 | Unexpected server error |

### API Response Schema

#### Success Response (Cart)
```json
{
  "id": "uuid",
  "userId": "uuid",
  "sessionId": "string",
  "items": [
    {
      "id": "uuid",
      "productId": "uuid",
      "productName": "string",
      "quantity": "integer",
      "price": "decimal",
      "subtotal": "decimal"
    }
  ],
  "total": "decimal",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

#### Error Response
```json
{
  "timestamp": "timestamp",
  "traceId": "string",
  "errorCode": "string",
  "message": "string",
  "details": [
    {
      "field": "string",
      "issue": "string"
    }
  ]
}
```

---

**Document Version**: 1.0  
**Last Updated**: 2024-01-15  
**Author**: QA Automation Team  
**Status**: Ready for Execution