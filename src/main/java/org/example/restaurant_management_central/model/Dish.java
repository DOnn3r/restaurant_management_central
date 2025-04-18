package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Dish {
    private Long id;
    private Integer originalId;
    private String nom;
    private PointDeVente pointDeVente;
}
