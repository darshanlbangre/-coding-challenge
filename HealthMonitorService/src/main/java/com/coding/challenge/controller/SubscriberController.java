package com.coding.challenge.controller;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Controller class to expose the service APIs.
 */
@RestController
@RequestMapping(path = "/api")
public class SubscriberController {
    /**
     * The HealthService instance.
     */
    @Autowired
    private HealthService healthService;

    /**
     * The API for subscribing users for external service health.
     *
     * @return the user details.
     */
    @PostMapping(path = "/service/status/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Subscriber subscribeForStatus(@RequestBody Subscriber subscriber, HttpServletResponse response) throws IOException {
        if (subscriber != null && subscriber.getEmail() != null && subscriber.getName() != null) {
            return healthService.addSubscriber(subscriber);
        } else {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Mandatory fields i.e.. name or email are missing.");
            return null;
        }
    }

    /**
     * The API for subscribing users for external service health.
     *
     * @return the user details.
     */
    @GetMapping(path = "/service/status/subscribers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Subscriber> getAllSubscribers(HttpServletResponse response) throws IOException {
        List<Subscriber> subscribers = new ArrayList<>();
        healthService.getAllSubscribers().forEach(subscribers::add);
        if (subscribers.size() > 0) {
            return subscribers;
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value()); // Setting HTTP response status to 404 as there are no subscribers found.
            return null;
        }
    }
}
