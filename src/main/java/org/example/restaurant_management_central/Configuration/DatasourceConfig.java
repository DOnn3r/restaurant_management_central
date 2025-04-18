package org.example.restaurant_management_central.Configuration;

import org.example.restaurant_management_central.DAO.repository.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatasourceConfig {

    @Bean
    @Primary
    public javax.sql.DataSource springDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource dataSource() {
        return new DataSource();
    }
}
