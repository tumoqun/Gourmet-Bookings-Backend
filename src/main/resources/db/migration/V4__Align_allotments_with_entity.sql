ALTER TABLE allotments
    ADD COLUMN IF NOT EXISTS start_time TIME NOT NULL DEFAULT '09:00:00',
    ADD COLUMN IF NOT EXISTS status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

INSERT INTO allotments (service_id, date, start_time, capacity, reserved_count, status)
SELECT service_id, date, start_time, capacity, reserved_count, status
FROM (
    VALUES
        (1::BIGINT, CURRENT_DATE, TIME '09:00:00', 8, 0, 'ACTIVE'),
        (1::BIGINT, CURRENT_DATE, TIME '13:00:00', 8, 2, 'ACTIVE'),
        (1::BIGINT, CURRENT_DATE, TIME '17:30:00', 8, 5, 'ACTIVE'),
        (2::BIGINT, CURRENT_DATE, TIME '10:00:00', 6, 1, 'ACTIVE'),
        (2::BIGINT, CURRENT_DATE, TIME '17:00:00', 6, 3, 'ACTIVE'),
        (2::BIGINT, CURRENT_DATE + INTERVAL '1 day', TIME '17:00:00', 6, 0, 'ACTIVE'),
        (3::BIGINT, CURRENT_DATE, TIME '11:30:00', 10, 4, 'ACTIVE'),
        (3::BIGINT, CURRENT_DATE, TIME '15:30:00', 10, 7, 'ACTIVE')
) AS seed(service_id, date, start_time, capacity, reserved_count, status)
WHERE EXISTS (
    SELECT 1
    FROM services s
    WHERE s.id = seed.service_id
)
AND NOT EXISTS (
    SELECT 1
    FROM allotments a
    WHERE a.service_id = seed.service_id
      AND a.date = seed.date
      AND a.start_time = seed.start_time
);
