-- Gourmet Bookings Database Schema
-- PostgreSQL 15+ with UTF-8, snake_case naming

-- Management and Catalog Tables

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_roles_code ON roles (code);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role_id ON users (role_id);
CREATE INDEX idx_users_is_active ON users (is_active);

CREATE TABLE resellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_resellers_status ON resellers (status);
CREATE INDEX idx_resellers_name ON resellers (name);

CREATE TABLE reseller_contacts (
    id BIGSERIAL PRIMARY KEY,
    reseller_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE CASCADE
);

CREATE INDEX idx_reseller_contacts_reseller_id ON reseller_contacts (reseller_id);
CREATE INDEX idx_reseller_contacts_email ON reseller_contacts (email);
CREATE INDEX idx_reseller_contacts_is_primary ON reseller_contacts (is_primary);

CREATE TABLE agents (
    id BIGSERIAL PRIMARY KEY,
    reseller_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE CASCADE
);

CREATE INDEX idx_agents_reseller_id ON agents (reseller_id);
CREATE INDEX idx_agents_email ON agents (email);

CREATE TABLE guides (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_guides_email ON guides (email);
CREATE INDEX idx_guides_is_active ON guides (is_active);
CREATE INDEX idx_guides_deleted_at ON guides (deleted_at);

CREATE TABLE areas (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_areas_code ON areas (code);
CREATE INDEX idx_areas_name ON areas (name);

CREATE TABLE service_types (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_service_types_code ON service_types (code);
CREATE INDEX idx_service_types_name ON service_types (name);

CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    area_id BIGINT NOT NULL,
    service_type_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    is_private_available BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE RESTRICT,
    FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE RESTRICT
);

CREATE INDEX idx_services_area_id ON services (area_id);
CREATE INDEX idx_services_service_type_id ON services (service_type_id);
CREATE INDEX idx_services_is_active ON services (is_active);
CREATE INDEX idx_services_deleted_at ON services (deleted_at);

CREATE TABLE distance_bands (
    id BIGSERIAL PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_distance_bands_sort_order ON distance_bands (sort_order);

CREATE TABLE special_request_types (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_special_request_types_code ON special_request_types (code);

CREATE TABLE order_statuses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_statuses_code ON order_statuses (code);

-- Orders Tables

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    status_id BIGINT NOT NULL,
    order_channel VARCHAR(50),
    is_tentative BOOLEAN NOT NULL DEFAULT FALSE,
    created_by_id BIGINT,
    created_by_name VARCHAR(200),
    reseller_id BIGINT,
    pic_contact_id BIGINT,
    pic_email VARCHAR(150),
    copy_email VARCHAR(150),
    original_agent_id BIGINT,
    ref1 VARCHAR(100),
    ref2 VARCHAR(100),
    voucher_number VARCHAR(100),
    guest_email VARCHAR(150),
    adult_count INTEGER DEFAULT 0,
    child_count INTEGER DEFAULT 0,
    dietary_restrictions TEXT,
    currency_code VARCHAR(3) DEFAULT 'USD',
    total_fee_amount DECIMAL(12,2) DEFAULT 0.00,
    requested_at TIMESTAMP,
    submitted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES order_statuses(id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE SET NULL,
    FOREIGN KEY (pic_contact_id) REFERENCES reseller_contacts(id) ON DELETE SET NULL,
    FOREIGN KEY (original_agent_id) REFERENCES agents(id) ON DELETE SET NULL
);

CREATE INDEX idx_orders_order_number ON orders (order_number);
CREATE INDEX idx_orders_status_id ON orders (status_id);
CREATE INDEX idx_orders_reseller_id ON orders (reseller_id);
CREATE INDEX idx_orders_requested_at ON orders (requested_at);
CREATE INDEX idx_orders_submitted_at ON orders (submitted_at);
CREATE INDEX idx_orders_deleted_at ON orders (deleted_at);

CREATE TABLE order_services (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    service_name_snapshot VARCHAR(200) NOT NULL,
    area_id BIGINT NOT NULL,
    service_type_id BIGINT NOT NULL,
    target_date DATE,
    start_time TIME,
    time_slot_code VARCHAR(50),
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    timezone VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE RESTRICT,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE RESTRICT,
    FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE RESTRICT
);

CREATE INDEX idx_order_services_order_id ON order_services (order_id);
CREATE INDEX idx_order_services_service_id ON order_services (service_id);
CREATE INDEX idx_order_services_target_date ON order_services (target_date);
CREATE INDEX idx_order_services_deleted_at ON order_services (deleted_at);

CREATE TABLE order_additional_services (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    kind VARCHAR(50) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    location VARCHAR(200),
    service_type_id BIGINT,
    distance_band_id BIGINT,
    suggested_time TIME,
    fee_amount DECIMAL(12,2),
    currency_code VARCHAR(3) DEFAULT 'USD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE SET NULL,
    FOREIGN KEY (distance_band_id) REFERENCES distance_bands(id) ON DELETE SET NULL
);

CREATE INDEX idx_order_additional_services_order_id ON order_additional_services (order_id);
CREATE INDEX idx_order_additional_services_kind ON order_additional_services (kind);
CREATE INDEX idx_order_additional_services_deleted_at ON order_additional_services (deleted_at);

CREATE TABLE order_special_requests (
    order_id BIGINT NOT NULL,
    special_request_type_id BIGINT NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id, special_request_type_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (special_request_type_id) REFERENCES special_request_types(id) ON DELETE CASCADE
);

CREATE INDEX idx_order_special_requests_order_id ON order_special_requests (order_id);
CREATE INDEX idx_order_special_requests_special_request_type_id ON order_special_requests (special_request_type_id);

CREATE TABLE order_status_history (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    changed_by_id BIGINT,
    changed_by_name VARCHAR(200),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES order_statuses(id) ON DELETE RESTRICT,
    FOREIGN KEY (changed_by_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_order_status_history_order_id ON order_status_history (order_id);
CREATE INDEX idx_order_status_history_status_id ON order_status_history (status_id);
CREATE INDEX idx_order_status_history_created_at ON order_status_history (created_at);

-- Operations Tables

CREATE TABLE allotments (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    date DATE NOT NULL,
    capacity INTEGER NOT NULL,
    reserved_count INTEGER DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

CREATE INDEX idx_allotments_service_id ON allotments (service_id);
CREATE INDEX idx_allotments_date ON allotments (date);
CREATE INDEX idx_allotments_is_active ON allotments (is_active);
CREATE INDEX idx_allotments_deleted_at ON allotments (deleted_at);

CREATE TABLE allotment_reservations (
    id BIGSERIAL PRIMARY KEY,
    allotment_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    guest_count INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (allotment_id) REFERENCES allotments(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_allotment_reservations_allotment_id ON allotment_reservations (allotment_id);
CREATE INDEX idx_allotment_reservations_order_id ON allotment_reservations (order_id);

CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    order_service_id BIGINT NOT NULL,
    guide_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'assigned',
    assigned_at TIMESTAMP,
    confirmed_at TIMESTAMP,
    completed_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (order_service_id) REFERENCES order_services(id) ON DELETE CASCADE,
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE RESTRICT
);

CREATE INDEX idx_assignments_order_id ON assignments (order_id);
CREATE INDEX idx_assignments_order_service_id ON assignments (order_service_id);
CREATE INDEX idx_assignments_guide_id ON assignments (guide_id);
CREATE INDEX idx_assignments_status ON assignments (status);
CREATE INDEX idx_assignments_deleted_at ON assignments (deleted_at);

CREATE TABLE order_financial_lines (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    line_type VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    amount DECIMAL(12,2),
    tax_amount DECIMAL(12,2),
    currency_code VARCHAR(3) DEFAULT 'USD',
    is_tax_included BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_order_financial_lines_order_id ON order_financial_lines (order_id);
CREATE INDEX idx_order_financial_lines_line_type ON order_financial_lines (line_type);
CREATE INDEX idx_order_financial_lines_deleted_at ON order_financial_lines (deleted_at);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'USD',
    payment_date DATE NOT NULL,
    reference_number VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_payments_order_id ON payments (order_id);
CREATE INDEX idx_payments_payment_date ON payments (payment_date);
CREATE INDEX idx_payments_deleted_at ON payments (deleted_at);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_values JSONB,
    new_values JSONB,
    changed_by_id BIGINT,
    changed_by_name VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (changed_by_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_audit_logs_entity ON audit_logs (entity_type, entity_id);
CREATE INDEX idx_audit_logs_action ON audit_logs (action);
CREATE INDEX idx_audit_logs_changed_by_id ON audit_logs (changed_by_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs (created_at);

-- Initial Data

INSERT INTO roles (code, name) VALUES 
('ADMIN', 'Administrator'),
('MANAGER', 'Manager'),
('AGENT', 'Agent'),
('GUIDE', 'Guide');

INSERT INTO service_types (code, name) VALUES 
('TOUR', 'Tour'),
('DINING', 'Dining Experience'),
('ACTIVITY', 'Activity'),
('TRANSPORT', 'Transportation');

INSERT INTO areas (code, name) VALUES 
('TOKYO', 'Tokyo'),
('OSAKA', 'Osaka'),
('KYOTO', 'Kyoto'),
('NAGOYA', 'Nagoya'),
('SAPPORO', 'Sapporo'),
('FUKUOKA', 'Fukuoka');

INSERT INTO distance_bands (label, sort_order) VALUES 
('Within City', 1),
('Suburban', 2),
('Regional', 3),
('Long Distance', 4);

INSERT INTO special_request_types (code, label) VALUES 
('VIP', 'VIP Guest'),
('BAG', 'Baggage Handling'),
('EYE', 'Eye Contact'),
('FOR', 'Fork & Spoon'),
('LINK', 'Linked Orders'),
('WHEEL', 'Wheelchair Access'),
('CHILD', 'Child Care'),
('DIET', 'Dietary Requirements');

INSERT INTO order_statuses (code, label) VALUES 
('REQUESTED', 'Requested'),
('TENTATIVE', 'Tentative'),
('OFFERED', 'Offered'),
('CONFIRMED', 'Confirmed'),
('ASSIGNED', 'Assigned'),
('IN_PROGRESS', 'In Progress'),
('COMPLETED', 'Completed'),
('CANCELLED', 'Cancelled'),
('REFUNDED', 'Refunded');

-- Create function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at columns
CREATE TRIGGER update_roles_updated_at BEFORE UPDATE ON roles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_resellers_updated_at BEFORE UPDATE ON resellers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_reseller_contacts_updated_at BEFORE UPDATE ON reseller_contacts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_agents_updated_at BEFORE UPDATE ON agents
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_guides_updated_at BEFORE UPDATE ON guides
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_areas_updated_at BEFORE UPDATE ON areas
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_service_types_updated_at BEFORE UPDATE ON service_types
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_services_updated_at BEFORE UPDATE ON services
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_distance_bands_updated_at BEFORE UPDATE ON distance_bands
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_special_request_types_updated_at BEFORE UPDATE ON special_request_types
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_order_statuses_updated_at BEFORE UPDATE ON order_statuses
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_order_services_updated_at BEFORE UPDATE ON order_services
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_order_additional_services_updated_at BEFORE UPDATE ON order_additional_services
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_order_special_requests_updated_at BEFORE UPDATE ON order_special_requests
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_allotments_updated_at BEFORE UPDATE ON allotments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_allotment_reservations_updated_at BEFORE UPDATE ON allotment_reservations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_assignments_updated_at BEFORE UPDATE ON assignments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_order_financial_lines_updated_at BEFORE UPDATE ON order_financial_lines
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payments_updated_at BEFORE UPDATE ON payments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
