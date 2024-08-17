package com.cache.guava_basic.controller;

import com.cache.guava_basic.entity.Subscription;
import com.cache.guava_basic.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/subscriptions")
@Slf4j
@RequiredArgsConstructor
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<?> addSubscription(@RequestBody Subscription subscription) {
        try {
            log.info("Subscription received: {}", subscription);
            subscriptionService.addSubscription(subscription);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getSubscriptions(
            @RequestParam String appName, @RequestParam String eventName) {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions(appName, eventName);
        return ResponseEntity.ok(subscriptions);
    }

}
