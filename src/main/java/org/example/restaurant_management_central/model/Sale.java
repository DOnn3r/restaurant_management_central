package org.example.restaurant_management_central.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.restaurant_management_central.DTO.DishSale;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sale {
    private DishSale dish;
    private int quantitySold;
    private double totalPrice;
}
