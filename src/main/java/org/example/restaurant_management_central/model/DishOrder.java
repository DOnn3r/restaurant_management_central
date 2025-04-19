package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DishOrder {
    private Long id;
    private Long dishId; // Référence au Dish central
    private String orderReference; // Référence de la commande source
    private Status status; // Enum: IN_PROGRESS, FINISHED, etc.
    private LocalDateTime statusChangedAt; // Timestamp du statut
    private String pointDeVenteId;

    public DishOrder() {
    }
}
