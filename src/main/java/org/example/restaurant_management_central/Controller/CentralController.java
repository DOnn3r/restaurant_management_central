package org.example.restaurant_management_central.Controller;

import org.example.restaurant_management_central.DTO.BestSalesDTO;
import org.example.restaurant_management_central.Service.BestSalesService;
import org.example.restaurant_management_central.model.BestSales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CentralController {
    private final BestSalesService bestSalesService;

    @Autowired
    public CentralController(BestSalesService bestSalesService) {
        this.bestSalesService = bestSalesService;
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
}