package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.entities.Username;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Optional;

/**
 * @author Rajib Kumer Ghosh
 */

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.microservice.erp.domain.repository"}
)
@PropertySource("classpath:mysql-db.properties")
@PropertySource("classpath:h2-db.properties")
@PropertySource("classpath:postgres-db.properties")
public class JPAConfig {

    private Environment env;

    public JPAConfig(Environment env) {
        this.env = env;
    }

    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${app.db.name}")
    String persistenceUnitName;

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary @Bean
    public DataSource dataSource(){
        DataSource dataSource = DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName(driverClassName)
                .build();
        return dataSource;
    }

    @Primary @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource){
        return builder
                .dataSource(dataSource)
                .packages("com.microservice.erp.domain.entities")
                .persistenceUnit(persistenceUnitName)
                .build();
    }

    @Primary @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public AuditorAware<Username> auditor() {
        return () -> Optional.ofNullable(new Username("GOD-ADMIN-USERNAME"));
    }

}
