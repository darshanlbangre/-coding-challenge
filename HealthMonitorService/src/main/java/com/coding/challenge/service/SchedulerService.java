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
     * The Health Checker service.
     */
    @Autowired
    private CustomHealthChecker customHealthChecker;

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
        logger.debug("The external service status is " + status);

        //fetch all the subscribers if the service is down
        Iterable<Subscriber> subscriberList = healthService.getAllSubscribers();
        if (subscriberList != null) {
            //send email to subscribers
            subscriberList.forEach(subscriber -> sendEmail(subscriber, status));
        } else {
            logger.info("Currently there are no subscribers for service health status");
        }

    }

    /**
     * This method send out the service status email to subscribers.
     *
     * @param subscriber
     * @param status
     */
    private void sendEmail(Subscriber subscriber, String status) {
        //check if the subscriber is already notified
        if (!subscribers.contains(subscriber.getEmail()) && status.equalsIgnoreCase(Constants.DOWN)) {
            logger.info("Sending status down email to " + subscriber.getEmail());
            subscribers.add(subscriber.getEmail());
        }
        //send out the follow-up email to the subscribers who have been notified about status down.
        if (status.equalsIgnoreCase(Constants.UP) && subscribers.size() > 0) {
            subscribers.forEach(s -> logger.info("Sending status up email to " + s));
            //clear the subscribers list after sending out an email.
            subscribers.clear();
        }
    }
}
