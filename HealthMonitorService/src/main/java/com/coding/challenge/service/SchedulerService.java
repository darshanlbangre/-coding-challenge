package com.coding.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

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
   * The Health Checker service.
   */
  @Autowired
  private CustomHealthChecker customHealthChecker;

  /**
   * The Scheduler method.
   */
  @Scheduled(fixedRateString = "${scheduler.interval}")
  public void checkHealth() {
    String status = customHealthChecker.health().getStatus().getCode();
    logger.info("The external service status is " + status);

  }
}
