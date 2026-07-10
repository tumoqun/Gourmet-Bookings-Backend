-- Create salary_scales table and seed default scales
CREATE TABLE salary_scales (
  scale_key VARCHAR(100) NOT NULL PRIMARY KEY,
  hourly_salary INTEGER NOT NULL
);

INSERT INTO salary_scales (scale_key, hourly_salary) VALUES ('tour_admin', 1000);
INSERT INTO salary_scales (scale_key, hourly_salary) VALUES ('tour_guide_junior', 700);
INSERT INTO salary_scales (scale_key, hourly_salary) VALUES ('tour_guide_senior', 900);

-- Add column to users and FK constraint
ALTER TABLE users ADD COLUMN salary_scale_key VARCHAR(100);
ALTER TABLE users ADD CONSTRAINT fk_users_salary_scale_key FOREIGN KEY (salary_scale_key) REFERENCES salary_scales(scale_key);
