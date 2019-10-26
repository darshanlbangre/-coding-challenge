# Service Health Monitor

# Introduction

Setup a Java Spring project with a dependency on another service (could be a database, a JMS queue,
another public API, etc...). From there, create an endpoint where users can sign up for alerts about
service outages. That signup will require a name and email address. Your project should monitor its
dependencies and if it detects that the dependent service is unavailable, it should send an alert to all
users who have signed up.

# Project Overview
This project contains TWO different applications i.e.. HealthMonitorService and RemoteService. The former keeps track of the service health status of the latter (RemoteService). The HealthMonitorService contains an in-memory database and exposes APIs for interested users to subscribe for an email notification of the heath status of RemoteService.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them
1. JDK 11
2. Maven
3. IDE (probably intelliJ)
4. Make sure the ports 8080 and 9000 are available and not accupied on the machines. The applications will attempt to run on these ports.

## Installation

1. Checkout the project from master branch.
2. Run mvn clean package on both the projects individually.
3. Navigate to RemoteService/target and start the remote service by executing "java -jar RemoteService-0.0.1-SNAPSHOT.jar" 
4. Navigate to HealthMonitorService/target and start the service by execting "java -jar health-monitor-service-0.0.1-SNAPSHOT.jar"
5. After the services have successully started, add the subscribers via POST REST API described below.

## REST APIs

* Add a subscriber
  * POST http://localhost:8080/api/service/status/subscribe
* Get all the subscribers
  * GET http://localhost:8080/api/service/status/subscribers


## Running the tests

The project runs SpringBootTest which runs the entire integration tests by actually starting the container and by invoking the actual REST APIs.

## Steps To Validate The Project

* Start the RemoteService (java -jar RemoteService-0.0.1-SNAPSHOT.jar)
* Start the HealthMonitorService (java -jar health-monitor-service-0.0.1-SNAPSHOT.jar)
* Subscribe for service health notification (Use POST API described above)
  * Example: http://localhost:8080/api/service/status/subscribe
  * Payload: {
	              "name":"your-name",
	              "email":"your-email-id"
             }
* Stop the RemoteService. Within few seconds, you should receive an email on above mentioned valid email address with the service down notification.
* Start the RemoteService again, you will receive another follow-up email on service restored notification.
* Note: I am using a free tier of MailJet and it allows only 10 emails per hour. So, after 10th email, the next email will be delivered after an hour.


## Deployment

The applications can be ran on any java based machine since it runs in it's own tomcat server.

## Built With

* [SpringBoot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Acknowledgments

* The project is using free tier of Mailjet for sending actual emails. Currently the number of emails per hour is limited to 10.
* Have not implemented the unit tests for the entire source code due to time contraints
* Will add more if I remember any :)

