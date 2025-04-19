package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.BestSales;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BestSalesDAO {
    private final DbConnection dataSource;

    public BestSalesDAO() {
        this.dataSource = new DbConnection();
    }

    public BestSalesDAO(DbConnection dataSource) {
        this.dataSource = dataSource;
    }

    public List<BestSales> getRealBestSales() {
        List<BestSales> bestSalesList = new ArrayList<>();
        String sql = "SELECT d.id, d.name as dish_name, SUM(bs.quantity_sold) as total_quantity, " +
                "SUM(bs.total_amount) as total_amount, bs.calculation_date " +
                "FROM best_sales bs JOIN dish d ON bs.dish_id = d.id " +
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

    public void saveBestSales(BestSales bestSales) throws SQLException {
        String sql = "INSERT INTO best_sales (dish_id, quantity_sold, total_amount, calculation_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bestSales.getDishId());
            pstmt.setInt(2, bestSales.getQuantitySold());
            pstmt.setDouble(3, bestSales.getTotalAmount());
            pstmt.setDate(4, Date.valueOf(bestSales.getCalculationDate()));

            pstmt.executeUpdate();
        }
    }
}
