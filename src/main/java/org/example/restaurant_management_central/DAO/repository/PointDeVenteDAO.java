package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.PointDeVente;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PointDeVenteDAO {
    private DataSource dataSource;

    public PointDeVenteDAO(){
        this.dataSource = new DataSource();
    }

    public void saveAll(List<PointDeVente> pointDeVentes) throws SQLException {
        List<PointDeVente> pointDeVenteList = new ArrayList<PointDeVente>();
        try{
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            for(PointDeVente pointDeVente : pointDeVentes){
                String sql = "insert into point_de_vente values(?,?)";

                try(PreparedStatement statement = connection.prepareStatement(sql)){
                    statement.setString(1, pointDeVente.getName());
                    statement.setTimestamp(2, Timestamp.valueOf(pointDeVente.getLastSync()));

                    try(ResultSet resultSet = statement.executeQuery()){
                        if(resultSet.next()){
                            pointDeVenteList.add(pointDeVente);
                        }
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PointDeVente> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM point_de_vente WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(mapToPointDeVente(resultSet));
                }
            }
        }
    }

    private PointDeVente mapToPointDeVente(ResultSet rs) throws SQLException {
        return new PointDeVente(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getTimestamp("last_sync").toLocalDateTime()
        );
    }
}
