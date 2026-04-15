package com.erp.assurance.tunisie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages = "com.erp.assurance.tunisie.repository")
public class DatabaseConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        // Configure EntityManagerFactory here
        return null;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        // Configure TransactionManager here
        return null;
    }
}