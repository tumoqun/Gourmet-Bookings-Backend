-- =====================================================
-- V9: Authentication & RBAC test accounts
-- Dev password for all accounts: Gourmet123!
-- =====================================================

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255),
    ADD COLUMN IF NOT EXISTS guide_id BIGINT,
    ADD COLUMN IF NOT EXISTS last_login_at TIMESTAMP;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_users_guide_id'
    ) THEN
        ALTER TABLE users
            ADD CONSTRAINT fk_users_guide_id
                FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE SET NULL;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_users_guide_id ON users (guide_id);

-- BCrypt hash for Gourmet123! (BCryptPasswordEncoder, cost 10)

UPDATE users
SET password_hash = '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby'
WHERE email = 'admin@gourmetbookings.com';

-- Deprecate legacy Manager mock user
UPDATE users
SET is_active = FALSE,
    password_hash = '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby'
WHERE email = 'salmon@gourmetbookings.com';

INSERT INTO users (role_id, full_name, email, password_hash, is_active, guide_id)
SELECT 1, 'Yuki Tanaka', 'ops.admin@gourmetbookings.com', '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby', TRUE, NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'ops.admin@gourmetbookings.com');

INSERT INTO users (role_id, full_name, email, password_hash, is_active, guide_id)
SELECT 3, 'Maria Chen', 'agent@gourmetbookings.com', '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby', TRUE, NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'agent@gourmetbookings.com');

INSERT INTO users (role_id, full_name, email, password_hash, is_active, guide_id)
SELECT 3, 'Ken Watanabe', 'agent2@gourmetbookings.com', '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby', TRUE, NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'agent2@gourmetbookings.com');

INSERT INTO users (role_id, full_name, email, password_hash, is_active, guide_id)
SELECT 4, 'Sophia Taylor', 'sophia.taylor@guides.com', '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'sophia.taylor@guides.com');

INSERT INTO users (role_id, full_name, email, password_hash, is_active, guide_id)
SELECT 4, 'Emily Johnson', 'emily.j@guides.com', '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby', TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'emily.j@guides.com');

UPDATE users
SET password_hash = '$2a$10$RmsBrkUkrnKc0HGfG0AQ4uW1Jgf1B8FQtgRLNivnc3Q3k4pO9ciby'
WHERE password_hash IS NULL;

ALTER TABLE users
    ALTER COLUMN password_hash SET NOT NULL;

-- Pending assignment for Emily (guide_id=2) on first available work
INSERT INTO assignments (work_id, guide_id, status)
SELECT w.id, 2, 'pending'
FROM works w
WHERE w.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM assignments a
      WHERE a.guide_id = 2 AND a.status = 'pending' AND a.deleted_at IS NULL
  )
ORDER BY w.id
LIMIT 1;
