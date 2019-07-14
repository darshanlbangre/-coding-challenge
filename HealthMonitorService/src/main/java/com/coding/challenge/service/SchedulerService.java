package com.coding.challenge.service;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.util.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
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
     * Global list to hold the notifiedSubscribers who have been notified.
     */
    private List<Subscriber> notifiedSubscribers = new ArrayList<>();
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
        //I am Avoiding network calls and making a method call instead.
        String status = customHealthChecker.health().getStatus().getCode();

        logger.debug("The external service status is " + status);

        //fetch all the notifiedSubscribers if the service is down
        if (status.equalsIgnoreCase(Constants.DOWN)) {

            List<Subscriber> subscriberList = new ArrayList<>();
            healthService.getAllSubscribers().forEach(subscriberList::add);
            if (subscriberList.size() > 0) {
                //send email to notifiedSubscribers
                sendDownEmail(subscriberList, status);
            } else {
                logger.info("Currently there are no subscribers for service health status");
            }
        } else if (status.equalsIgnoreCase(Constants.UP) && notifiedSubscribers.size() > 0) {
            sendUpEmail(status);
            //clear the notifiedSubscribers list after sending out an email.
            notifiedSubscribers.clear();
        }
    }


    /**
     * This method send out the service status email to notifiedSubscribers.
     *
     * @param subscribers
     * @param status
     */
    private void sendDownEmail(List<Subscriber> subscribers, String status) {
        JSONArray recipients = new JSONArray();
        //check if the subscriber is already notified
        for (Subscriber subscriber : subscribers) {
            if (!notifiedSubscribers.contains(subscriber)) {
                recipients.put(new JSONObject().put("Email", subscriber.getEmail()));
                notifiedSubscribers.add(subscriber);
            }
        }
        if(recipients.length() > 0){
            logger.info("Sending status down emails to " + recipients.toString());
            emailService.sendEmail(recipients, status);
        }
    }


    /**
     * This method send out the service status email to notifiedSubscribers.
     */
    private void sendUpEmail(String status) {
        JSONArray recipients = new JSONArray();
        //check if the subscriber is already notified
        for (Subscriber subscriber : notifiedSubscribers) {
            recipients.put(new JSONObject().put("Email", subscriber.getEmail()));
        }
        logger.info("Sending status up emails to " + recipients.toString());
        emailService.sendEmail(recipients, status);
    }
}
