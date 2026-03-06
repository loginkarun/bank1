# API Test Cases - Shopping Cart API

**Project:** Shopping Cart Management System  
**Repository:** bank1  
**Branch:** main  
**Generated:** 2024-01-15  
**Total Endpoints:** 5  
**Base URL:** http://localhost:8080  

---

## Test Coverage Summary

| Category | Count |
|----------|-------|
| Total Endpoints | 5 |
| Positive Test Cases | 15 |
| Negative Test Cases | 20 |
| Edge Cases | 10 |
| **Total Test Cases** | **45** |

---

## Authentication

**Type:** HTTP Basic Authentication  
**Username:** admin  
**Password:** admin123  

All endpoints require authentication.

---

## Endpoint 1: Add Item to Cart

### TC-001: Add New Item to Cart - Happy Path

**Test Case ID:** TC-001  
**Endpoint:** POST /api/cart/add  
**Scenario:** Successfully add a new product to an empty cart  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Product exists in database
- Product has sufficient stock
- Cart is empty or doesn't exist

**Test Steps:**
1. Authenticate with valid credentials (admin/admin123)
2. Send POST request to /api/cart/add
3. Include valid productId and quantity in request body

**Request Body:**
```json
{
  "productId": "{{valid_product_id}}",
  "quantity": 2
}
```

**Expected Result:**
- Status Code: 201 Created
- Response contains CartDTO with:
  - Valid cart ID
  - User ID matches authenticated user
  - Items array contains 1 item
  - Item quantity is 2
  - Total is calculated correctly (price * quantity)
  - createdAt and updatedAt timestamps are present

**Assertions:**
- Response status code is 201
- Response body has id field
- Response body has userId field
- items array length is 1
- items[0].quantity equals 2
- items[0].productId equals request productId
- total equals items[0].subtotal
- subtotal equals price * quantity

---

### TC-002: Add Item to Existing Cart - Increment Quantity

**Test Case ID:** TC-002  
**Endpoint:** POST /api/cart/add  
**Scenario:** Add same product again to increment quantity  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Cart already exists with 1 item (productId: X, quantity: 2)
- Product has sufficient stock for additional quantity

**Test Steps:**
1. Authenticate with valid credentials
2. Add product X with quantity 2 (first time)
3. Add same product X with quantity 3 (second time)
4. Verify quantity is incremented to 5

**Request Body:**
```json
{
  "productId": "{{existing_product_id}}",
  "quantity": 3
}
```

**Expected Result:**
- Status Code: 201 Created
- Cart contains 1 item (not 2 separate items)
- Item quantity is 5 (2 + 3)
- Total is recalculated correctly
- updatedAt timestamp is updated

**Assertions:**
- items array length is 1
- items[0].quantity equals 5
- total equals price * 5
- updatedAt > createdAt

---

### TC-003: Add Multiple Different Products

**Test Case ID:** TC-003  
**Endpoint:** POST /api/cart/add  
**Scenario:** Add different products to cart  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Multiple products exist in database
- All products have sufficient stock

**Test Steps:**
1. Authenticate with valid credentials
2. Add product A with quantity 2
3. Add product B with quantity 1
4. Add product C with quantity 3
5. Verify cart contains 3 distinct items

**Expected Result:**
- Status Code: 201 Created (for each request)
- Cart contains 3 items
- Each item has correct productId and quantity
- Total is sum of all subtotals

**Assertions:**
- items array length is 3
- Each productId is unique
- total equals sum of all subtotals

---

### TC-004: Add Item - Product Not Found

**Test Case ID:** TC-004  
**Endpoint:** POST /api/cart/add  
**Scenario:** Attempt to add non-existent product  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- ProductId does not exist in database

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with invalid/non-existent productId

**Request Body:**
```json
{
  "productId": "00000000-0000-0000-0000-000000000000",
  "quantity": 2
}
```

**Expected Result:**
- Status Code: 404 Not Found
- Error response with:
  - errorCode: "PRODUCT_NOT_FOUND"
  - message: "Product not found with ID: ..."
  - timestamp present
  - traceId present

**Assertions:**
- Response status code is 404
- errorCode equals "PRODUCT_NOT_FOUND"
- message contains "Product not found"
- traceId is not null

---

### TC-005: Add Item - Out of Stock

**Test Case ID:** TC-005  
**Endpoint:** POST /api/cart/add  
**Scenario:** Attempt to add quantity exceeding available stock  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Product exists with stock = 10

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = 20 (exceeds stock)

**Request Body:**
```json
{
  "productId": "{{valid_product_id}}",
  "quantity": 20
}
```

**Expected Result:**
- Status Code: 409 Conflict
- Error response with:
  - errorCode: "OUT_OF_STOCK"
  - message contains requested and available quantities
  - timestamp and traceId present

**Assertions:**
- Response status code is 409
- errorCode equals "OUT_OF_STOCK"
- message contains "out of stock"
- message contains requested quantity
- message contains available quantity

---

### TC-006: Add Item - Invalid Quantity (Zero)

**Test Case ID:** TC-006  
**Endpoint:** POST /api/cart/add  
**Scenario:** Attempt to add item with quantity = 0  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Product exists

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = 0

**Request Body:**
```json
{
  "productId": "{{valid_product_id}}",
  "quantity": 0
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Error response with:
  - errorCode: "VALIDATION_ERROR"
  - message: "Input validation failed"
  - details array contains field error for "quantity"
  - details[0].issue: "Quantity must be at least 1"

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"
- details array is not empty
- details[0].field equals "quantity"
- details[0].issue contains "at least 1"

---

### TC-007: Add Item - Negative Quantity

**Test Case ID:** TC-007  
**Endpoint:** POST /api/cart/add  
**Scenario:** Attempt to add item with negative quantity  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Product exists

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = -5

**Request Body:**
```json
{
  "productId": "{{valid_product_id}}",
  "quantity": -5
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Validation error for quantity field

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"
- details contains quantity field error

---

### TC-008: Add Item - Missing ProductId

**Test Case ID:** TC-008  
**Endpoint:** POST /api/cart/add  
**Scenario:** Attempt to add item without productId  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request without productId field

**Request Body:**
```json
{
  "quantity": 2
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Validation error for productId field
- details[0].field: "productId"
- details[0].issue: "Product ID is required"

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"
- details array contains productId error
- details[0].issue contains "required"

---

### TC-009: Add Item - Unauthorized Access

**Test Case ID:** TC-009  
**Endpoint:** POST /api/cart/add  
**Scenario:** Attempt to add item without authentication  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- No authentication credentials provided

**Test Steps:**
1. Send POST request without Authorization header
2. Include valid request body

**Request Body:**
```json
{
  "productId": "{{valid_product_id}}",
  "quantity": 2
}
```

**Expected Result:**
- Status Code: 401 Unauthorized
- No cart data returned

**Assertions:**
- Response status code is 401
- Response body is empty or contains authentication error

---

### TC-010: Add Item - Invalid JSON Format

**Test Case ID:** TC-010  
**Endpoint:** POST /api/cart/add  
**Scenario:** Send malformed JSON in request body  
**Priority:** Low  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with invalid JSON

**Request Body:**
```
{productId: invalid, quantity: 2
```

**Expected Result:**
- Status Code: 400 Bad Request
- Error indicating JSON parse failure

**Assertions:**
- Response status code is 400

---

## Endpoint 2: Get Cart

### TC-011: Get Cart - Cart Exists

**Test Case ID:** TC-011  
**Endpoint:** GET /api/cart  
**Scenario:** Retrieve existing cart with items  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Cart exists with 2 items

**Test Steps:**
1. Authenticate with valid credentials
2. Send GET request to /api/cart

**Expected Result:**
- Status Code: 200 OK
- Response contains CartDTO with:
  - Valid cart ID
  - User ID matches authenticated user
  - Items array with 2 items
  - Correct total calculation
  - Timestamps present

**Assertions:**
- Response status code is 200
- id field is present
- userId matches authenticated user
- items array length is 2
- total equals sum of subtotals
- createdAt and updatedAt are present

---

### TC-012: Get Cart - Empty Cart

**Test Case ID:** TC-012  
**Endpoint:** GET /api/cart  
**Scenario:** Retrieve cart with no items  
**Priority:** Medium  

**Preconditions:**
- User is authenticated
- Cart exists but is empty

**Test Steps:**
1. Authenticate with valid credentials
2. Create cart (or clear existing cart)
3. Send GET request to /api/cart

**Expected Result:**
- Status Code: 200 OK
- Cart exists with:
  - items array is empty
  - total is 0.00

**Assertions:**
- Response status code is 200
- items array length is 0
- total equals 0

---

### TC-013: Get Cart - Cart Not Found

**Test Case ID:** TC-013  
**Endpoint:** GET /api/cart  
**Scenario:** Attempt to get cart for user with no cart  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- No cart exists for user

**Test Steps:**
1. Authenticate with valid credentials (new user)
2. Send GET request to /api/cart

**Expected Result:**
- Status Code: 404 Not Found
- Error response with:
  - errorCode: "CART_NOT_FOUND"
  - message: "Cart not found for user: ..."

**Assertions:**
- Response status code is 404
- errorCode equals "CART_NOT_FOUND"
- message contains "Cart not found"

---

### TC-014: Get Cart - Unauthorized

**Test Case ID:** TC-014  
**Endpoint:** GET /api/cart  
**Scenario:** Attempt to get cart without authentication  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- No authentication credentials

**Test Steps:**
1. Send GET request without Authorization header

**Expected Result:**
- Status Code: 401 Unauthorized

**Assertions:**
- Response status code is 401

---

## Endpoint 3: Remove Item from Cart

### TC-015: Remove Item - Success

**Test Case ID:** TC-015  
**Endpoint:** DELETE /api/cart/remove  
**Scenario:** Successfully remove an item from cart  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Cart exists with 2 items (Product A and Product B)

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request to /api/cart/remove
3. Include productId of Product A in request body

**Request Body:**
```json
{
  "productId": "{{product_a_id}}"
}
```

**Expected Result:**
- Status Code: 200 OK
- Response contains updated CartDTO with:
  - items array contains only 1 item (Product B)
  - total is recalculated (only Product B subtotal)
  - updatedAt timestamp is updated

**Assertions:**
- Response status code is 200
- items array length is 1
- Removed product is not in items array
- total equals remaining item subtotal
- updatedAt > previous updatedAt

---

### TC-016: Remove Last Item from Cart

**Test Case ID:** TC-016  
**Endpoint:** DELETE /api/cart/remove  
**Scenario:** Remove the only item in cart  
**Priority:** Medium  

**Preconditions:**
- User is authenticated
- Cart exists with 1 item

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request with the only product's ID

**Request Body:**
```json
{
  "productId": "{{only_product_id}}"
}
```

**Expected Result:**
- Status Code: 200 OK
- Cart is empty:
  - items array is empty
  - total is 0.00

**Assertions:**
- Response status code is 200
- items array length is 0
- total equals 0

---

### TC-017: Remove Item - Product Not in Cart

**Test Case ID:** TC-017  
**Endpoint:** DELETE /api/cart/remove  
**Scenario:** Attempt to remove product not in cart  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Cart exists with Product A
- Product B exists but not in cart

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request with Product B's ID

**Request Body:**
```json
{
  "productId": "{{product_not_in_cart_id}}"
}
```

**Expected Result:**
- Status Code: 404 Not Found
- Error response with:
  - errorCode: "CART_NOT_FOUND" or "PRODUCT_NOT_FOUND"
  - message: "Product not found in cart"

**Assertions:**
- Response status code is 404
- message contains "not found"

---

### TC-018: Remove Item - Missing ProductId

**Test Case ID:** TC-018  
**Endpoint:** DELETE /api/cart/remove  
**Scenario:** Send request without productId  
**Priority:** Low  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request with empty body or missing productId

**Request Body:**
```json
{}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Validation error for productId

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"
- details contains productId error

---

### TC-019: Remove Item - Unauthorized

**Test Case ID:** TC-019  
**Endpoint:** DELETE /api/cart/remove  
**Scenario:** Attempt to remove item without authentication  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- No authentication credentials

**Test Steps:**
1. Send DELETE request without Authorization header

**Request Body:**
```json
{
  "productId": "{{valid_product_id}}"
}
```

**Expected Result:**
- Status Code: 401 Unauthorized

**Assertions:**
- Response status code is 401

---

## Endpoint 4: Update Item Quantity

### TC-020: Update Quantity - Increase

**Test Case ID:** TC-020  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Increase quantity of existing item  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Cart contains Product A with quantity 2
- Product A has sufficient stock (>= 5)

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request to /api/cart/update
3. Set quantity to 5

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": 5
}
```

**Expected Result:**
- Status Code: 200 OK
- Response contains updated CartDTO with:
  - Item quantity is 5
  - Subtotal is recalculated (price * 5)
  - Total is recalculated
  - updatedAt timestamp is updated

**Assertions:**
- Response status code is 200
- items[0].quantity equals 5
- items[0].subtotal equals price * 5
- total is updated correctly

---

### TC-021: Update Quantity - Decrease

**Test Case ID:** TC-021  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Decrease quantity of existing item  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Cart contains Product A with quantity 5

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request to decrease quantity to 2

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": 2
}
```

**Expected Result:**
- Status Code: 200 OK
- Item quantity is 2
- Subtotal and total are recalculated

**Assertions:**
- Response status code is 200
- items[0].quantity equals 2
- subtotal and total are correct

---

### TC-022: Update Quantity - Set to 1

**Test Case ID:** TC-022  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Update quantity to minimum valid value (1)  
**Priority:** Medium  

**Preconditions:**
- User is authenticated
- Cart contains Product A with quantity 5

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request to set quantity to 1

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": 1
}
```

**Expected Result:**
- Status Code: 200 OK
- Item quantity is 1
- Subtotal equals price

**Assertions:**
- Response status code is 200
- items[0].quantity equals 1
- items[0].subtotal equals items[0].price

---

### TC-023: Update Quantity - Product Not in Cart

**Test Case ID:** TC-023  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Attempt to update quantity of product not in cart  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Cart exists but doesn't contain Product B

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request for Product B

**Request Body:**
```json
{
  "productId": "{{product_not_in_cart_id}}",
  "quantity": 3
}
```

**Expected Result:**
- Status Code: 404 Not Found
- Error message: "Product not found in cart"

**Assertions:**
- Response status code is 404
- message contains "not found in cart"

---

### TC-024: Update Quantity - Exceeds Stock

**Test Case ID:** TC-024  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Attempt to update quantity beyond available stock  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Cart contains Product A with quantity 2
- Product A has stock = 10

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request to set quantity to 20

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": 20
}
```

**Expected Result:**
- Status Code: 409 Conflict
- Error response with:
  - errorCode: "OUT_OF_STOCK"
  - message contains requested and available quantities

**Assertions:**
- Response status code is 409
- errorCode equals "OUT_OF_STOCK"
- message contains stock information

---

### TC-025: Update Quantity - Zero

**Test Case ID:** TC-025  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Attempt to set quantity to 0  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Cart contains Product A

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with quantity = 0

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": 0
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Validation error: "Quantity must be at least 1"

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"
- details contains quantity validation error

---

### TC-026: Update Quantity - Negative Value

**Test Case ID:** TC-026  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Attempt to set negative quantity  
**Priority:** Low  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- Cart contains Product A

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with quantity = -3

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": -3
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Validation error for quantity

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"

---

### TC-027: Update Quantity - Missing Fields

**Test Case ID:** TC-027  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Send request with missing required fields  
**Priority:** Low  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with missing productId or quantity

**Request Body:**
```json
{
  "productId": "{{product_a_id}}"
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Validation errors for missing fields

**Assertions:**
- Response status code is 400
- errorCode equals "VALIDATION_ERROR"
- details contains field errors

---

### TC-028: Update Quantity - Unauthorized

**Test Case ID:** TC-028  
**Endpoint:** PUT /api/cart/update  
**Scenario:** Attempt to update without authentication  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- No authentication credentials

**Test Steps:**
1. Send PUT request without Authorization header

**Request Body:**
```json
{
  "productId": "{{product_a_id}}",
  "quantity": 5
}
```

**Expected Result:**
- Status Code: 401 Unauthorized

**Assertions:**
- Response status code is 401

---

## Endpoint 5: Clear Cart

### TC-029: Clear Cart - Success

**Test Case ID:** TC-029  
**Endpoint:** DELETE /api/cart/clear  
**Scenario:** Successfully clear cart with multiple items  
**Priority:** High  

**Preconditions:**
- User is authenticated
- Cart exists with 3 items

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request to /api/cart/clear

**Expected Result:**
- Status Code: 200 OK
- Response contains CartDTO with:
  - items array is empty
  - total is 0.00
  - Cart ID remains the same
  - updatedAt timestamp is updated

**Assertions:**
- Response status code is 200
- items array length is 0
- total equals 0
- id field is present (cart still exists)
- updatedAt is updated

---

### TC-030: Clear Cart - Already Empty

**Test Case ID:** TC-030  
**Endpoint:** DELETE /api/cart/clear  
**Scenario:** Clear cart that is already empty  
**Priority:** Low  

**Preconditions:**
- User is authenticated
- Cart exists but is empty

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request to /api/cart/clear

**Expected Result:**
- Status Code: 200 OK
- Cart remains empty
- No errors

**Assertions:**
- Response status code is 200
- items array length is 0
- total equals 0

---

### TC-031: Clear Cart - Cart Not Found

**Test Case ID:** TC-031  
**Endpoint:** DELETE /api/cart/clear  
**Scenario:** Attempt to clear non-existent cart  
**Priority:** Medium  
**Type:** Negative Test  

**Preconditions:**
- User is authenticated
- No cart exists for user

**Test Steps:**
1. Authenticate with valid credentials (new user)
2. Send DELETE request to /api/cart/clear

**Expected Result:**
- Status Code: 404 Not Found
- Error response with:
  - errorCode: "CART_NOT_FOUND"
  - message: "Cart not found for user: ..."

**Assertions:**
- Response status code is 404
- errorCode equals "CART_NOT_FOUND"

---

### TC-032: Clear Cart - Unauthorized

**Test Case ID:** TC-032  
**Endpoint:** DELETE /api/cart/clear  
**Scenario:** Attempt to clear cart without authentication  
**Priority:** High  
**Type:** Negative Test  

**Preconditions:**
- No authentication credentials

**Test Steps:**
1. Send DELETE request without Authorization header

**Expected Result:**
- Status Code: 401 Unauthorized

**Assertions:**
- Response status code is 401

---

## Edge Cases and Additional Scenarios

### TC-033: Concurrent Cart Operations

**Test Case ID:** TC-033  
**Scenario:** Multiple simultaneous operations on same cart  
**Priority:** Medium  
**Type:** Edge Case  

**Test Steps:**
1. Authenticate with valid credentials
2. Send 5 concurrent POST requests to add different products
3. Verify all items are added correctly
4. Verify total is calculated correctly

**Expected Result:**
- All requests succeed
- Cart contains all 5 items
- No data loss or corruption
- Total is accurate

---

### TC-034: Large Quantity Values

**Test Case ID:** TC-034  
**Scenario:** Add item with very large quantity (within stock limits)  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Authenticate with valid credentials
2. Add product with quantity = 1000 (if stock allows)

**Expected Result:**
- Request succeeds if stock is sufficient
- Subtotal and total are calculated correctly
- No integer overflow

---

### TC-035: Special Characters in Product Names

**Test Case ID:** TC-035  
**Scenario:** Handle products with special characters in names  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Add product with name containing special characters (é, ñ, 中文, etc.)
2. Retrieve cart
3. Verify product name is displayed correctly

**Expected Result:**
- Product name is stored and retrieved correctly
- No encoding issues

---

### TC-036: Decimal Price Calculations

**Test Case ID:** TC-036  
**Scenario:** Verify accurate decimal calculations for prices  
**Priority:** High  
**Type:** Edge Case  

**Test Steps:**
1. Add product with price = 19.99
2. Set quantity = 3
3. Verify subtotal = 59.97
4. Add another product with price = 10.01
5. Verify total = 69.98

**Expected Result:**
- All decimal calculations are accurate
- No rounding errors
- Subtotals and totals match expected values

---

### TC-037: Session Persistence

**Test Case ID:** TC-037  
**Scenario:** Verify cart persists across sessions  
**Priority:** High  
**Type:** Edge Case  

**Test Steps:**
1. Authenticate and add items to cart
2. Logout or end session
3. Re-authenticate with same credentials
4. Retrieve cart

**Expected Result:**
- Cart contains same items as before
- Quantities and totals are preserved

---

### TC-038: Maximum Items in Cart

**Test Case ID:** TC-038  
**Scenario:** Add maximum number of different products to cart  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Add 50+ different products to cart
2. Verify all items are stored
3. Retrieve cart
4. Verify performance is acceptable

**Expected Result:**
- All items are added successfully
- Cart retrieval is performant (< 500ms)
- Total is calculated correctly

---

### TC-039: Product Stock Updates During Cart Operations

**Test Case ID:** TC-039  
**Scenario:** Handle stock changes while item is in cart  
**Priority:** Medium  
**Type:** Edge Case  

**Test Steps:**
1. Add product A (stock = 10) with quantity 5 to cart
2. Externally reduce product A stock to 3
3. Attempt to update cart quantity to 8

**Expected Result:**
- Update fails with OUT_OF_STOCK error
- Error message shows current available stock (3)

---

### TC-040: Empty Request Body

**Test Case ID:** TC-040  
**Scenario:** Send requests with empty body where body is required  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Send POST /api/cart/add with empty body
2. Send DELETE /api/cart/remove with empty body
3. Send PUT /api/cart/update with empty body

**Expected Result:**
- All requests return 400 Bad Request
- Validation errors for missing required fields

---

### TC-041: Invalid UUID Format

**Test Case ID:** TC-041  
**Scenario:** Send productId in invalid UUID format  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Send POST request with productId = "invalid-uuid-format"

**Request Body:**
```json
{
  "productId": "not-a-uuid",
  "quantity": 2
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Error indicating invalid UUID format

---

### TC-042: Very Long Product Names

**Test Case ID:** TC-042  
**Scenario:** Handle products with very long names (>255 characters)  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Add product with name exceeding database field limit
2. Verify name is truncated or error is returned

**Expected Result:**
- Either name is truncated gracefully
- Or validation error is returned
- No database errors

---

### TC-043: Rapid Add/Remove Operations

**Test Case ID:** TC-043  
**Scenario:** Rapidly add and remove same item  
**Priority:** Low  
**Type:** Edge Case  

**Test Steps:**
1. Add product A
2. Remove product A
3. Add product A again
4. Repeat 10 times

**Expected Result:**
- All operations succeed
- Final cart state is consistent
- No orphaned data

---

### TC-044: Cart Total Precision

**Test Case ID:** TC-044  
**Scenario:** Verify cart total maintains 2 decimal precision  
**Priority:** Medium  
**Type:** Edge Case  

**Test Steps:**
1. Add multiple items with various prices
2. Verify total has exactly 2 decimal places
3. Verify no floating-point precision errors

**Expected Result:**
- Total always has 2 decimal places
- Calculations are accurate
- Format: XX.XX

---

### TC-045: Authentication Token Expiry

**Test Case ID:** TC-045  
**Scenario:** Handle expired authentication token  
**Priority:** Medium  
**Type:** Edge Case  

**Test Steps:**
1. Authenticate and get token
2. Wait for token to expire
3. Attempt cart operation with expired token

**Expected Result:**
- Status Code: 401 Unauthorized
- Error message indicates token expiry

---

## Performance Test Cases

### TC-046: Response Time - Add Item

**Test Case ID:** TC-046  
**Scenario:** Verify add item response time  
**Priority:** Medium  
**Type:** Performance  

**Test Steps:**
1. Send POST /api/cart/add request
2. Measure response time

**Expected Result:**
- Response time < 500ms
- Status Code: 201

---

### TC-047: Response Time - Get Cart

**Test Case ID:** TC-047  
**Scenario:** Verify get cart response time  
**Priority:** Medium  
**Type:** Performance  

**Test Steps:**
1. Send GET /api/cart request
2. Measure response time

**Expected Result:**
- Response time < 300ms
- Status Code: 200

---

### TC-048: Load Test - Concurrent Users

**Test Case ID:** TC-048  
**Scenario:** Test system under load with multiple concurrent users  
**Priority:** High  
**Type:** Performance  

**Test Steps:**
1. Simulate 100 concurrent users
2. Each user performs add, get, update, remove operations
3. Measure response times and error rates

**Expected Result:**
- 95% of requests complete within 1 second
- Error rate < 1%
- No system crashes

---

## Security Test Cases

### TC-049: SQL Injection Attempt

**Test Case ID:** TC-049  
**Scenario:** Attempt SQL injection in productId field  
**Priority:** High  
**Type:** Security  

**Test Steps:**
1. Send request with SQL injection payload in productId

**Request Body:**
```json
{
  "productId": "'; DROP TABLE cart; --",
  "quantity": 2
}
```

**Expected Result:**
- Request is rejected safely
- No database damage
- Appropriate error response

---

### TC-050: XSS Attempt in Product Name

**Test Case ID:** TC-050  
**Scenario:** Attempt XSS attack via product name  
**Priority:** Medium  
**Type:** Security  

**Test Steps:**
1. Add product with name containing script tags
2. Retrieve cart
3. Verify script is not executed

**Expected Result:**
- Script tags are escaped or sanitized
- No XSS vulnerability

---

## Test Execution Summary Template

```
Test Execution Report
=====================

Execution Date: [DATE]
Executor: [NAME]
Environment: [DEV/QA/STAGING]
Base URL: http://localhost:8080

Test Results:
-------------
Total Test Cases: 50
Passed: [X]
Failed: [Y]
Skipped: [Z]
Blocked: [W]

Pass Rate: [X/50 * 100]%

Failed Test Cases:
------------------
[List of failed test case IDs with failure reasons]

Blocked Test Cases:
-------------------
[List of blocked test case IDs with blocking reasons]

Defects Found:
--------------
[List of defects with severity and status]

Performance Metrics:
--------------------
Average Response Time: [X]ms
Max Response Time: [Y]ms
Min Response Time: [Z]ms

Recommendations:
----------------
[Any recommendations for improvements]

Sign-off:
---------
QA Lead: _______________
Date: _______________
```

---

## Test Data Requirements

### Sample Products

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Wireless Mouse",
    "price": 29.99,
    "stock": 100
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Mechanical Keyboard",
    "price": 89.99,
    "stock": 50
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "name": "USB-C Cable",
    "price": 12.99,
    "stock": 200
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "name": "Laptop Stand",
    "price": 45.00,
    "stock": 75
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440004",
    "name": "Webcam HD",
    "price": 65.50,
    "stock": 30
  }
]
```

### Test Users

```
Username: admin
Password: admin123
Role: USER, ADMIN
```

---

## Appendix

### Error Codes Reference

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| PRODUCT_NOT_FOUND | 404 | Product does not exist |
| OUT_OF_STOCK | 409 | Insufficient stock |
| CART_NOT_FOUND | 404 | Cart does not exist |
| INVALID_QUANTITY | 400 | Quantity validation failed |
| VALIDATION_ERROR | 400 | Input validation failed |
| INTERNAL_SERVER_ERROR | 500 | Unexpected server error |

### Test Environment Setup

1. Start Spring Boot application: `mvn spring-boot:run`
2. Verify H2 database is running: http://localhost:8080/h2-console
3. Verify Swagger UI is accessible: http://localhost:8080/swagger-ui.html
4. Ensure sample products are initialized
5. Verify authentication is working

### Tools Required

- Postman or Newman for API testing
- JMeter for performance testing
- Git for version control
- Maven for build management

---

**Document Version:** 1.0  
**Last Updated:** 2024-01-15  
**Author:** QA Automation Team  
**Status:** Ready for Execution
