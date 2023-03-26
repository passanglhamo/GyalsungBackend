package com.microservice.erp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
public class AccessPermissionFilter extends AbstractGatewayFilterFactory<AccessPermissionFilter.Config> {

    private WebClient.Builder builder;

    @Value("${app.auth.has.permission.url}")
    private String accessPermissionURL;

    public AccessPermissionFilter(WebClient.Builder builder) {
        super(Config.class);
        this.builder = builder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return createGatewayFilter(builder, accessPermissionURL);
    }

    public static class Config {}

    public static GatewayFilter createGatewayFilter(WebClient.Builder builder, String accessPermissionURL) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String action = parseAction(exchange.getRequest());
            String resource = parseResource(exchange.getRequest());
            //
            String token = TokenValidator.parseToken(authHeader, "Bearer ");
            JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
            String body = String.format("{\"username\":\"%s\", \"action\":\"%s\", \"resource\":\"%s\"}"
                    ,payload.getIss().isEmpty()?payload.getData().get("email"):payload.getIss()
                    , action
                    , resource);
            //
            Mono<Void> filterChain = builder.build()
                    .post()
                    .uri(accessPermissionURL)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .exchange()
                    .flatMap(clientResponse -> {
                        if (clientResponse.statusCode().value() >= HttpStatus.BAD_REQUEST.value()){
                            //Kick-out from here:
                            return unauthorizedPermissionHandler(exchange
                                    , clientResponse.statusCode()
                                    ,"Un-Authorized Access!");
                        } else {
                            //Passing down the stream:
                            return chain.filter(exchange);
                        }
                    });
            //
            return filterChain;
        };
    }

    private static String parseResource(ServerHttpRequest request) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        List<String> values = queryParams.getOrDefault("resource"
                , Arrays.asList(request.getPath().value()));
        return values.size() > 0 ? values.get(0) : "";
    }

    private static String parseAction(ServerHttpRequest request) {
        String defaultVal;
        switch (request.getMethod()){
            case GET:
                defaultVal = "Read";
                break;
            case POST:
            case PUT:
            case DELETE:
                defaultVal = "Write";
                break;
            default:
                defaultVal = "None";
        }
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        List<String> values = queryParams.getOrDefault("action"
                , Arrays.asList(defaultVal));
        return values.size() > 0 ? values.get(0) : "";
    }

    @SuppressWarnings("Duplicates")
    private static Mono<Void> unauthorizedPermissionHandler(ServerWebExchange exchange, HttpStatus status, Object body) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setLocation(URI.create("/error/unauthorized.html"));
        //
        if (body != null){
            DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
            ObjectMapper objMapper = new ObjectMapper();
            try {
                byte[] obj = objMapper.writeValueAsBytes(body);
                return response.writeWith(Mono.just(obj).map(r -> dataBufferFactory.wrap(r)));
            } catch (Exception e) {}
        }
        //
        return response.setComplete();
    }
}
