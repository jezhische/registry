package com.tagsoft.registry.testConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//@Configuration
@EnableJpaRepositories(basePackages = "com.tagsoft.registry.repository")
//@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class PostgresTestProfileDbConfig {

    @Bean
    @Profile("testPostgresTestDb")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/registry_usa");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        return dataSource;
    }
}
