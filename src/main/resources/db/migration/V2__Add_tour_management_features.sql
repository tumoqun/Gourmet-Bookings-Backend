-- Gourmet Bookings - Tour Management Features
-- Adds: Guest Groups, Guests, Order Groups, Works, Itineraries, Suppliers, Receipts, Salaries, Pricing

-- ===== CUSTOMER/GUEST MANAGEMENT =====

CREATE TABLE customer_groups (
    id BIGSERIAL PRIMARY KEY,
    reseller_id BIGINT,
    name VARCHAR(200) NOT NULL,
    contact_email VARCHAR(150),
    contact_phone VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE SET NULL
);

CREATE INDEX idx_customer_groups_reseller_id ON customer_groups (reseller_id);
CREATE INDEX idx_customer_groups_deleted_at ON customer_groups (deleted_at);

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    customer_group_id BIGINT NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(50),
    passport_number VARCHAR(100),
    date_of_birth DATE,
    nationality VARCHAR(100),
    is_primary_contact BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (customer_group_id) REFERENCES customer_groups(id) ON DELETE CASCADE
);

CREATE INDEX idx_customers_customer_group_id ON customers (customer_group_id);
CREATE INDEX idx_customers_email ON customers (email);
CREATE INDEX idx_customers_deleted_at ON customers (deleted_at);

-- ===== ORDER GROUP MANAGEMENT =====

CREATE TABLE order_groups (
    id BIGSERIAL PRIMARY KEY,
    customer_group_id BIGINT NOT NULL,
    reseller_id BIGINT,
    group_name VARCHAR(200),
    total_pax INTEGER DEFAULT 0,
    currency_code VARCHAR(3) DEFAULT 'USD',
    total_amount DECIMAL(12,2) DEFAULT 0.00,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (customer_group_id) REFERENCES customer_groups(id) ON DELETE CASCADE,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE SET NULL
);

CREATE INDEX idx_order_groups_customer_group_id ON order_groups (customer_group_id);
CREATE INDEX idx_order_groups_reseller_id ON order_groups (reseller_id);
CREATE INDEX idx_order_groups_deleted_at ON order_groups (deleted_at);

-- Add order_group_id to existing orders table
ALTER TABLE orders ADD COLUMN order_group_id BIGINT;
ALTER TABLE orders ADD COLUMN customer_group_id BIGINT;
ALTER TABLE orders ADD FOREIGN KEY (order_group_id) REFERENCES order_groups(id) ON DELETE SET NULL;
ALTER TABLE orders ADD FOREIGN KEY (customer_group_id) REFERENCES customer_groups(id) ON DELETE SET NULL;
CREATE INDEX idx_orders_order_group_id ON orders (order_group_id);
CREATE INDEX idx_orders_customer_group_id ON orders (customer_group_id);

-- ===== OPERATIONAL/WORK MANAGEMENT =====

CREATE TABLE works (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    work_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL DEFAULT 'scheduled',
    tour_date DATE NOT NULL,
    tour_start_time TIME,
    tour_end_time TIME,
    tour_started_at TIMESTAMP,
    tour_ended_at TIMESTAMP,
    closed_at TIMESTAMP,
    location_name VARCHAR(200),
    location_address TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_works_order_id ON works (order_id);
CREATE INDEX idx_works_status ON works (status);
CREATE INDEX idx_works_tour_date ON works (tour_date);
CREATE INDEX idx_works_deleted_at ON works (deleted_at);

-- ===== SUPPLIER/RESTAURANT MANAGEMENT =====

CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    supplier_type VARCHAR(50) NOT NULL,
    address TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    phone VARCHAR(50),
    email VARCHAR(150),
    website VARCHAR(200),
    cuisine_type VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_suppliers_name ON suppliers (name);
CREATE INDEX idx_suppliers_supplier_type ON suppliers (supplier_type);
CREATE INDEX idx_suppliers_is_active ON suppliers (is_active);
CREATE INDEX idx_suppliers_deleted_at ON suppliers (deleted_at);

-- ===== ITINERARY MANAGEMENT =====

CREATE TABLE itineraries (
    id BIGSERIAL PRIMARY KEY,
    work_id BIGINT NOT NULL,
    day_number INTEGER NOT NULL,
    day_title VARCHAR(200),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (work_id) REFERENCES works(id) ON DELETE CASCADE
);

CREATE INDEX idx_itineraries_work_id ON itineraries (work_id);
CREATE INDEX idx_itineraries_day_number ON itineraries (day_number);

CREATE TABLE itinerary_stops (
    id BIGSERIAL PRIMARY KEY,
    itinerary_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    stop_sequence INTEGER NOT NULL,
    stop_type VARCHAR(50) NOT NULL,
    scheduled_time TIME,
    estimated_duration_minutes INTEGER,
    description TEXT,
    special_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (itinerary_id) REFERENCES itineraries(id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE RESTRICT
);

CREATE INDEX idx_itinerary_stops_itinerary_id ON itinerary_stops (itinerary_id);
CREATE INDEX idx_itinerary_stops_supplier_id ON itinerary_stops (supplier_id);
CREATE INDEX idx_itinerary_stops_stop_sequence ON itinerary_stops (stop_sequence);

-- ===== GUIDE ASSIGNMENT & ACCOUNTING =====

-- Restructure assignments table to track per-guide accounting
DROP TABLE IF EXISTS assignments;

CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    work_id BIGINT NOT NULL,
    guide_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    accepted_at TIMESTAMP,
    rejected_at TIMESTAMP,
    rejection_reason TEXT,
    reminder_sent_at TIMESTAMP,
    tour_started_at TIMESTAMP,
    tour_ended_at TIMESTAMP,
    closed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (work_id) REFERENCES works(id) ON DELETE CASCADE,
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE RESTRICT
);

CREATE INDEX idx_assignments_work_id ON assignments (work_id);
CREATE INDEX idx_assignments_guide_id ON assignments (guide_id);
CREATE INDEX idx_assignments_status ON assignments (status);
CREATE INDEX idx_assignments_deleted_at ON assignments (deleted_at);

-- ===== RECEIPT/EXPENSE TRACKING =====

CREATE TABLE receipts (
    id BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL,
    supplier_id BIGINT,
    itinerary_stop_id BIGINT,
    receipt_type VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'USD',
    receipt_date DATE NOT NULL,
    receipt_time TIME,
    receipt_number VARCHAR(100),
    category VARCHAR(50),
    payment_method VARCHAR(50),
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

CREATE INDEX idx_receipts_assignment_id ON receipts (assignment_id);
CREATE INDEX idx_receipts_supplier_id ON receipts (supplier_id);
CREATE INDEX idx_receipts_receipt_date ON receipts (receipt_date);
CREATE INDEX idx_receipts_is_verified ON receipts (is_verified);
CREATE INDEX idx_receipts_deleted_at ON receipts (deleted_at);

-- ===== GUIDE AVAILABILITY =====

CREATE TABLE availabilities (
    id BIGSERIAL PRIMARY KEY,
    guide_id BIGINT NOT NULL,
    available_date DATE NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    max_tours_per_day INTEGER DEFAULT 1,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE
);

CREATE INDEX idx_availabilities_guide_id ON availabilities (guide_id);
CREATE INDEX idx_availabilities_available_date ON availabilities (available_date);
CREATE INDEX idx_availabilities_is_available ON availabilities (is_available);

-- ===== SALARY MANAGEMENT =====

CREATE TABLE salary_structures (
    id BIGSERIAL PRIMARY KEY,
    guide_id BIGINT NOT NULL,
    salary_type VARCHAR(50) NOT NULL,
    base_amount DECIMAL(12, 2),
    transaction_fee_percent DECIMAL(5, 2) DEFAULT 0.00,
    currency_code VARCHAR(3) DEFAULT 'USD',
    effective_from DATE NOT NULL,
    effective_until DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE
);

CREATE INDEX idx_salary_structures_guide_id ON salary_structures (guide_id);
CREATE INDEX idx_salary_structures_effective_from ON salary_structures (effective_from);
CREATE INDEX idx_salary_structures_deleted_at ON salary_structures (deleted_at);

CREATE TABLE salary_entries (
    id BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL,
    guide_id BIGINT NOT NULL,
    work_id BIGINT NOT NULL,
    salary_type VARCHAR(50) NOT NULL,
    base_amount DECIMAL(12, 2) NOT NULL,
    expense_reimbursement DECIMAL(12, 2) DEFAULT 0.00,
    deductions DECIMAL(12, 2) DEFAULT 0.00,
    transaction_fee DECIMAL(12, 2) DEFAULT 0.00,
    total_amount DECIMAL(12, 2) NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'USD',
    period_start_date DATE NOT NULL,
    period_end_date DATE NOT NULL,
    payment_date DATE,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50) DEFAULT 'pending',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE RESTRICT,
    FOREIGN KEY (work_id) REFERENCES works(id) ON DELETE RESTRICT
);

CREATE INDEX idx_salary_entries_assignment_id ON salary_entries (assignment_id);
CREATE INDEX idx_salary_entries_guide_id ON salary_entries (guide_id);
CREATE INDEX idx_salary_entries_work_id ON salary_entries (work_id);
CREATE INDEX idx_salary_entries_payment_status ON salary_entries (payment_status);
CREATE INDEX idx_salary_entries_period_start_date ON salary_entries (period_start_date);

-- ===== PRICING MANAGEMENT =====

CREATE TABLE price_books (
    id BIGSERIAL PRIMARY KEY,
    created_by_id BIGINT,
    price_book_name VARCHAR(200) NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'USD',
    effective_from DATE NOT NULL,
    effective_until DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_price_books_created_by_id ON price_books (created_by_id);
CREATE INDEX idx_price_books_effective_from ON price_books (effective_from);
CREATE INDEX idx_price_books_is_active ON price_books (is_active);
CREATE INDEX idx_price_books_deleted_at ON price_books (deleted_at);

CREATE TABLE price_book_reseller_links (
    id BIGSERIAL PRIMARY KEY,
    price_book_id BIGINT NOT NULL,
    reseller_id BIGINT NOT NULL,
    link_type VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (price_book_id) REFERENCES price_books(id) ON DELETE CASCADE,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE CASCADE
);

CREATE INDEX idx_price_book_reseller_links_price_book_id ON price_book_reseller_links (price_book_id);
CREATE INDEX idx_price_book_reseller_links_reseller_id ON price_book_reseller_links (reseller_id);
CREATE UNIQUE INDEX uidx_price_book_reseller ON price_book_reseller_links (price_book_id, reseller_id);

CREATE TABLE price_entries (
    id BIGSERIAL PRIMARY KEY,
    price_book_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    guest_type VARCHAR(50) NOT NULL,
    base_price DECIMAL(12, 2) NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'USD',
    min_pax INTEGER,
    max_pax INTEGER,
    seasonal_adjustment_percent DECIMAL(5, 2) DEFAULT 0.00,
    effective_from DATE NOT NULL,
    effective_until DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (price_book_id) REFERENCES price_books(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

CREATE INDEX idx_price_entries_price_book_id ON price_entries (price_book_id);
CREATE INDEX idx_price_entries_service_id ON price_entries (service_id);
CREATE INDEX idx_price_entries_guest_type ON price_entries (guest_type);
CREATE INDEX idx_price_entries_effective_from ON price_entries (effective_from);
CREATE INDEX idx_price_entries_deleted_at ON price_entries (deleted_at);

-- ===== UPDATE WORK STATUSES =====

INSERT INTO order_statuses (code, label) VALUES
('SCHEDULED', 'Scheduled'),
('IN_PREP', 'In Prep'),
('ACCEPTED', 'Accepted'),
('READY', 'Ready'),
('STARTED', 'Started'),
('ENDED', 'Ended'),
('CLOSED', 'Closed')
ON CONFLICT (code) DO NOTHING;

-- ===== CREATE TRIGGERS FOR NEW TABLES =====

CREATE TRIGGER update_customer_groups_updated_at BEFORE UPDATE ON customer_groups
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_order_groups_updated_at BEFORE UPDATE ON order_groups
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_works_updated_at BEFORE UPDATE ON works
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_suppliers_updated_at BEFORE UPDATE ON suppliers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_itineraries_updated_at BEFORE UPDATE ON itineraries
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_itinerary_stops_updated_at BEFORE UPDATE ON itinerary_stops
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_assignments_updated_at BEFORE UPDATE ON assignments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_receipts_updated_at BEFORE UPDATE ON receipts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_availabilities_updated_at BEFORE UPDATE ON availabilities
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_salary_structures_updated_at BEFORE UPDATE ON salary_structures
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_salary_entries_updated_at BEFORE UPDATE ON salary_entries
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_price_books_updated_at BEFORE UPDATE ON price_books
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_price_book_reseller_links_updated_at BEFORE UPDATE ON price_book_reseller_links
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_price_entries_updated_at BEFORE UPDATE ON price_entries
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();


