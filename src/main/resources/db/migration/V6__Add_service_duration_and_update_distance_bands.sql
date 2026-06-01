-- =====================================================
-- V6: Add Service Duration and Update Distance Bands
-- =====================================================

-- ===== ADD DURATION TO SERVICES =====

-- Add duration_minutes column to services table
ALTER TABLE services ADD COLUMN duration_minutes INT NOT NULL DEFAULT 0;

-- Update duration for existing services based on service type
UPDATE services SET duration_minutes = 60 WHERE service_type_id = 1;
UPDATE services SET duration_minutes = 90 WHERE service_type_id = 2;
UPDATE services SET duration_minutes = 120 WHERE service_type_id = 3;

-- ===== UPDATE DISTANCE BANDS =====

-- Update distance bands with new labels and fee amounts
-- First, clear existing distance bands
DELETE FROM distance_bands;

-- Reset auto-increment counter (for MySQL)
ALTER TABLE distance_bands AUTO_INCREMENT = 1;

-- Insert new distance bands
INSERT INTO distance_bands (label, sort_order, fee_amount) VALUES
('<5km', 1, 100.00),
('5km-10km', 2, 200.00),
('>10km-15km', 3, 300.00),
('>15km', 4, 400.00),
('15km-20km', 5, 500.00);
