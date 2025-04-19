package org.example.restaurant_management_central.Service;

import lombok.SneakyThrows;
import org.example.restaurant_management_central.DAO.repository.BestSalesDAO;
import org.example.restaurant_management_central.DAO.repository.PointDeVenteDAO;
import org.example.restaurant_management_central.DTO.BestSalesDTO;
import org.example.restaurant_management_central.model.BestSales;
import org.example.restaurant_management_central.model.PointDeVente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BestSalesService {
    private final BestSalesDAO bestSalesDAO;
    private final PointDeVenteDAO pointDeVenteDAO;
    private final RestTemplate restTemplate;

    @Autowired
    public BestSalesService(BestSalesDAO bestSalesDAO, PointDeVenteDAO pointDeVenteDAO, RestTemplate restTemplate) {
        this.bestSalesDAO = bestSalesDAO;
        this.pointDeVenteDAO = pointDeVenteDAO;
        this.restTemplate = restTemplate;
    }

    public void syncBestSalesFromAllPoints() throws SQLException {
        List<PointDeVente> pointsDeVente = pointDeVenteDAO.getAllPointDeVente();

        for (PointDeVente pdv : pointsDeVente) {
            String apiUrl = pdv.getUrl() + "/bestSales";

            BestSalesDTO[] bestSalesArray = restTemplate.getForObject(apiUrl, BestSalesDTO[].class);
            List<BestSalesDTO> bestSalesFromPdv = Arrays.asList(bestSalesArray);

            for (BestSalesDTO dto : bestSalesFromPdv) {
                BestSales bestSales = new BestSales(
                        dto.getDishId().intValue(),
                        dto.getDishName(),
                        dto.getQuantitySold(),
                        dto.getTotalAmount(),
                        LocalDate.now()
                );
                bestSalesDAO.saveBestSales(bestSales);
            }
        }
    }

    public List<BestSales> getTopBestSales(int limit) {
        List<BestSales> allSales = bestSalesDAO.getRealBestSales();
        return allSales.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public void saveCalculatedBestSales(List<BestSales> bestSales) {
        bestSales.forEach(bestSalesDAO::saveBestSales);
    }
}