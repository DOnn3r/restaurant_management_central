package org.example.restaurant_management_central.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregateBestSalesDTO {
    private String dishName;
    private int quantitySold;
    private double totalAmount;
}