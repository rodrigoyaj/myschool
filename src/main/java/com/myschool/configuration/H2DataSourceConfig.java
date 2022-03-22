package com.myschool.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class H2DataSourceConfig {
    @Value("${jdbc.h2.datasource.url:'jdbc:h2:mem:testdb'}")
    private String url;
    @Value("${jdbc.h2.datasource.username:sa}")
    private String userName;
    @Value("${jdbc.h2.datasource.password:}")
    private String password;

   // @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        return dataSource;
    }
}
