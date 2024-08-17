package com.cache.guava_basic.service;

import com.cache.guava_basic.dao.SubscriptionRepository;
import com.cache.guava_basic.entity.Subscription;
import com.cache.guava_basic.model.SubscriptionKey;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private LoadingCache<SubscriptionKey, List<Subscription>> guavaCache;

    @PostConstruct
    public void initCache() {
        guavaCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)  // Set the maximum number of entries
                .build(new CacheLoader<>() {
                    @Override
                    public List<Subscription> load(@NonNull SubscriptionKey key) {
                        return loadSubscriptionsFromDatabase(key.getAppName(), key.getEventName());
                    }
                });
    }

    public List<Subscription> getAllSubscriptions(String appName, String eventName) {
        SubscriptionKey key = SubscriptionKey.builder()
                .appName(appName)
                .eventName(eventName)
                .build();
        try {
            return guavaCache.get(key);
        } catch (Exception e) {
            log.error("Error loading from cache", e);
            return loadSubscriptionsFromDatabase(appName, eventName);
        }
    }

    public void addSubscription(Subscription subscription) {
        log.info("subscription received in service");
        subscriptionRepository.save(subscription);
        log.info("subscription saved");
        guavaCache.invalidate(SubscriptionKey.builder()
                .appName(subscription.getAppName())
                .eventName(subscription.getEventName())
                .build());
        log.info("cache invalidated");
    }

    private List<Subscription> loadSubscriptionsFromDatabase(String appName, String eventName) {
        log.info("Fetching from database for appName: {} and eventName: {}", appName, eventName);
        return subscriptionRepository.findByAppNameAndEventName(appName, eventName);
    }

}
