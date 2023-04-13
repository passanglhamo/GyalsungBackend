package com.microservice.erp.config;

import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.microservice.erp.config.AuthFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Rajib Kumer Ghosh
 */

@Configuration
@PropertySource("classpath:service-names.properties")
public class SpringCloudConfig {

    @Value("${app.auth.has.permission.url}")
    private String accessPermissionFilterURL;

    @Value("${app.auth.validation.url}")
    private String authValidationURL;

    @Value("${app.auth.url}")
    private String authURL;

    @Value("${app.first.url}")
    private String firstURL;

    @Value("${app.second.url}")
    private String secondURL;

    @Value("${app.user.profile.url}")
    private String userProfileURL;

    @Value("${app.training.management.url}")
    private String trainingManagementURL;

    @Value("${app.enrolment.deferment.exemption.url}")
    private String enrolmentDefermentExemptionURL;

    @Value("${app.medical.screening.url}")
    private String medicalScreeningURL;

    @Value("${app.notification.url}")
    private String notificationURL;

    /*@Bean
    public GlobalFilter globalFilter() {
        return (exchange, chain) -> {
            System.out.println("Pre Global filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //TODO:
                System.out.println("Post Global filter");
            }));
        };
    }*/

    @Bean("CustomAuthFilter")
    public GatewayFilter getAuthFilter(WebClient.Builder builder) {
        return AuthFilter.createGatewayFilter(builder, new AuthFilter.Config(authValidationURL));
    }

    @Bean("CustomAccessPermissionFilter")
    public GatewayFilter getAccessPermissionFilter(WebClient.Builder builder) {
        return AccessPermissionFilter.createGatewayFilter(builder, accessPermissionFilterURL);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder
            , @Qualifier("CustomAuthFilter") GatewayFilter authFilter
            , @Qualifier("CustomAccessPermissionFilter") GatewayFilter accessPermissionFilter
            , RedisRateLimiter rateLimiter) {

        return builder.routes()
                .route("loginRateLimiter"
                        , r -> r.path("/api/auth/auth/v1/login")
                                .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(rateLimiter))
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(authURL))
                .route("authModule"
                        , r -> r.path("/api/auth/auth/**")
                                .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(authURL))
                .route("authModule"
                        , r -> r.path("/api/auth/ndi/**")
                                .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(authURL))
                /*.route("authModule"
                        , r -> r.path("/api/auth/auth/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(authURL))*/
                .route("authorizationModule"
                        , r -> r.path("/api/auth/access/**")
                                .filters(f -> f.filter(authFilter)
                                        .circuitBreaker(c -> c.setName("access_circuit")
                                                .setFallbackUri("/api/fallback/messages/unreachable"))
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(authURL))

                .route("employeeModuleRateLimit"
                        , r -> r.path("/api/employee/v1/rateLimit/**")
                                .filters(f -> {
                                    //Code breakdown for readability:
                                    return f.requestRateLimiter()
                                            .configure(c -> c.setRateLimiter(rateLimiter))
                                            .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE");
                                })
                                .uri(firstURL))
                .route("employeeModuleDelayed"
                        , r -> r.path("/api/employee/v1/delayed/**")
                                .filters(f -> {
                                    //Code breakdown for readability:
                                    return f.filter(authFilter)
                                            .filter(accessPermissionFilter)
                                            .circuitBreaker(c -> c.setName("id-employee-circuit")
                                                    .setFallbackUri("/api/employee/v1/errorFallback"))
                                            .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE");
                                    //OR Generic messages:
                                    //.setFallbackUri("/api/fallback/messages/unreachable"));
                                })
                                .uri(firstURL))
                .route("employeeModule"
                        , r -> r.path("/api/employee/v1/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                                )
                                .uri(firstURL))
                .route("consumerModule"
                        , r -> r.path("/api/consumer/**")
                                .uri(secondURL))
                .route("userProfile"
                        , r -> r.path("/api/user/profile/signup/**")
                                .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(userProfileURL))
                .route("userProfile"
                        , r -> r.path("/api/user/profile/common/getPersonStatus")
                                .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(userProfileURL))
                .route("userProfile"
                        , r -> r.path("/api/user/profile/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(userProfileURL))
                .route("trainingManagement"
                        , r -> r.path("/api/training/management/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(trainingManagementURL))
                .route("enrolmentDefermentExemption"
                        , r -> r.path("/api/enrolment/deferment/exemption/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(enrolmentDefermentExemptionURL))
                .route("medicalScreening"
                        , r -> r.path("/api/medical/screening/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(medicalScreeningURL))
                .route("notification"
                        , r -> r.path("/api/notification/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(notificationURL))
                .route("authModule"
                        , r -> r.path("/api/auth/**")
                                .filters(f -> f.filter(authFilter)
                                        .filter(accessPermissionFilter)
                                        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                                .uri(authURL))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCircuitBreakerFactory() {
        return (factory) -> factory.configureDefault(id -> {
            //Code breakdown for readability:
            Duration timeout = Duration.ofMillis(3000); //For testing replace with 5100ms.
            return new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom()
                            .timeoutDuration(timeout)
                            .build())
                    .build();
        });
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        /**
         * defaultReplenishRate: Default number of request an user can do in a second without dropping any request.
         * defaultBurstCapacity: Maximum number of request an user allowed to do in a second.
         */
        return new RedisRateLimiter(2, 3);
    }

    /*@Bean
    public KeyResolver userKeyResolver() {
        *//**
         * RedisRateLimiter need a KeyResolver, without this limiter will not work.
         *//*
        return exchange -> Mono.just("rate-limiter-key");
    }*/

    @Bean
    public KeyResolver userKeyResolver(){
        return (exchange) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                System.out.println("No Authorization-Header");//FIXME
                return Mono.just("rate-limiter-key");
            }
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader == null || authHeader.isEmpty()
                    || !authHeader.startsWith("Bearer")){
                System.out.println("EmptyORNull Authorization-Header");//FIXME
                return Mono.just("rate-limiter-key");
            }
            //
            System.out.println("Exist Authorization-Header");//FIXME
            try {
                String token = TokenValidator.parseToken(authHeader, "Bearer ");
                JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
                String username = payload.getIss();
                return Mono.just(username);
            } catch (RuntimeException e) {
                return Mono.just("rate-limiter-key");
            }
        };
    }

}