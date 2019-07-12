package com.coding.challenge.service;

import com.coding.challenge.entity.Subscriber;
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

  private List<String> subscribers = new ArrayList<>();

  /**
   * The logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

  /**
   * The Health Checker service.
   */
  @Autowired
  private CustomHealthChecker customHealthChecker;

  @Autowired
  private HealthService healthService;

  /**
   * The Scheduler method.
   */
  @Scheduled(fixedRateString = "${scheduler.interval}")
  public void notifyHealthStatus() {
    String status = customHealthChecker.health().getStatus().getCode();
    logger.info("The external service status is " + status);

    Iterable<Subscriber> subscriberList = healthService.getAllSubscribers();
    if(subscriberList != null) {
      subscriberList.forEach(subscriber -> sendEmail(subscriber, status));
    }
    else{
      logger.info("Currently there are no subscribers for service health status");
    }

  }

  private void sendEmail(Subscriber subscriber, String status){
    if (!subscribers.contains(subscriber.getEmail()) && status.equalsIgnoreCase("DOWN")) {
      logger.info("Sending status down email to "+subscriber.getEmail());
      subscribers.add(subscriber.getEmail());
    }
    if(status.equalsIgnoreCase("UP") && subscribers.size() > 0) {
      subscribers.forEach(s -> logger.info("Sending status up email to " + s));
      subscribers.clear();
    }
  }
}
