package com.aptCard.example.mailsender;

import org.springframework.stereotype.Service;

//Interface
@Service
public interface EmailService {

 // Method
 // To send a simple email
 String sendSimpleMail(EmailDetails details);

 // Method
 // To send an email with attachment
 String sendMailWithAttachment(EmailDetails details);
}