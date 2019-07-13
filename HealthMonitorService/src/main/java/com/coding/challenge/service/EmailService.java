package com.coding.challenge.service;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.util.Constants;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * This service handles the email capabilities of the application.
 */
@Component
public class EmailService {

    /**
     * The logger object
     */
    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    //MailClient API Key (Ideally the key should never be part of source code) This is for the demo purpose only.
//    private static String apiKey = "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";
    private static String key = "4b818acc68907a78b6f3dc9a247b643f";
    private static String secret = "175184c0c8a577df98a1d8edd828cd2b";

    /**
     * This method sends the service status email to health subscribers.
     * @param email
     * @param status
     */
    public void sendEmail(String email, String status) {

        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(key, secret, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "darshanbangre89@gmail.com")
                                        .put("Name", "Darshan Bangre"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", "You")))
                                .put(Emailv31.Message.SUBJECT, "Dependency Service Status")
                                .put(Emailv31.Message.HTMLPART, "<h3>" +
                                        (status.equalsIgnoreCase(Constants.UP) ? Constants.UP_MESSAGE : Constants.DOWN_MESSAGE) +
                                        "</h3>")));
        try {
            response = client.post(request);
            if(response.getStatus() == HttpStatus.OK.value()) {
                logger.info("Email successfuly sent to " + email);
            }
        } catch (MailjetException e) {
            logger.info("Failed to sent email to " + email);
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            logger.info("Failed to sent email to " + email);
            e.printStackTrace();
        }

    }

//        logger.info("The API key is " + apiKey);
//        Email from = new Email("darshanbangre89@gmail.com"); //not my actual email id. Created an email account for this app purpose :)
//        String subject = "Dependency Service Status"; // email subject
//        Email to = new Email(emailId);
//        Content content = new Content();
//        if (status.equalsIgnoreCase(Constants.DOWN)) {
//            content = new Content("text/plain", Constants.DOWN_MESSAGE);
//        } else if (status.equalsIgnoreCase(Constants.UP)) {
//            content = new Content("text/plain", Constants.UP_MESSAGE);
//        }
//        Mail mail = new Mail(from, subject, to, content);
//        SendGrid sg = new SendGrid(apiKey);
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sg.api(request);
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (IOException ex) {
//            logger.error("Failed to send email to " + emailId);
//            ex.printStackTrace();
//        }
//    }


}
