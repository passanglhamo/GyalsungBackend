package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.entities.Username;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.Principal;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

import static java.util.Optional.*;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@PropertySources({
        @PropertySource("classpath:postgres-db.properties"),
        @PropertySource("classpath:dbscript/commonDao.mssql.properties")
})
@EnableJpaRepositories("com.microservice.erp")
public class JPAConfig {
    private final Environment env;

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

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.microservice.erp.domain.entities")
                .persistenceUnit(persistenceUnitName)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.microservice.erp");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


//    @Bean
//    public AuditorAware<Username> auditor() {
////
////        Supplier<Boolean> isAnonymous = () -> Optional.ofNullable(SecurityContextHolder.getContext())
////                .map(SecurityContext::getAuthentication)
////                .filter(Authentication::isAuthenticated)
////                .map(Authentication::getPrincipal)
////                .map(isStudent -> isStudent.toString().equals("anonymousUser"))
////                .orElse(false);
//////        Boolean isStudentVal = ()-> ofNullable(SecurityContextHolder.getContext())
//////                        .map(SecurityContext::getAuthentication)
//////                        .filter(Authentication::isAuthenticated)
//////                        .map(Authentication::getPrincipal)
//////                .map(isStudent-> {
//////                    if(isStudent.toString().equals("anonymousUser")){
//////                        return true;
//////                    }
//////                    return false;
//////                });
////
////        if(isAnonymous.get()){
////            return  () -> ofNullable(SecurityContextHolder.getContext())
////                    .map(SecurityContext::getAuthentication)
////                    .filter(Authentication::isAuthenticated)
////                    .map(Authentication::getPrincipal)
////                    .map(UserDetails.class::cast)
////                    .map(u -> new Username(u.getUsername()));
////        }
//
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
//                .map(u -> new Username(BigInteger.ZERO));
//
//
//
//    }



    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        return properties;
    }
}
