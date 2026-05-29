-- =====================================================
-- V3: Admin Override Flag + Mock Data for Orders Page
-- =====================================================

-- ===== SCHEMA CHANGE =====

ALTER TABLE order_services
    ADD COLUMN is_admin_modified BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN original_service_id BIGINT,
    ADD COLUMN original_service_name_snapshot VARCHAR(200),
    ADD FOREIGN KEY (original_service_id) REFERENCES services(id) ON DELETE SET NULL;

CREATE INDEX idx_order_services_is_admin_modified ON order_services (is_admin_modified);

CREATE TRIGGER update_order_services_is_admin_modified
    BEFORE UPDATE ON order_services
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- MOCK DATA
-- Matches the 5 rows visible in the Orders page design
-- =====================================================

-- ===== 1. USERS =====

INSERT INTO users (role_id, full_name, email, is_active) VALUES
    (1, 'Alexander Pierce', 'admin@gourmetbookings.com', TRUE),   -- Admin (logged in user)
    (2, 'Salmon', 'salmon@gourmetbookings.com', TRUE);            -- Manager (top right)


-- ===== 2. RESELLERS =====

INSERT INTO resellers (name, status) VALUES
    ('EXC',           'active'),   -- id=1
    ('Deano Travel',  'active'),   -- id=2
    ('Deano',         'active'),   -- id=3
    ('Deano Japan',   'active');   -- id=4
-- Note: "Direct Customer" orders have no reseller (reseller_id = NULL)


-- ===== 3. RESELLER CONTACTS (PIC) =====

INSERT INTO reseller_contacts (reseller_id, name, email, is_primary) VALUES
    (1, 'James Anderson', 'james.anderson@exc.com',         TRUE),   -- id=1
    (2, 'Emily Johnson',  'emily.johnson@deanotravl.com',  TRUE),   -- id=2
    (3, 'Michael Brown',  'michael.brown@deano.com',       TRUE),   -- id=3
    (4, 'Elena Dyan',     'elena.dyan@deanojapan.com',     TRUE);   -- id=4


-- ===== 4. AGENTS =====

INSERT INTO agents (reseller_id, name, email) VALUES
    (1, 'EXC Agent 1',         'agent1@exc.com'),
    (2, 'Deano Travel Agent',  'agent@deanotravl.com'),
    (3, 'Deano Agent',         'agent@deano.com'),
    (4, 'Deano Japan Agent',   'agent@deanojapan.com');


-- ===== 5. GUIDES =====

INSERT INTO guides (full_name, email, phone, is_active) VALUES
    ('Sophia Taylor',  'sophia.taylor@guides.com',  '+81-90-1111-0001', TRUE),  -- id=1
    ('Emily Johnson',  'emily.j@guides.com',        '+81-90-1111-0002', TRUE),  -- id=2
    ('Jackson Brown',  'jackson.brown@guides.com',  '+81-90-1111-0003', TRUE),  -- id=3
    ('Olivia Wilson',  'olivia.wilson@guides.com',  '+81-90-1111-0004', TRUE),  -- id=4
    ('David Wilson',   'david.wilson@guides.com',   '+81-90-1111-0005', TRUE);  -- id=5


-- ===== 6. SERVICES =====
-- Areas (TOKYO=1) and service_types (TOUR=1) already seeded in V1

INSERT INTO services (area_id, service_type_id, name, is_private_available, is_active) VALUES
    (1, 1, 'The Tokyos',        TRUE,  TRUE),   -- id=1
    (1, 1, 'Tokyo Undisclosed', FALSE, TRUE),   -- id=2  (admin-only service name)
    (1, 1, 'Shinsaiku',         TRUE,  TRUE);   -- id=3


-- ===== 7. ORDERS =====

INSERT INTO orders (
    order_number, status_id, order_channel, is_tentative,
    created_by_id, created_by_name,
    reseller_id, pic_contact_id, pic_email,
    original_agent_id,
    ref1, ref2,
    adult_count, child_count,
    currency_code, total_fee_amount,
    requested_at, submitted_at
) VALUES
(
    -- Row 1: EXC | James Anderson | Completed
    'ORD-2024-0001', 7, 'AGENT', FALSE,
    1, 'Alexander Pierce',
    1, 1, 'james.anderson@exc.com',
    1,
    'TK-DB213-1', 'ACC-JM23-2BC',
    5, 0,
    'JPY', 90480.00,
    '2024-12-07 09:00:00', '2024-12-07 10:00:00'
),
(
    -- Row 2: Deano Travel | Emily Johnson | Active (CONFIRMED) — admin modified service
    'ORD-2025-0001', 4, 'AGENT', FALSE,
    1, 'Alexander Pierce',
    2, 2, 'emily.johnson@deanotravl.com',
    2,
    'JPITAM-M-87', 'VOLLAMA-V87',
    3, 1,
    'JPY', 90036.00,
    '2025-01-04 09:00:00', '2025-01-04 10:30:00'
),
(
    -- Row 3: Deano | Michael Brown | Requested — admin modified service
    'ORD-2025-0002', 1, 'AGENT', FALSE,
    1, 'Alexander Pierce',
    3, 3, 'michael.brown@deano.com',
    3,
    'NOLIRI.322', 'KANANA',
    3, 0,
    'JPY', 5300.00,
    '2025-01-15 09:00:00', NULL
),
(
    -- Row 4: Deano Japan | Elena Dyan | Pending Offer — admin modified service
    'ORD-2025-0003', 3, 'AGENT', FALSE,
    1, 'Alexander Pierce',
    4, 4, 'elena.dyan@deanojapan.com',
    4,
    'JPTAM-JMX5000', 'ACYW-JMX5000',
    8, 0,
    'JPY', 80038.00,
    '2025-01-26 09:00:00', NULL
),
(
    -- Row 5: Direct Customer | Daniel Wilson | Cancelled
    'ORD-2025-0004', 8, 'DIRECT', FALSE,
    1, 'Alexander Pierce',
    NULL, NULL, NULL,
    NULL,
    'EPT-#1', 'EPT-#2',
    4, 0,
    'JPY', 164722.00,
    '2025-05-19 09:00:00', NULL
);


-- ===== 8. ORDER SERVICES =====

INSERT INTO order_services (
    order_id, service_id, service_name_snapshot,
    area_id, service_type_id,
    target_date, start_time, time_slot_code,
    is_private,
    is_admin_modified, original_service_id, original_service_name_snapshot
) VALUES
(
    -- Row 1: The Tokyos | Private | NOT admin modified
    1, 1, 'The Tokyos',
    1, 1,
    '2024-12-02', '17:30:00', 'AFTERNOON',
    TRUE,
    FALSE, NULL, NULL
),
(
    -- Row 2: Tokyo Undisclosed | Shared | ADMIN MODIFIED (highlighted in UI)
    -- Admin changed from "The Tokyos" to "Tokyo Undisclosed"
    2, 2, 'Tokyo Undisclosed',
    1, 1,
    '2025-01-04', '17:00:00', 'AFTERNOON',
    FALSE,
    TRUE, 1, 'The Tokyos'
),
(
    -- Row 3: Shinsaiku | Private | ADMIN MODIFIED (highlighted in UI)
    -- Admin changed from "The Tokyos" to "Shinsaiku"
    3, 3, 'Shinsaiku',
    1, 1,
    '2025-01-15', '15:30:00', 'AFTERNOON',
    TRUE,
    TRUE, 1, 'The Tokyos'
),
(
    -- Row 4: Tokyo Undisclosed | Shared | ADMIN MODIFIED (highlighted in UI)
    4, 2, 'Tokyo Undisclosed',
    1, 1,
    '2025-01-26', '17:00:00', 'AFTERNOON',
    FALSE,
    TRUE, 1, 'The Tokyos'
),
(
    -- Row 5: The Tokyos | Shared | NOT admin modified
    5, 1, 'The Tokyos',
    1, 1,
    '2025-05-19', '18:20:00', 'EVENING',
    FALSE,
    FALSE, NULL, NULL
);


-- ===== 9. ORDER ADDITIONAL SERVICES (PU/DO) =====

INSERT INTO order_additional_services (
    order_id, kind, is_enabled, location, service_type_id, suggested_time, fee_amount, currency_code
) VALUES
-- Row 2: Dropoff at OC (5:00PM/OC)
(2, 'DROPOFF', TRUE, 'OC', 4, '17:00:00', 0.00, 'JPY'),
-- Row 3: Pickup at 4PM
(3, 'PICKUP', TRUE, NULL, 4, '16:00:00', 0.00, 'JPY');


-- ===== 10. ORDER SPECIAL REQUESTS =====
-- special_request_types seeded in V1: VIP=1, BAG=2, EYE=3, FOR=4, LINK=5, WHEEL=6, CHILD=7, DIET=8

INSERT INTO order_special_requests (order_id, special_request_type_id, notes) VALUES
-- Row 1: VIP + BAG icons visible
(1, 1, 'VIP guest, handle with care'),
(1, 2, 'Extra baggage handling required'),
-- Row 3: Triangle (WHEEL/accessibility icon)
(3, 6, 'Wheelchair access needed');


-- ===== 11. ORDER STATUS HISTORY =====

INSERT INTO order_status_history (order_id, status_id, changed_by_id, changed_by_name, notes) VALUES
(1, 1, 1, 'Alexander Pierce', 'Order requested'),
(1, 4, 1, 'Alexander Pierce', 'Order confirmed'),
(1, 7, 1, 'Alexander Pierce', 'Order completed'),
(2, 1, 1, 'Alexander Pierce', 'Order requested'),
(2, 4, 1, 'Alexander Pierce', 'Order confirmed - service changed by admin'),
(3, 1, 1, 'Alexander Pierce', 'Order requested'),
(4, 1, 1, 'Alexander Pierce', 'Order requested'),
(4, 3, 1, 'Alexander Pierce', 'Pending offer sent'),
(5, 1, 1, 'Alexander Pierce', 'Order requested'),
(5, 8, 1, 'Alexander Pierce', 'Order cancelled by customer');


-- ===== 12. WORKS (for completed order) =====

INSERT INTO works (
    order_id, work_number, status, tour_date,
    tour_start_time, tour_end_time,
    location_name, location_address
) VALUES
(1, 'WRK-2024-0001', 'completed', '2024-12-02',
 '17:30:00', '20:30:00',
 'The Tokyos Meeting Point', 'Shinjuku Station East Exit, Tokyo');


-- ===== 13. ASSIGNMENTS (Guide assignments) =====

INSERT INTO assignments (work_id, guide_id, status, accepted_at) VALUES
(1, 1, 'completed', '2024-11-28 10:00:00');  -- Sophia Taylor assigned to WRK-2024-0001


-- ===== 14. FINANCIAL LINES =====

INSERT INTO order_financial_lines (order_id, line_type, description, amount, tax_amount, currency_code, is_tax_included) VALUES
(1, 'SERVICE_FEE', 'The Tokyos - Private Tour (5 adults)', 82254.00, 8226.00, 'JPY', TRUE),
(2, 'SERVICE_FEE', 'Tokyo Undisclosed - Shared Tour (3 adults, 1 child)', 81851.00, 8185.00, 'JPY', TRUE),
(3, 'SERVICE_FEE', 'Shinsaiku - Private Tour (3 adults)', 4818.00,  482.00,  'JPY', TRUE),
(4, 'SERVICE_FEE', 'Tokyo Undisclosed - Shared Tour (8 adults)', 72762.00, 7276.00, 'JPY', TRUE),
(5, 'SERVICE_FEE', 'The Tokyos - Shared Tour (4 adults)', 149747.00, 14975.00, 'JPY', TRUE);
