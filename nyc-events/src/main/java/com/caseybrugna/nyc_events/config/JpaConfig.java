package com.caseybrugna.nyc_events.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.JakartaJpaTransactionManager;



import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.caseybrugna.nyc_events.repository")
public class JpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("com.caseybrugna.nyc_events.model");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }

    @Bean
    public DataSource dataSource() {
        // Define your data source configuration here
        // For example, using HikariCP:
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("caseybrugna");
        dataSource.setPassword("Macintosh3!");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
         return new JakartaJpaTransactionManager(emf);
    }
}
