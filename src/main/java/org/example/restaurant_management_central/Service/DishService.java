package org.example.restaurant_management_central.Service;

import org.example.restaurant_management_central.DAO.repository.DishDAO;
import org.example.restaurant_management_central.DAO.repository.DishOrderDAO;
import org.example.restaurant_management_central.model.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class DishService {
    private final DishOrderDAO dishOrderDao;
    private final DishDAO dishDao;

    public DishService(DishDAO dishDao) {
        this.dishDao = dishDao;
        this.dishOrderDao = new DishOrderDAO();
    }

    public ProcessingTimeResponse calculateProcessingTime(
            Long dishId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TimeUnit unit,
            CalculationType type
    ) {
        // 1. Vérifier que le plat existe
        Dish dish = dishDao.findById(dishId);
        if (dish == null) {
            throw new IllegalArgumentException("Dish with id " + dishId + " not found");
        }

        // 2. Récupérer les DishOrder dans la plage de dates
        List<DishOrder> orders = dishOrderDao.findForProcessingTime(dishId, startDate, endDate);

        // 3. Calculer les durées de traitement
        List<Duration> processingDurations = calculateDurations(orders);

        // 4. Appliquer le type de calcul (MOYENNE, MIN, MAX)
        double result = applyCalculationType(processingDurations, type);

        // 5. Convertir dans l'unité demandée
        result = convertToTimeUnit(result, unit);

        return new ProcessingTimeResponse(
                dishId,
                result,
                unit,
                type,
                LocalDateTime.now()
        );
    }

    private List<Duration> calculateDurations(List<DishOrder> orders) {
        // Group orders by their order reference
        Map<String, List<DishOrder>> ordersByReference = orders.stream()
                .collect(Collectors.groupingBy(DishOrder::getOrderReference));

        List<Duration> durations = new ArrayList<>();

        for (List<DishOrder> orderGroup : ordersByReference.values()) {
            // Find IN_PROGRESS and FINISHED statuses
            Optional<DishOrder> inProgress = orderGroup.stream()
                    .filter(o -> o.getStatus() == Status.IN_PROGRESS)
                    .findFirst();

            Optional<DishOrder> finished = orderGroup.stream()
                    .filter(o -> o.getStatus() == Status.FINISHED)
                    .findFirst();

            if (inProgress.isPresent() && finished.isPresent()) {
                Duration duration = Duration.between(
                        inProgress.get().getStatusChangedAt(),
                        finished.get().getStatusChangedAt()
                );
                durations.add(duration);
            }
        }

        return durations;
    }

    private double applyCalculationType(List<Duration> durations, CalculationType type) {
        if (durations.isEmpty()) {
            return 0;
        }

        return switch (type) {
            case AVERAGE -> durations.stream()
                    .mapToLong(Duration::toSeconds)
                    .average()
                    .orElse(0);
            case MINIMUM -> durations.stream()
                    .mapToLong(Duration::toSeconds)
                    .min()
                    .orElse(0);
            case MAXIMUM -> durations.stream()
                    .mapToLong(Duration::toSeconds)
                    .max()
                    .orElse(0);
        };
    }

    private double convertToTimeUnit(double seconds, TimeUnit unit) {
        return switch (unit) {
            case SECONDS -> seconds;
            case MINUTES -> seconds / 60;
            case HOURS -> seconds / 3600;
        };
    }
}