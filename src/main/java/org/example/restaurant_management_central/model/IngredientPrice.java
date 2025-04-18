package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class IngredientPrice {
    private Long id;
    private Integer original_id;
    private PointDeVente pointDeVente;
    private Ingredient ingredient;
    private double price;
}