package com.cache.guava_basic.service;

import com.cache.guava_basic.dao.SubscriptionRepository;
import com.cache.guava_basic.entity.Subscription;
import com.cache.guava_basic.model.SubscriptionKey;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final LoadingCache<SubscriptionKey, List<Subscription>> subscriptionCache;

    public List<Subscription> getAllSubscriptions(String appName, String eventName) {
        SubscriptionKey key = SubscriptionKey.builder()
                .appName(appName)
                .eventName(eventName)
                .build();
        try {
            log.info("fetching from cache for key: {}", key);
            return subscriptionCache.get(key);
        } catch (Exception e) {
            log.error("Error loading from cache", e);
            return loadSubscriptionsFromDatabase(appName, eventName);
        }
    }

    public void addSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
        log.info("subscription saved");
        subscriptionCache.invalidate(SubscriptionKey.builder()
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
