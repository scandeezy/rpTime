/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.roosterpark.rptime.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Named
public class EmailNotificationService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    public Integer sendWeeklyNotification()
    {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "...";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
             new InternetAddress("scannell.ben@gmail.com", "Scandeezy"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            LOGGER.error("An error occurred.", e);
        } catch (MessagingException e) {
            LOGGER.error("An error occurred.", e);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("An error occurred.", ex);
        }
        Random rand = new Random();
        return rand.nextInt();
    }
}
