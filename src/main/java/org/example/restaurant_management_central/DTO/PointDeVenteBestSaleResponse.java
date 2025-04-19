package org.example.restaurant_management_central.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointDeVenteBestSaleResponse {
    private DishSale dish;
    private int quantitySold;
    private double totalPrice;
}
