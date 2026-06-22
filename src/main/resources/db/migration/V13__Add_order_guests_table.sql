CREATE TABLE order_guests (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    guest_type VARCHAR(50),
    is_vip BOOLEAN DEFAULT FALSE,
    age INTEGER,
    gender VARCHAR(50),
    nationality VARCHAR(100),
    phone_number VARCHAR(50),
    allergies VARCHAR(500),
    special_occasion VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_order_guests_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
