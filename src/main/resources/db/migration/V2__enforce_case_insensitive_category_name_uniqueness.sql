

-- Enforce case-insensitive uniqueness at the database level.
CREATE UNIQUE INDEX uk_categories_name_lower
    ON categories (LOWER(name));