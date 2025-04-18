package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class StockMouvement {
    private Long id;
    private Integer originalId;
    private PointDeVente pointDeVente;
    private MouvementType mouvementType;
    private Double quantity;
    private Ingredient ingredient;
}