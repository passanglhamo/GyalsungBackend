package com.microservice.erp.webapp.config;

//TODO Need to remove

import com.microservice.erp.domain.security.jwt.AuthEntryPointJwt;
import com.microservice.erp.domain.security.jwt.AuthTokenFilter;
import com.microservice.erp.domain.security.services.UserDetailsServiceImpl;
import com.microservice.erp.webapp.config.jdbc.DriverClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author Rajib Kumer Ghosh
 */

//@EnableWebSecurity
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public static final String[] URL_WHITELIST = {
            "/v2/api-docs"
            , "/swagger-ui.html"
            , "/swagger-ui.html/**"
            , "/webjars/springfox-swagger-ui/**"
            , "/swagger-resources/**"
            , "/swagger-resources/configuration/**"
            , "/actuator/health"
            , "/actuator/prometheus"
            , "/h2-console/**"
            , "/kmsfileupload/**"
    };

    @Value("${spring.datasource.driver-class-name}")
    String activeDriverClass;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                //.requiresChannel().anyRequest().requiresSecure() //enable for Https
                //.and()
                //.authorizeRequests().anyRequest().authenticated(); //enable to restrict all
                .authorizeRequests().antMatchers("/**").permitAll();
         //Disable for H2 DB:
        if (activeDriverClass.equalsIgnoreCase(DriverClass.H2_EMBEDDED.toString())) {
            http.headers().frameOptions().disable();
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(URL_WHITELIST);
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(-1);//-1 means no file size restriction
        return multipartResolver;
    }

//    @Bean
//    public ServletRegistrationBean<ImageServlet> exampleServletBean() {
//        ServletRegistrationBean<ImageServlet> bean = new ServletRegistrationBean<>(
//                new ImageServlet(), "/profilePic/*");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
}
