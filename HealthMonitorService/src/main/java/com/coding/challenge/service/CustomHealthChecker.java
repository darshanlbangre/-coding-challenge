package com.coding.challenge.service;

import com.coding.challenge.util.Constants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * This class implements a Custom Health Checker.
 */
@Component
public class CustomHealthChecker implements HealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(CustomHealthChecker.class);



    @Value("${service.url}")
    private String uri;

    /**
     * This method overrides the health method to include
     * the health status of an external service to actuator.
     *
     * @return the health status.
     */
    @Override
    public Health health() {

        if (isServiceRunning()) {
            return Health.up().withDetail(Constants.SERVICE, Constants.AVAILABLE).build();
        }
        return Health.down().withDetail(Constants.SERVICE, Constants.NOT_AVAILABLE).build();
    }

    /**
     * This method invokes the actuator end point of an external service.
     *
     * @return the service status.
     */
    private Boolean isServiceRunning() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
            String status = new JSONObject(result.getBody()).getString("status");
            if (result.getStatusCodeValue() == HttpStatus.OK.value() && status.equalsIgnoreCase("UP")) {
                return true;
            }
        } catch (ResourceAccessException exception) {
            logger.error("The dependency service is unreachable");
            // Ideally implement custom exception and handle the errors gracefully.
            // Handles the scenario when the service itself is unreachable.
            return false;
        }
        // return false by default for non 200 response status.
        return false;
    }
}
