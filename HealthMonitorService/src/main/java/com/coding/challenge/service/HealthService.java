package com.coding.challenge.service;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.repository.HealthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /**
     * This method invokes the repository to insert subscriber details.
     *
     * @param subscriber
     * @return
     */
    public Subscriber addSubscriber(Subscriber subscriber) {
        return healthRepository.save(subscriber);
    }

    /**
     * This method returns all the subscribers.
     *
     * @return
     */
    public Iterable<Subscriber> getAllSubscribers() {
        return healthRepository.findAll();
    }
}
