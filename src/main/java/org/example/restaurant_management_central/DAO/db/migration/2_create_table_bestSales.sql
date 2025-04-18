create table bestSales(
    id serial primary key ,
    pointDeVenteId int not null,
    dishId int not null,
    quantity int not null,
    constraint fk_pointDeVente foreign key (pointDeVenteId) references pointDeVente(id) on delete cascade,
    constraint fk_dishId foreign key (dishId) references Dish(id) on delete cascade
);