package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.DishOrder;
import org.example.restaurant_management_central.model.Status;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishOrderDAO {
    private DataSource dataSource;

    public DishOrderDAO() {
        this.dataSource = new DataSource();
    }

    public DishOrderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<DishOrder> findForProcessingTime(Long dishId, LocalDateTime start, LocalDateTime end) {
        List<DishOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM dish_order WHERE dish_id = ? AND status_changed_at BETWEEN ? AND ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, dishId);
            stmt.setTimestamp(2, Timestamp.valueOf(start));
            stmt.setTimestamp(3, Timestamp.valueOf(end));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DishOrder order = new DishOrder();
                order.setId(rs.getLong("id"));
                order.setDishId(rs.getLong("dish_id"));
                order.setOrderReference(rs.getString("order_reference"));
                order.setStatus(Status.valueOf(rs.getString("status")));
                order.setStatusChangedAt(rs.getTimestamp("status_changed_at").toLocalDateTime());
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
