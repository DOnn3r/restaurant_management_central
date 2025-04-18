CREATE TABLE point_of_sale (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                last_sync TIMESTAMP,
                                url varchar
);

-- Table ingredient (correspond à Ingredient.java)
create table ingredient (
                            id serial primary key ,
                            original_id int not null,
                            point_of_sale_id int not null,
                            name varchar(100) not null,
                            unity unity not null,
                            constraint fk_point_of_sale_id foreign key (point_of_sale_id) references point_of_sale(id),
                            constraint uq_ingredient_unique_per_pdv unique (original_id, point_of_sale_id)
);

-- Table dish (correspond à Dish.java)
CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      original_id INT NOT NULL,
                      name VARCHAR(100) NOT NULL,
                      point_of_sale_id INT NOT NULL REFERENCES point_of_sale(id),
                      CONSTRAINT uq_dish_unique_per_pdv UNIQUE (original_id, point_of_sale_id)
);

-- Table ingredient_price (correspond à IngredientPrice.java)
CREATE TABLE ingredient_price (
                                  id SERIAL PRIMARY KEY,
                                  original_id INTEGER NOT NULL,
                                  price DECIMAL(10,2) NOT NULL,
                                  ingredient_id INT NOT NULL REFERENCES ingredient(id),
                                  point_of_sale_id INT NOT NULL REFERENCES point_of_sale(id),
                                  CONSTRAINT uq_price_unique_per_pdv UNIQUE (original_id, point_of_sale_id)
);

-- Table stock_mouvement (correspond à StockMouvement.java)
CREATE TABLE stock_mouvement (
                                 id SERIAL PRIMARY KEY,
                                 original_id INTEGER NOT NULL,
                                 mouvement_type mouvement NOT NULL,
                                 quantity DECIMAL(10,3) NOT NULL,
                                 mouvement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 ingredient_id INT NOT NULL REFERENCES ingredient(id),
                                 point_of_sale_id INT NOT NULL REFERENCES point_of_sale(id),
                                 CONSTRAINT uq_mouvement_unique_per_pdv UNIQUE (original_id, point_of_sale_id)
);

-- Table dish_ingredient (pour la relation entre Dish et Ingredient)
CREATE TABLE dish_ingredient (
                                 dish_id INTEGER NOT NULL REFERENCES dish(id),
                                 ingredient_id INT NOT NULL REFERENCES ingredient(id),
                                 required_quantity DECIMAL(10,3) NOT NULL,
                                 PRIMARY KEY (dish_id, ingredient_id)
);