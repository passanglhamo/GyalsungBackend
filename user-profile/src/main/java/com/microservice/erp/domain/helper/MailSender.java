package com.microservice.erp.domain.helper;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MailSender {
    public static Boolean sendMail(String destinationEmail, String sentMailFrom, File attachmentFile, String messageBody,
                                   String subject) throws Exception {

        boolean isMailSent = false;
        final String trayMessage;
        Resource resource = new ClassPathResource("/mailConfig/mailConfig.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        final String isMailSendRequired = props.getProperty("mail.isRequiredToSend");
        if (isMailSendRequired.equalsIgnoreCase("0") || isMailSendRequired.isEmpty()) {
            return isMailSent;
        }
        sentMailFrom = props.getProperty("mail.fromAddress", "");

        if (sentMailFrom.isEmpty()) {
            return isMailSent;
        }

        if (destinationEmail == null || destinationEmail.isEmpty()) {
            new Thread() {
                public void run() {
                    String errorMsg = "Mail cannot be sent. There is no mail address given to sent mail";
                    SystemTrayIcon.displayTray(errorMsg);
                }
            }.start();
            return isMailSent;
        }

        final String username = props.getProperty("mail.username", "dhicompact@dhi.bt");
        final String password = props.getProperty("mail.password", "ogie pmue ewlk pwgd");
        String host = props.getProperty("mail.host", "smtp.gmail.com");
        String port = props.getProperty("mail.port", "587");
        String auth = props.getProperty("mail.auth", "true");
        String startLlsEnable = props.getProperty("mail.startLlsEnable", "true");

        //smtp configuration
        String smtpHost = props.getProperty("mail.smtpHost", "mail.smtp.host");
        String smtpPort = props.getProperty("mail.smtpPort", "mail.smtp.port");
        String smtpAuth = props.getProperty("mail.smtpAuth", "mail.smtp.auth");
        String smtpStartLlsEnable = props.getProperty("mail.smtpStartLlsEnable", "mail.smtp.starttls.enable");
        Properties properties = new Properties();
        properties.put(smtpHost, host);
        properties.put(smtpPort, port);
        properties.put(smtpAuth, auth);
        properties.put(smtpStartLlsEnable, startLlsEnable);

        //creating session of current user.
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        final Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sentMailFrom));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationEmail));
        message.setSubject(subject);
        message.setSentDate(new Date());

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageBody, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        //set text part message
        multipart.addBodyPart(messageBodyPart);
        //adding attachmentFile
        if (attachmentFile != null) {
            MimeBodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.attachFile(attachmentFile);
            multipart.addBodyPart(messageBodyPart1);
        }

        //send the complete message parts
        message.setContent(multipart);
        trayMessage = "Mail is sent successfully to " + destinationEmail;
        new Thread() {
            public void run() {
                try {
                    Transport.send(message);
                    SystemTrayIcon.displayTray(trayMessage);
                } catch (MessagingException e) {
                    String errorMsg = "Mail cannot be sent. Check your net connection or configuration or " +
                            "if destination email address is valid or not.";
                    SystemTrayIcon.displayTray(errorMsg);
                }
            }
        }.start();
        isMailSent = true;
        return isMailSent;
    }

    public static Boolean sendMailWithoutFile(String message, String subject, String toAddress, String fromAddress) throws Exception {
        return MailSender.sendMail(toAddress, fromAddress, null, message, subject);
    }

    public static Boolean sendMailWithFile(MultipartFile multipartFile, String message, String toAddress) throws Exception {
        String fromAddress = "info@bt.gov.bt";
        //String message1 = "User creation success";
        String subject = "User creation";
        File file = convertToFile(multipartFile);
        Boolean isMailSent = MailSender.sendMail(toAddress, fromAddress, file, message, subject);
        return isMailSent;
    }

    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }

    public static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];
            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }
            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}

