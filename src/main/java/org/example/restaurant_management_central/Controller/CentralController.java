package org.example.restaurant_management_central.Controller;

import org.example.restaurant_management_central.DTO.BestSalesDTO;
import org.example.restaurant_management_central.Service.BestSalesService;
import org.example.restaurant_management_central.Service.DishService;
import org.example.restaurant_management_central.model.BestSales;
import org.example.restaurant_management_central.model.CalculationType;
import org.example.restaurant_management_central.model.ProcessingTimeResponse;
import org.example.restaurant_management_central.model.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CentralController {
    private final BestSalesService bestSalesService;
    private final DishService dishService;

    @Autowired
    public CentralController(BestSalesService bestSalesService, DishService dishService) {
        this.bestSalesService = bestSalesService;
        this.dishService = dishService;
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @PostMapping("/synchronisation")
    public ResponseEntity<String> syncBestSales() throws SQLException {
        bestSalesService.syncBestSalesFromAllPoints();
        return ResponseEntity.ok("Synchronisation des bestSales terminée !");
    }

    @GetMapping("/bestSales")
    public List<BestSales> getBestSales(@RequestParam(defaultValue = "5") int limit) {
        return bestSalesService.getTopBestSales(limit);
    }

    @PostMapping("/bestSales")
    public String saveBestSales(@RequestBody List<BestSalesDTO> bestSalesDTOs) {
        List<BestSales> bestSales = bestSalesDTOs.stream()
                .map(dto -> new BestSales(
                        dto.getDishId().intValue(),
                        dto.getDishName(),
                        dto.getQuantitySold(),
                        dto.getTotalAmount(),
                        LocalDate.now()
                ))
                .collect(Collectors.toList());

        bestSalesService.saveCalculatedBestSales(bestSales);
        return "BestSales sauvegardés !";
    }

    @GetMapping("/dishes/{id}/bestProcessingTime")
    public ResponseEntity<ProcessingTimeResponse> getBestProcessingTime(
            @PathVariable Long id,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "SECONDS") TimeUnit unit,
            @RequestParam(required = false, defaultValue = "AVERAGE") CalculationType type
    ) {
        ProcessingTimeResponse response = dishService.calculateProcessingTime(
                id, startDate, endDate, unit, type
        );
        return ResponseEntity.ok(response);
    }
}