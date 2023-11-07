package com.pmd.dm.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
	
	@Autowired
	private JavaMailSender sendMail;
	
	public void generateEmail(String to, String subject, String body) throws MessagingException {
		
		MimeMessage mimeMessage = sendMail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        
        helper.addTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        
        
        sendMail.send(mimeMessage);
        
       
        	
	}

}
