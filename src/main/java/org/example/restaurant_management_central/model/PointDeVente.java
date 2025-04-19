package org.example.restaurant_management_central.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PointDeVente {
    private int id;
    private String name;
    private String url;
    private LocalDateTime lastSync;
}
