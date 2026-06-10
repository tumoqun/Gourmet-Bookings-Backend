INSERT INTO order_statuses (code, label)
SELECT 'OFFERED', 'Offered'
WHERE NOT EXISTS (SELECT 1 FROM order_statuses WHERE code = 'OFFERED');
