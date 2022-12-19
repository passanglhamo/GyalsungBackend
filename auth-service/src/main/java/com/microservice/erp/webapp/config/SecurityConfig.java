package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.repositories.UserRepository;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collection;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    public SecurityConfig() {
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository users) {
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
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                //.requiresChannel().anyRequest().requiresSecure() //enable for Https
                //.and()
                //.authorizeRequests().anyRequest().authenticated() //enable to restrict all
                //.and()
                //.authorizeRequests().antMatchers("/auth/auth/v1/new/**"
                //                                            , "/auth/access/v1/add/**"
                //                                            , "/auth/access/v1/remove/**"
                //                                            , "/auth/access/v1/fetch/**")
                //                    .hasAnyRole("GOD_ADMIN","ROLE_ADMIN", "ADMIN")
                //.and()
                //.authorizeRequests().antMatchers("/auth/auth/v1/isAccountExist"
                //                                            , "/auth/auth/v1/login"
                //                                            , "/auth/auth/v1/forget"
                //                                            , "/auth/auth/v1/reset"
                //                                            , "/auth/access/v1/hasPermission")
                //                    .permitAll() //enable to open some paths
                //.and()
                .authorizeRequests().antMatchers("/**").permitAll() //enable to open all
                .and()
                .addFilterBefore(new AuthorizationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(URL_WHITELIST);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService(null)).passwordEncoder(encoder());
    }

    public static boolean matchAnyAdminRole(String...args) {
        return String.join(" ", args).toUpperCase().contains("ADMIN");
    }

    public static boolean matchAnyAdminRole(Collection<? extends GrantedAuthority> authority) {
        String[] args = AuthorityUtils.authorityListToSet(authority).toArray(new String[0]);
        return matchAnyAdminRole(args);
    }

}
