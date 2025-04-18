package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class BestSales {
    private int dishId;
    private String dishName;
    private int quantitySold;
    private double totalAmount;
    private LocalDate calculationDate;
}