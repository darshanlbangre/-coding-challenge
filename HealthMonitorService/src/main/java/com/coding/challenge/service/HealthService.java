package com.coding.challenge.service;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.repository.HealthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The service class to handle the business logic.
 */
@Component
public class HealthService {

    /**
     * The HealthRepository instance.
     */
    @Autowired
    private HealthRepository healthRepository;

    public Subscriber addSubscriber(Subscriber subscriber) {
        return healthRepository.save(subscriber);
    }

    public Iterable<Subscriber> getAllSubscribers() {
        return healthRepository.findAll();
    }
}
