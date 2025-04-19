package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.Dish;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DishDAO {
    private DataSource dataSource;

    public DishDAO() {
        this.dataSource = new DataSource();
    }

    public DishDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Dish findById(Long id) {
        String sql = """
            SELECT id, original_id, name, point_de_vente_id, last_synced_at
            FROM dish
            WHERE id = ?
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getLong("id"));
                    dish.setOriginalId(rs.getInt("original_id"));
                    dish.setName(rs.getString("name"));
                    return dish;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching dish", e);
        }

        return null;
    }
}
