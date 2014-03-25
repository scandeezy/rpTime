/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.roosterpark.rptime.service;

import java.util.Random;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class EmailNotificationService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    public Integer sendWeeklyNotification()
    {
        Random rand = new Random();
        return rand.nextInt();
    }
}
