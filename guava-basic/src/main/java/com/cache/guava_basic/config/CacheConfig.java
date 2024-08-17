package com.cache.guava_basic.config;

import com.cache.guava_basic.dao.SubscriptionRepository;
import com.cache.guava_basic.entity.Subscription;
import com.cache.guava_basic.model.SubscriptionKey;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class CacheConfig {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Bean
    public LoadingCache<SubscriptionKey, List<Subscription>> subscriptionCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)  // Set the maximum number of entries
                .build(new CacheLoader<>() {
                    @Override
                    public List<Subscription> load(@NonNull SubscriptionKey key) {
                        log.info("Fetching from database for key: {}", key);
                        return subscriptionRepository.findByAppNameAndEventName(key.getAppName(), key.getEventName());
                    }
                });
    }
}
