-- Gourmet Bookings Database Schema
-- MySQL 8+ with InnoDB, utf8mb4, snake_case naming

-- Management and Catalog Tables

CREATE TABLE roles (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_roles_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE users (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT UNSIGNED NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT,
    INDEX idx_users_email (email),
    INDEX idx_users_role_id (role_id),
    INDEX idx_users_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE resellers (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_resellers_status (status),
    INDEX idx_resellers_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reseller_contacts (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reseller_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE CASCADE,
    INDEX idx_reseller_contacts_reseller_id (reseller_id),
    INDEX idx_reseller_contacts_email (email),
    INDEX idx_reseller_contacts_is_primary (is_primary)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE agents (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reseller_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE CASCADE,
    INDEX idx_agents_reseller_id (reseller_id),
    INDEX idx_agents_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE guides (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_guides_email (email),
    INDEX idx_guides_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE areas (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_areas_code (code),
    INDEX idx_areas_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE service_types (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_service_types_code (code),
    INDEX idx_service_types_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE services (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    area_id BIGINT UNSIGNED NOT NULL,
    service_type_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(200) NOT NULL,
    is_private_available BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE RESTRICT,
    FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE RESTRICT,
    INDEX idx_services_area_id (area_id),
    INDEX idx_services_service_type_id (service_type_id),
    INDEX idx_services_is_active (is_active),
    INDEX idx_services_is_private_available (is_private_available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE distance_bands (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    sort_order INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_distance_bands_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE special_request_types (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_special_request_types_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Order Tables

CREATE TABLE order_statuses (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_statuses_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE orders (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    status_id BIGINT UNSIGNED NOT NULL,
    order_channel VARCHAR(50),
    is_tentative BOOLEAN NOT NULL DEFAULT FALSE,
    created_by_user_id BIGINT UNSIGNED,
    created_by_name VARCHAR(200),
    reseller_id BIGINT UNSIGNED,
    pic_contact_id BIGINT UNSIGNED,
    pic_email VARCHAR(150),
    copy_email VARCHAR(500),
    original_agent_id BIGINT UNSIGNED,
    ref_1 VARCHAR(100),
    ref_2 VARCHAR(100),
    voucher_number VARCHAR(100),
    guest_email VARCHAR(150),
    adult_count INT,
    child_count INT,
    dietary_restrictions VARCHAR(500),
    currency_code VARCHAR(3),
    total_fee_amount DECIMAL(10,2),
    requested_at TIMESTAMP NULL,
    submitted_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (status_id) REFERENCES order_statuses(id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (reseller_id) REFERENCES resellers(id) ON DELETE RESTRICT,
    FOREIGN KEY (pic_contact_id) REFERENCES reseller_contacts(id) ON DELETE SET NULL,
    FOREIGN KEY (original_agent_id) REFERENCES agents(id) ON DELETE SET NULL,
    INDEX idx_orders_order_number (order_number),
    INDEX idx_orders_status_id_requested_at (status_id, requested_at),
    INDEX idx_orders_reseller_id_requested_at (reseller_id, requested_at),
    INDEX idx_orders_pic_contact_id (pic_contact_id),
    INDEX idx_orders_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_services (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    service_id BIGINT UNSIGNED NOT NULL,
    service_name_snapshot VARCHAR(200) NOT NULL,
    area_id BIGINT UNSIGNED NOT NULL,
    service_type_id BIGINT UNSIGNED NOT NULL,
    target_date DATE,
    start_time TIME,
    time_slot_code VARCHAR(50),
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    timezone VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE RESTRICT,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE RESTRICT,
    FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE RESTRICT,
    INDEX idx_order_services_target_date_area_id_service_id (target_date, area_id, service_id),
    INDEX idx_order_services_order_id (order_id),
    INDEX idx_order_services_service_id (service_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_additional_services (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    kind VARCHAR(20) NOT NULL COMMENT 'pickup, dropoff, handoff',
    is_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    location VARCHAR(200),
    service_type_id BIGINT UNSIGNED,
    distance_band_id BIGINT UNSIGNED,
    suggested_time TIME,
    fee_amount DECIMAL(10,2),
    currency_code VARCHAR(3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE SET NULL,
    FOREIGN KEY (distance_band_id) REFERENCES distance_bands(id) ON DELETE SET NULL,
    INDEX idx_order_additional_services_order_id (order_id),
    INDEX idx_order_additional_services_kind (kind)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_special_requests (
    order_id BIGINT UNSIGNED NOT NULL,
    special_request_type_id BIGINT UNSIGNED NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id, special_request_type_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (special_request_type_id) REFERENCES special_request_types(id) ON DELETE CASCADE,
    INDEX idx_order_special_requests_special_request_type_id (special_request_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_status_history (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    from_status_id BIGINT UNSIGNED,
    to_status_id BIGINT UNSIGNED NOT NULL,
    changed_by_user_id BIGINT UNSIGNED,
    note VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (from_status_id) REFERENCES order_statuses(id) ON DELETE SET NULL,
    FOREIGN KEY (to_status_id) REFERENCES order_statuses(id) ON DELETE RESTRICT,
    FOREIGN KEY (changed_by_user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_order_status_history_order_id (order_id),
    INDEX idx_order_status_history_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Operations Tables

CREATE TABLE allotments (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    service_id BIGINT UNSIGNED NOT NULL,
    service_date DATE NOT NULL,
    start_time TIME NOT NULL,
    capacity_total INT NOT NULL,
    reserved_total INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    INDEX idx_allotments_service_id_service_date_start_time (service_id, service_date, start_time),
    INDEX idx_allotments_service_date (service_date),
    INDEX idx_allotments_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE allotment_reservations (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    allotment_id BIGINT UNSIGNED NOT NULL,
    order_service_id BIGINT UNSIGNED NOT NULL,
    adult_count INT,
    child_count INT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (allotment_id) REFERENCES allotments(id) ON DELETE CASCADE,
    FOREIGN KEY (order_service_id) REFERENCES order_services(id) ON DELETE CASCADE,
    INDEX idx_allotment_reservations_allotment_id (allotment_id),
    INDEX idx_allotment_reservations_order_service_id (order_service_id),
    INDEX idx_allotment_reservations_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE assignments (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_service_id BIGINT UNSIGNED NOT NULL,
    guide_id BIGINT UNSIGNED NOT NULL,
    status VARCHAR(50) NOT NULL,
    assigned_at TIMESTAMP NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_service_id) REFERENCES order_services(id) ON DELETE CASCADE,
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE RESTRICT,
    INDEX idx_assignments_guide_id_assigned_at (guide_id, assigned_at),
    INDEX idx_assignments_order_service_id (order_service_id),
    INDEX idx_assignments_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_financial_lines (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    line_type VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    amount DECIMAL(10,2),
    tax_amount DECIMAL(10,2),
    currency_code VARCHAR(3),
    is_tax_included BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_financial_lines_order_id_line_type (order_id, line_type),
    INDEX idx_order_financial_lines_line_type (line_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payments (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    amount DECIMAL(10,2),
    currency_code VARCHAR(3),
    method VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_payments_order_id (order_id),
    INDEX idx_payments_status (status),
    INDEX idx_payments_paid_at (paid_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE audit_logs (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    actor_user_id BIGINT UNSIGNED,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT UNSIGNED,
    action VARCHAR(50) NOT NULL,
    metadata_json JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (actor_user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_audit_logs_entity_type_entity_id (entity_type, entity_id),
    INDEX idx_audit_logs_actor_user_id (actor_user_id),
    INDEX idx_audit_logs_action (action),
    INDEX idx_audit_logs_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Initial Data

INSERT INTO order_statuses (code, label) VALUES
('requested', 'Requested'),
('pending_offer', 'Pending Offer'),
('active', 'Active'),
('completed', 'Completed'),
('cancelled', 'Cancelled');

INSERT INTO roles (code, name) VALUES
('admin', 'Administrator'),
('manager', 'Manager'),
('agent', 'Agent'),
('guide', 'Guide');

INSERT INTO service_types (code, name) VALUES
('tour', 'Tour'),
('transfer', 'Transfer'),
('activity', 'Activity'),
('dining', 'Dining');
