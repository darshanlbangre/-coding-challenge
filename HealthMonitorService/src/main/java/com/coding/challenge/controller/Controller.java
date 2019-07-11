package com.coding.challenge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Controller class to expose the APIs.
 */
@RestController
@RequestMapping(path = "/api")
public class Controller {

  /**
   * The API for subscribing users for external service health.
   * @return the user details.
   */
  @PostMapping("/service/status/subscribe")
  public String subscribeUsersForStatus() {
    return "Hi there";
  }
}
