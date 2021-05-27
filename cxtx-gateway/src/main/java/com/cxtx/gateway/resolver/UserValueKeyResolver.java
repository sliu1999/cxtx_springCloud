package com.cxtx.gateway.resolver;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 根据post请求体参数限流
 * @author sliu
 * @date 2021/3/29
 */
public class UserValueKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        //请求body必须带user参数
        System.out.print("user参数值限流");
        if("".equals(exchange.getAttribute("cachedRequestBodyObject")) || exchange.getAttribute("cachedRequestBodyObject") == null){
            return Mono.just(exchange.getRequest().getURI().getPath());
        }
        Flux<DataBuffer> cachedRequestBodyObject =  exchange.getAttribute("cachedRequestBodyObject");
        String cachedRequestBodyString = resolveBodyFromRequest(cachedRequestBodyObject);
        JSONObject cachedRequestBody = JSONObject.parseObject(cachedRequestBodyString);
        return Mono.just(cachedRequestBody.getString("user"));

    }


    private String resolveBodyFromRequest(Flux<DataBuffer> body) {
        //获取请求体
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }
    }
