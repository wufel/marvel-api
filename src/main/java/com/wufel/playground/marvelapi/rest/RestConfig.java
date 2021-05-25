package com.wufel.playground.marvelapi.rest;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class RestConfig {

    public static final String CHARACTER_ID_LIST_CACHE = "CHARACTER_ID_LIST_CACHE";
    public static final String CHARACTER_DETAIL_CACHE = "CHARACTER_DETAIL_CACHE";

    @Value("${marvel.api.cache.initial.capacity:200}")
    private Integer cacheInitialCapacity;
    @Value("${marvel.api.cache.initial.capacity:500}")
    private Integer cacheMaximumSize;

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CHARACTER_ID_LIST_CACHE, CHARACTER_DETAIL_CACHE);
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(cacheInitialCapacity)
                .maximumSize(cacheMaximumSize));
        return caffeineCacheManager;
    }
}
