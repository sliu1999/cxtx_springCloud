package com.cxtx.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * user限流
 * @author sliu
 * @date 2021/3/24
 */
public class UserKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        //请求路径必须带user参数
        System.out.print("url参数user限流");
        return Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }
}
