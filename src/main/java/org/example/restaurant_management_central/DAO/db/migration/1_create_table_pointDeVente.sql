create table point_of_sale(
    id serial primary key ,
    name varchar(100) not null,
    last_sync timestamp
)