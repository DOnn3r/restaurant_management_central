package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.BestSales;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BestSalesDAO {
    private DataSource dataSource;

    public BestSalesDAO() {
        this.dataSource = new DataSource();
    }

    public BestSalesDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<BestSales> getRealBestSales() {
        List<BestSales> bestSalesList = new ArrayList<>();
        String sql = "SELECT d.id, d.name as dish_name, SUM(bs.quantity_sold) as total_quantity, " +
                "SUM(bs.total_amount) as total_amount, bs.calculation_date " +
                "FROM best_sales bs " +
                "JOIN dish d ON bs.dish_id = d.id " +
                "JOIN point_of_sale pos ON bs.point_of_sale_id = pos.id " + // Ajouté
                "GROUP BY d.id, d.name, bs.calculation_date " +
                "ORDER BY total_quantity DESC";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bestSalesList.add(new BestSales(
                        rs.getInt("id"),
                        rs.getString("dish_name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_amount"),
                        rs.getDate("calculation_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bestSalesList;
    }

    public void saveBestSales(BestSales bestSales, int pointOfSaleId) throws SQLException {
        // 1. Vérification que le plat existe pour ce point de vente
        String checkDishSql = "SELECT id FROM dish WHERE original_id = ? AND point_of_sale_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkDishSql)) {

            checkStmt.setInt(1, bestSales.getDishId());
            checkStmt.setInt(2, pointOfSaleId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Dish with original_id=" + bestSales.getDishId()
                            + " does not exist for point_of_sale_id=" + pointOfSaleId);
                }
            }
        }

        String insertSql = "INSERT INTO best_sales (dish_id, quantity_sold, total_amount, calculation_date, point_of_sale_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            insertStmt.setInt(1, bestSales.getDishId());
            insertStmt.setInt(2, bestSales.getQuantitySold());
            insertStmt.setDouble(3, bestSales.getTotalAmount());
            insertStmt.setDate(4, Date.valueOf(bestSales.getCalculationDate()));
            insertStmt.setInt(5, pointOfSaleId);

            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating best sales failed, no rows affected.");
            }
        }
    }
}
