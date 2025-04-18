package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class PointDeVente {
    private int id;
    private String name;
    private LocalDateTime lastSync;
}
