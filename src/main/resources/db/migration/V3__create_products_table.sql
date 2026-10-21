CREATE TABLE products (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                          sku VARCHAR(50) NOT NULL,
                          name VARCHAR(150) NOT NULL,
                          description VARCHAR(2000),

                          price NUMERIC(12, 2) NOT NULL,
                          currency VARCHAR(3) NOT NULL DEFAULT 'KES',

                          category_id BIGINT NOT NULL,

                          status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT uk_products_sku UNIQUE (sku),

                          CONSTRAINT fk_products_category
                              FOREIGN KEY (category_id)
                                  REFERENCES categories (id),

                          CONSTRAINT chk_products_price_non_negative
                              CHECK (price >= 0),

                          CONSTRAINT chk_products_status
                              CHECK (status IN ('DRAFT', 'ACTIVE', 'ARCHIVED')),

                          CONSTRAINT chk_products_currency
                              CHECK (currency ~ '^[A-Z]{3}$')
    );

CREATE INDEX idx_products_category_id
    ON products (category_id);

CREATE INDEX idx_products_status
    ON products (status);