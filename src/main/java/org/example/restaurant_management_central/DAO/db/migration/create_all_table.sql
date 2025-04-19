CREATE TABLE point_of_sale (
                               id SERIAL PRIMARY KEY DEFAULT ,
                               name VARCHAR(100) NOT NULL,
                               url VARCHAR(255) NOT NULL,
                               last_sync TIMESTAMP
);

CREATE TABLE dish (
                      id SERIAL PRIMARY KEY DEFAULT,
                      original_id INTEGER NOT NULL,
                      name VARCHAR(100) NOT NULL,
                      point_of_sale_id INTEGER NOT NULL REFERENCES point_of_sale(id),
                      CONSTRAINT unique_dish_per_pdv UNIQUE (original_id, point_of_sale_id)
);

CREATE TABLE best_sales (
                            id SERIAL PRIMARY KEY DEFAULT,
                            dish_id INTEGER NOT NULL REFERENCES dish(id),
                            quantity_sold INTEGER NOT NULL,
                            total_amount DECIMAL(10, 2) NOT NULL,
                            point_of_sale_id INTEGER NOT NULL REFERENCES point_of_sale(id),
                            calculation_date DATE NOT NULL,
                            CONSTRAINT unique_best_sales_entry UNIQUE (dish_id, point_of_sale_id, calculation_date)
);