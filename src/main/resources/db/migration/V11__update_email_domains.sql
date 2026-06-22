-- =====================================================
-- V11: Update Email Domains to @tmatour.com
-- This migration updates email domains from @gourmetbookings.com to @tmatour.com
-- =====================================================

-- Update email domains for admin accounts
UPDATE users
SET email = REPLACE(email, '@gourmetbookings.com', '@tmatour.com')
WHERE email LIKE '%@gourmetbookings.com';

-- Update email domains for guide accounts
UPDATE users
SET email = REPLACE(email, '@guides.com', '@tmatour.com')
WHERE email LIKE '%@guides.com';

-- Note: Password hashes remain unchanged
-- Users will need to use their new email addresses to log in
