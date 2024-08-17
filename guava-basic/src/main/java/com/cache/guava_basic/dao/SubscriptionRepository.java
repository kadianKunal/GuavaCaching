package com.cache.guava_basic.dao;

import com.cache.guava_basic.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    List<Subscription> findByAppNameAndEventName(String appName, String eventName);

}
