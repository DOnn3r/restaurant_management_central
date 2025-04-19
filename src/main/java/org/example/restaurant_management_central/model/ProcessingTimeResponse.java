package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ProcessingTimeResponse {
    private Long dishId;
    private double duration;
    private TimeUnit unit;
    private CalculationType type;
    private LocalDateTime lastUpdated;
}
