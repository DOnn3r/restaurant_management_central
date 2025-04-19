package org.example.restaurant_management_central.Service;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.restaurant_management_central.DAO.repository.BestSalesDAO;
import org.example.restaurant_management_central.DAO.repository.PointDeVenteDAO;
import org.example.restaurant_management_central.DTO.BestSalesDTO;
import org.example.restaurant_management_central.model.BestSales;
import org.example.restaurant_management_central.model.PointDeVente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BestSalesService {
    private static final Logger log = LogManager.getLogger(BestSalesService.class);
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
        BestSalesService.log.info("Starting synchronization for {} points de vente", pointsDeVente.size());

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        for (PointDeVente pdv : pointsDeVente) {
            String apiUrl = pdv.getUrl() + "/bestSales?startDate=" + startDate + "&endDate=" + endDate;
            log.debug("Fetching best sales from: {}", apiUrl);

            BestSalesDTO[] bestSalesArray = restTemplate.getForObject(apiUrl, BestSalesDTO[].class);
            if (bestSalesArray == null) continue;

            List<BestSalesDTO> bestSalesFromPdv = Arrays.asList(bestSalesArray);
            log.info("Received {} best sales from {}", bestSalesFromPdv.size(), pdv.getName());

            for (BestSalesDTO dto : bestSalesFromPdv) {
                try {
                    BestSales bestSales = new BestSales(
                            dto.getDishId().intValue(),
                            dto.getDishName(),
                            dto.getQuantitySold(),
                            dto.getTotalAmount(),
                            LocalDate.now()
                    );
                    bestSalesDAO.saveBestSales(bestSales);
                    log.debug("Saved best sales for dish {}", dto.getDishId());
                } catch (SQLException e) {
                    log.error("Failed to save best sales for dish {}: {}", dto.getDishId(), e.getMessage());
                    throw e;
                }
            }
        }
    }

    public List<BestSales> getTopBestSales(int limit) {
        List<BestSales> allSales = bestSalesDAO.getRealBestSales();
        return allSales.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<BestSales> getAll(){
        return bestSalesDAO.getRealBestSales();
    }

    @SneakyThrows
    public void saveCalculatedBestSales(List<BestSales> bestSales) {
        for (BestSales bs : bestSales) {
            bestSalesDAO.saveBestSales(bs);
        }
    }
}