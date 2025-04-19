package org.example.restaurant_management_central.DAO.repository;

import org.example.restaurant_management_central.model.CalculationType;
import org.example.restaurant_management_central.model.ProcessingTimeResponse;
import org.example.restaurant_management_central.model.TimeUnit;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcessingTimeDAO {
    private final DbConnection dataSource;

    public ProcessingTimeDAO(DbConnection dataSource) {
        this.dataSource = dataSource;
    }
    public ProcessingTimeDAO(){
        this.dataSource = new DbConnection();
    }
    public void saveProcessingTime(ProcessingTimeResponse processingTime) throws SQLException {
        String sql = """
            
                INSERT INTO processing_time 
            (dish_id, duration, time_unit, calculation_type, synced_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, processingTime.getDishId());
            stmt.setDouble(3, processingTime.getDuration());
            stmt.setString(4, String.valueOf(processingTime.getUnit()));
            stmt.setString(5, String.valueOf(processingTime.getType()));
            stmt.setTimestamp(6, Timestamp.valueOf(processingTime.getLastUpdated()));

            stmt.executeUpdate();

        }
    }

    public List<ProcessingTimeResponse> getLatestProcessingTimes(int limit) throws SQLException {
        String sql = """
            SELECT * FROM processing_time
            ORDER BY synced_at DESC
            LIMIT ?
            """;
        List<ProcessingTimeResponse> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProcessingTimeResponse pt = new ProcessingTimeResponse(
                            rs.getLong("dish_id"),
                            rs.getDouble("duration"),
                            TimeUnit.valueOf(rs.getString("time_unit")),
                            CalculationType.valueOf(rs.getString("calculation_type")),
                            rs.getTimestamp("last_update").toLocalDateTime()
                    );
                    results.add(pt);
                }
            }
        }

        return results;
    }
}