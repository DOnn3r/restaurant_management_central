package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Ingredient {
    private Long id;
    private Integer originalId;
    private PointDeVente pointDeVente;
    private String nom;
    private Unity unite;
}
