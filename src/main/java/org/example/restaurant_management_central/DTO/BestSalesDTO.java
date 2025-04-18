package org.example.restaurant_management_central.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestSalesDTO {
    private Long dishId;
    private String dishName;
    private int quantitySold;
    private double totalAmount;
}
