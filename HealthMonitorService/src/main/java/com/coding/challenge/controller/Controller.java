package com.coding.challenge.controller;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Controller class to expose the APIs.
 */
@RestController
@RequestMapping(path = "/api")
public class Controller {
  /**
   * The HealthService instance.
   */
  @Autowired
  private HealthService healthService;

  /**
   * The API for subscribing users for external service health.
   * @return the user details.
   */
  @PostMapping(path = "/service/status/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public Subscriber subscribeForStatus(@RequestBody Subscriber subscriber) {
    return healthService.addSubscriber(subscriber);
  }
}
