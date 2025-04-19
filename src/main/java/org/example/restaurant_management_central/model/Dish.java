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
    private String name;
    private PointDeVente pointDeVente;

    public Dish() {
        
    }

    public void setPointDeVenteId(String pointDeVenteId) {
    }
}
