# MiniWms
Design a simplified backend service for a warehouse inventory system

Below is a clear, ready set of diagrams for the Mini WMS (Warehouse Management System) backend.
These diagrams directly map to your requirements: inventory safety, concurrency, and idempotent retries.


+------------------+
|  Mobile Client   |
| (Android / iOS)  |
+--------+---------+
         |
         | REST API (retry-safe)
         v
+----------------------------+
|   Inventory Service       |
|  (Spring Boot Backend)    |
|                            |
|  - InventoryController    |
|  - ReservationService     |
|  - Idempotency Handler    |
+-------------+--------------+
              |
              | Transactions / Locks
              v
+----------------------------+
|        Database            |
|                            |
|  INVENTORY TABLE           |
|  - sku (PK)                |
|  - available_qty           |
|  - reserved_qty            |
|                            |
|  RESERVATION TABLE         |
|  - order_id (PK)           |
|  - sku                     |
|  - quantity                |
|  - status                  |
|                            |
|  IDEMPOTENCY TABLE         |
|  - request_id (PK)         |
|  - response                |
+----------------------------+

Why this works

✔ Prevents negative inventory
✔ Supports concurrent users
✔ Safe for mobile retries

⸻

2️⃣ Database Model (ER Diagram)

+------------------+        +----------------------+
|   INVENTORY      |        |    RESERVATION       |
+------------------+        +----------------------+
| sku (PK)         |<----+  | order_id (PK)        |
| available_qty    |     |  | sku (FK)             |
| reserved_qty     |     +--| quantity             |
+------------------+        | status               |
                            +----------------------+


⸻

3️⃣ Sequence Diagram – reserveItem(sku, qty, orderId)

Client
  |
  | reserveItem(sku, qty, orderId)
  v
InventoryController
  |
  | BEGIN TRANSACTION
  v
InventoryService
  |
  | SELECT inventory WHERE sku=? FOR UPDATE
  |
  | check available_qty >= qty
  |
  | UPDATE inventory
  |   available_qty -= qty
  |   reserved_qty += qty
  |
  | INSERT reservation(orderId, sku, qty, RESERVED)
  |
  | COMMIT
  v
Client ← Success Response

Concurrency Handling
    •   SELECT ... FOR UPDATE (row lock)
    •   Transaction ensures atomic update
    •   Prevents overselling

⸻

4️⃣ Sequence Diagram – shipOrder(orderId)

Client
  |
  | shipOrder(orderId)
  v
InventoryController
  |
  | BEGIN TRANSACTION
  v
ReservationService
  |
  | SELECT reservation WHERE orderId=?
  |
  | UPDATE reservation SET status=SHIPPED
  |
  | UPDATE inventory
  |   reserved_qty -= quantity
  |
  | COMMIT
  v
Client ← Order Shipped


⸻

5️⃣ Idempotency & Retry Handling (Important for Mobile)

Client (Retry)
  |
  | POST /reserveItem (requestId)
  v
Idempotency Check
  |
  | requestId exists?
  |---- YES → Return saved response
  |
  |---- NO → Process request
  |          Save response

Why this matters

✔ Mobile networks are unstable
✔ Prevents double reservation
✔ Ensures safe retries


