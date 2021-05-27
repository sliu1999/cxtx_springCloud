package com.cxtx.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 接口api限流
 * @author sliu
 * @date 2021/3/24
 */
public class UriKeyResolver  implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        System.out.print("接口api限流");
        return Mono.just(exchange.getRequest().getURI().getPath());
    }

}