package com.coding.challenge.service;

import com.coding.challenge.util.Constants;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

/**
 * This service handles the email capabilities of the application.
 */
public class EmailService {

    /**
     * The logger object
     */
    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    @Value("${sendgrid.api.key}")
    private static String apiKey;

    /**
     * This method sends the service status email to health subscribers.
     * @param emailId
     * @param status
     */
    private void sendEmail(String emailId, String status) {

        Email from = new Email("darshanbangre89@gmail.com"); //not my actual email id. Created an email account for this app purpose :)
        String subject = "Dependency Service Status"; // email subject
        Email to = new Email(emailId);
        Content content = new Content();
        if (status.equalsIgnoreCase(Constants.DOWN)) {
            content = new Content("text/plain", Constants.DOWN_MESSAGE);
        } else if (status.equalsIgnoreCase(Constants.UP)) {
            content = new Content("text/plain", Constants.UP_MESSAGE);
        }
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error("Failed to send email to " + emailId);
            ex.printStackTrace();
        }
    }

    public void handleEmails(List<String> emails, String status) {
        emails.forEach(email -> sendEmail(email, status));
    }

}
