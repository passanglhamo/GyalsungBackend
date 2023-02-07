package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.webapp.config.jdbc.DriverClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.Collection;

/**
 * @author Rajib Kumer Ghosh
 */

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        // jsr250Enabled = true,
//        prePostEnabled = true)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private IUserInfoRepository userInfoRepository;

    public SecurityConfig(@Autowired IUserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(IUserInfoRepository users) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return users
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
            }
        };
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService(userInfoRepository)).passwordEncoder(encoder());
    }

    public static boolean matchAnyAdminRole(String... args) {
        return String.join(" ", args).toUpperCase().contains("ADMIN");
    }

    public static boolean matchAnyAdminRole(Collection<? extends GrantedAuthority> authority) {
        String[] args = AuthorityUtils.authorityListToSet(authority).toArray(new String[0]);
        return matchAnyAdminRole(args);
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(-1);//-1 means no file size restriction
        return multipartResolver;
    }
}
