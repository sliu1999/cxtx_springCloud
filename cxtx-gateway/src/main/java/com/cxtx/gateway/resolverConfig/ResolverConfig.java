package com.cxtx.gateway.resolverConfig;

import com.cxtx.gateway.resolver.HostAddrKeyResolver;
import com.cxtx.gateway.resolver.UriKeyResolver;
import com.cxtx.gateway.resolver.UserKeyResolver;
import com.cxtx.gateway.resolver.UserValueKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 限流配置
 * @author sliu
 * @date 2021/3/24
 */
@Configuration
public class ResolverConfig {

    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }

    @Bean
    @Primary
    public UriKeyResolver uriKeyResolver() {
        return new UriKeyResolver();
    }

    @Bean
    public UserKeyResolver userKeyResolver() {
        return new UserKeyResolver();
    }

    @Bean
    public UserValueKeyResolver userValueKeyResolver() {
        return new UserValueKeyResolver();
    }

}
