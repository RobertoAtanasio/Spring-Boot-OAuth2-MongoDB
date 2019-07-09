package com.rapl.curso.ws.security.utils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TestesDiversos {
	
	public static void main(String[] args) {
		
		Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, 60*24);
        Date expiryDate = new Date(cal.getTime().getTime());
        System.out.println( expiryDate );
        
        LocalDateTime dataLocal = LocalDateTime.now();
        dataLocal = dataLocal.plusHours(24);
        System.out.println( dataLocal );
        
        
        System.out.println( UUID.randomUUID().toString() );
		
	}
	
	
}
