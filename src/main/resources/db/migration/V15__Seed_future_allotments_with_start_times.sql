ALTER TABLE allotments
    ADD COLUMN IF NOT EXISTS start_time TIME NOT NULL DEFAULT '09:00:00',
    ADD COLUMN IF NOT EXISTS status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

INSERT INTO allotments (service_id, date, start_time, capacity, reserved_count, status)
SELECT
    s.id,
    d.dt,
    t.start_time,
    8,
    0,
    'ACTIVE'
FROM services s
CROSS JOIN (
    SELECT generate_series(
        DATE_TRUNC('year', CURRENT_DATE)::date,
        (DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' - INTERVAL '1 day')::date,
        INTERVAL '1 day'
    )::date AS dt
) AS d
CROSS JOIN (
    VALUES
        (TIME '09:00:00'),
        (TIME '13:00:00'),
        (TIME '17:30:00')
) AS t(start_time)
WHERE s.is_active = TRUE
  AND d.dt >= CURRENT_DATE
  AND NOT EXISTS (
      SELECT 1
      FROM allotments a
      WHERE a.service_id = s.id
        AND a.date = d.dt
        AND a.start_time = t.start_time
  );
