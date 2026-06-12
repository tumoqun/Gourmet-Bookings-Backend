-- =====================================================
-- V10: Add vehicle_type to order_additional_services
-- =====================================================

ALTER TABLE order_additional_services
    ADD COLUMN vehicle_type VARCHAR(50);
