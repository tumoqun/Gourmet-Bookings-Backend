-- =====================================================
-- V7: Restructure Work-Order Relationships
-- =====================================================

-- 1. Add is_private to orders
ALTER TABLE orders ADD COLUMN is_private BOOLEAN NOT NULL DEFAULT FALSE;

-- 2. Update orders with is_private from order_services
-- If any service in the order was private, mark the order as private
UPDATE orders 
SET is_private = TRUE 
WHERE id IN (
    SELECT order_id FROM order_services WHERE is_private = TRUE
);

-- 3. Create work_orders table
CREATE TABLE work_orders (
    work_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    PRIMARY KEY (work_id, order_id),
    FOREIGN KEY (work_id) REFERENCES works(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- 4. Copy existing relationships to work_orders
INSERT INTO work_orders (work_id, order_id)
SELECT id, order_id FROM works;

-- 5. Drop old columns
ALTER TABLE order_services DROP COLUMN is_private;
ALTER TABLE works DROP COLUMN order_id;

ALTER TABLE receipts ADD COLUMN fee DECIMAL(12, 2);
ALTER TABLE receipts ADD COLUMN tax DECIMAL(12, 2);

-- 6. Add field avatar, role, is_calendar_invitation to guides
ALTER TABLE guides ADD COLUMN avatar VARCHAR(255);
ALTER TABLE assignments ADD COLUMN is_calendar_invitation BOOLEAN DEFAULT FALSE;
ALTER TABLE assignments ADD COLUMN role VARCHAR(50);
ALTER TABLE assignments ADD COLUMN note VARCHAR(255);

-- 7. Add field status, added_by to itinerary_stops
ALTER TABLE itinerary_stops ADD COLUMN status VARCHAR(50);
ALTER TABLE itinerary_stops ADD COLUMN added_by VARCHAR(50);

-- 8. Add field submitted_by to receipts
ALTER TABLE receipts ADD COLUMN submitted_by VARCHAR(100);