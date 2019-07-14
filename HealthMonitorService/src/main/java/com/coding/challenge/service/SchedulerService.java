package com.coding.challenge.service;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The Scheduler Service to frequently monitor the external service.
 */
@Component
public class SchedulerService {

    /**
     * The logger object
     */
    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    /**
     * Global list to hold the subscribers who have been notified.
     */
    private List<String> subscribers = new ArrayList<>();
    /**
     * The Health Checker service instance.
     */
    @Autowired
    private CustomHealthChecker customHealthChecker;

    /**
     * The Email Service instance.
     */
    @Autowired
    private EmailService emailService;

    /**
     * The HealthService instance.
     */
    @Autowired
    private HealthService healthService;

    /**
     * The Scheduler method.
     */
    @Scheduled(fixedDelayString = "${scheduler.delay.interval}", initialDelay = 8000)
    public void notifyHealthStatus() {
        //customHealthChecker can be invoked or the actuator health API can be invoked as well to fetch the health status.
        //Avoiding network calls and making a method call instead.
        String status = customHealthChecker.health().getStatus().getCode();
        logger.info("The external service status is " + status);

        //fetch all the subscribers if the service is down
        if (status.equalsIgnoreCase(Constants.DOWN)) {
            List<Subscriber> subscriberList = new ArrayList<>();
            healthService.getAllSubscribers().forEach(subscriberList::add);
            if (subscriberList != null && subscriberList.size() > 0) {
                //send email to subscribers
                subscriberList.forEach(subscriber -> sendEmail(subscriber.getEmail(), status));
            } else {
                logger.info("Currently there are no subscribers for service health status");
            }
        } else if(status.equalsIgnoreCase(Constants.UP) && subscribers.size() > 0) {
            if(subscribers.size() > 0) {
                subscribers.forEach(s -> sendEmail(s, status));
                //clear the subscribers list after sending out an email.
                subscribers.clear();
            }
            else {
                logger.info("Currently there are no subscribers for service health status");
            }

        }
    }

    /**
     * This method send out the service status email to subscribers.
     *
     * @param email
     * @param status
     */
    private void sendEmail(String email, String status) {
        //check if the subscriber is already notified
        if (!subscribers.contains(email) && status.equalsIgnoreCase(Constants.DOWN)) {
            logger.info("Sending status " + status + " email to " + email);
            emailService.sendEmail(email, status);
            subscribers.add(email);
        }
        if(status.equalsIgnoreCase(Constants.UP)) {
            emailService.sendEmail(email, status);
        }
    }
}
