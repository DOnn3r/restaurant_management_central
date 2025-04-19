package org.example.restaurant_management_central.Service;

import lombok.SneakyThrows;
import org.example.restaurant_management_central.DAO.repository.PointDeVenteDAO;
import org.example.restaurant_management_central.DAO.repository.ProcessingTimeDAO;
import org.example.restaurant_management_central.DTO.ProcessingTimeDTO;
import org.example.restaurant_management_central.model.BestSales;
import org.example.restaurant_management_central.model.PointDeVente;
import org.example.restaurant_management_central.model.ProcessingTimeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ProcessingTimeService {
    private final ProcessingTimeDAO processingTimeDAO;
    private final PointDeVenteDAO pointDeVenteDAO;
    private final RestTemplate restTemplate;

    public ProcessingTimeService(ProcessingTimeDAO processingTimeDAO, PointDeVenteDAO pointDeVenteDAO, RestTemplate restTemplate) {
        this.processingTimeDAO = processingTimeDAO;
        this.pointDeVenteDAO = pointDeVenteDAO;
        this.restTemplate = restTemplate;
    }
    public void syncProcessingTimeFromAllPoints() throws SQLException {
        List<PointDeVente> pointsDeVente = pointDeVenteDAO.getAllPointDeVente();

        for (PointDeVente pdv : pointsDeVente) {
            String apiUrl = pdv.getUrl() + "/dishes/processingTimes";

            ProcessingTimeDTO[] processingTimesArray = restTemplate.getForObject(apiUrl, ProcessingTimeDTO[].class);
            List<ProcessingTimeDTO> processingTimesFromPdv = Arrays.asList(processingTimesArray);

            for (ProcessingTimeDTO dto : processingTimesFromPdv) {
                ProcessingTimeResponse processingTime = new ProcessingTimeResponse(
                        dto.getDishId(),
                        dto.getDuration(),
                        dto.getTimeUnit(),
                        dto.getCalculationType(),
                        LocalDateTime.now()
                );
                processingTimeDAO.saveProcessingTime(processingTime);
            }
        }
    }

    public List<ProcessingTimeResponse> getAggregatedProcessingTimes(int limit) throws SQLException {
        return processingTimeDAO.getLatestProcessingTimes(limit);
    }

    @SneakyThrows
    public void saveCalculatedProcessingTimes(List<ProcessingTimeResponse> processingTimes) {
        for (ProcessingTimeResponse bs : processingTimes) {
            processingTimeDAO.saveProcessingTime(bs);
        }
    }
}