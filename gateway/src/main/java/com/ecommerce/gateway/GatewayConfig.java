package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // This means the bucket can hold upto 20 tokens, 10 tokens are added to the token bucket every second, and
        // 1 token is consumed by each request so under steady conditions 10 request/sec can be handled
        return new RedisRateLimiter(10,20,1);
    }

    @Bean
    public KeyResolver hostNameKeyResolver() {
        // Every user will be uniquely identified by host name
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("product-service", r -> r
                        .path("/api/products/**")
                    .filters(f -> f.retry(retryConfig -> retryConfig
                            .setRetries(10)
                            .setMethods(HttpMethod.GET)
                    ).requestRateLimiter(config -> config
                            .setRateLimiter(redisRateLimiter())
                            .setKeyResolver(hostNameKeyResolver()))
                            .circuitBreaker(config -> config.setName("eComBreaker")
                            .setFallbackUri("forward:/fallback/products")))
                        .uri("lb://PRODUCT-SERVICE"))

                .route("user-service", r -> r
                        .path("/api/users/**")
//                        .filters(f->f.rewritePath("/users(?<segment>/?.*)",
//                                "/api/users${segment}"))
                        .uri("lb://USER-SERVICE"))

                .route("order-service", r -> r
                        .path("/api/cart/**", "/api/orders/**")
//                        .filters(f->f.rewritePath("/(?<segment>.*)",
//                                "/api/${segment}"))
                        .uri("lb://ORDER-SERVICE"))

                .route("eureka-server", r -> r
                        .path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/"))
                        .uri("http://localhost:8761"))

                .route("eureka-server-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))

                .build();

  }
}
