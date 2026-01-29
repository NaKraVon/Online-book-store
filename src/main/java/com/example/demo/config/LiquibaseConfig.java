package com.example.demo.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);

        // Point to your master changelog file
        liquibase.setChangeLog("classpath:db/changelog/db-changelog-master.yaml");

        // Contexts/Labels (optional, keep default for now)
        // liquibase.setContexts("development,test,production");

        // Force it to update the database
        liquibase.setShouldRun(true);

        return liquibase;
    }
}
