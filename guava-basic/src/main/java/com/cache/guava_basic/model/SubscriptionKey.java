package com.cache.guava_basic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionKey implements Serializable {

    private static final long serialVersionUID = 4087186666454278901L;

    private String appName;
    private String eventName;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        SubscriptionKey that = (SubscriptionKey) o;
//        return appName.equals(that.appName) && eventName.equals(that.eventName);
//    }
//
//    @Override
//    public int hashCode() {
//        return appName.hashCode() * 31 + eventName.hashCode();
//    }
}
