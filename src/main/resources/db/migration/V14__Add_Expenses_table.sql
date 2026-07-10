-- Add column notes to orders table
ALTER TABLE orders ADD COLUMN notes TEXT;

-- Add column dietary_restrictions to order_guests table
ALTER TABLE order_guests ADD COLUMN dietary_restrictions TEXT;

-- Create expenses table
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    submitted_by VARCHAR(255) NOT NULL,
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
    FOREIGN KEY (verified_by_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Add column check_number to receipts table
ALTER TABLE receipts ADD COLUMN check_number BOOLEAN NOT NULL DEFAULT FALSE;