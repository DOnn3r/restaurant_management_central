package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.PointDeVente;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PointDeVenteDAO {
    private DbConnection dataSource;

    public PointDeVenteDAO(){
        this.dataSource = new DbConnection();
    }

    public List<PointDeVente> getAllPointDeVente() {
        List<PointDeVente> pointsDeVente = new ArrayList<>();
        String sql = "SELECT id, name, url FROM point_of_sale";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pointsDeVente.add(new PointDeVente(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("url"),
                        rs.getTimestamp("last_sync").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pointsDeVente;
    }
}