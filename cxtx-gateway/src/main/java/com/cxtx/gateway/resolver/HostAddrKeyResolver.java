package com.cxtx.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ip限流
 * @author sliu
 * @date 2021/3/24
 */
public class HostAddrKeyResolver implements KeyResolver {

    //根据ip地址限流
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        System.out.print("进入ip限流");
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

}