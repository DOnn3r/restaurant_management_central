package org.example.restaurant_management_central.DTO;

import lombok.Getter;
import lombok.Setter;
import org.example.restaurant_management_central.model.CalculationType;
import org.example.restaurant_management_central.model.TimeUnit;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProcessingTimeDTO {
    private Long dishId;
    private String dishName;
    private double duration;
    private TimeUnit timeUnit; // "SECONDS", "MINUTES", "HOURS"
    private CalculationType calculationType; // "AVERAGE", "MIN", "MAX"
    private LocalDateTime lastUpdated;
}
