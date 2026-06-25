-- Add column notes to orders table
ALTER TABLE orders ADD COLUMN notes TEXT;

-- Add column dietary_restrictions to order_guests table
ALTER TABLE order_guests ADD COLUMN dietary_restrictions TEXT;

-- Create expenses table
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL,
    supplier_id BIGINT,
    itinerary_stop_id BIGINT,
    amount DECIMAL(12, 2) NOT NULL,
    expense_date DATE NOT NULL,
    expense_time TIME,
    notes TEXT,
    image_url VARCHAR(500),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verified_by_id BIGINT,
    verified_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    FOREIGN KEY (itinerary_stop_id) REFERENCES itinerary_stops(id) ON DELETE SET NULL,
    FOREIGN KEY (verified_by_id) REFERENCES users(id) ON DELETE SET NULL
);
