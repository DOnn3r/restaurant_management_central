package org.example.restaurant_management_central.Controller;

import org.example.restaurant_management_central.model.Dish;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BestSalesController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/bestSales")
    public List<Dish> getBestSales(){
        throw new Error("Not implemented");
    }
}
