package com.erp.assurance.tunisie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class ErpAssuranceTunisieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpAssuranceTunisieApplication.class, args);
    }
}
