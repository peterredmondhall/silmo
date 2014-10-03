package com.gwt.wizard.server.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.gwt.wizard.shared.model.BookingInfo;

public class Mailer
{
    private static final Logger log = Logger.getLogger(Mailer.class.getName());
    private static final String FROM_EMAIL = "enter app email id here, can be found from google console";

    public static void send(BookingInfo bookingInfo)
    {
        log.info("Mail:send bookingInfo :" + bookingInfo.getReference());

        String emailMsg = BookingUtil.toEmailText(bookingInfo);
        String html = "error";
        if (bookingInfo.isWithReturn())
        {
            html = BookingUtil.toEmailHtml(bookingInfo, new File("template/confirmation.html"));
        }
        else
        {
            html = BookingUtil.toEmailHtml(bookingInfo, new File("template/confirmationNoReturn.html"));
        }
        String email = bookingInfo.getOrganizerEmail();
        send(emailMsg, "hall@silvermobilityservices.com", html, "Silver Mobility");
        send(emailMsg, email, html, "Silver Mobility");
    }

    private static void send(String msgBody, String toEmail, String htmlBody, String subject)
    {

        // ...
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try
        {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("peterredmondhall@gmail.com", "silvermobilityservices.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail, "Silver Mobility"));
            msg.setSubject("Silver Mobility");
            msg.setText(msgBody);

            // html
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            Transport.send(msg);
            log.info("sent message to :" + toEmail);
        }
        catch (AddressException e)
        {
            try
            {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("peterredmondhall@gmail.com", "silvermobilityservices.com Admin"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("peterredmondhall@gmail.com", "Silver Mobility"));
                msg.setSubject("Error: Silver Mobility");
                msg.setText(e.getMessage());
                Transport.send(msg);
            }
            catch (Exception ex)
            {
                //
            }
            log.log(Level.SEVERE, "address exception :" + e.getMessage());
            throw new RuntimeException(e);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);

        }
    }

    public static boolean send(String toEmail, String subject, byte[] attachment)
    {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        boolean isSent = true;
        try
        {
            Message msg = new MimeMessage(session);
            if (attachment != null)
            {

                msg.setFrom(new InternetAddress(FROM_EMAIL/* "peterredmondhall@gmail.com" */, "silvermobilityservices.com Admin"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toEmail, "Silver Mobility"));

                msg.setSubject(subject);
                msg.setText("..");
                DataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
                Multipart mp = new MimeMultipart();
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setFileName("template_sample.pdf");
                mbp.setDataHandler(new DataHandler(dataSource));
                mp.addBodyPart(mbp);
                msg.setContent(mp);

                Transport.send(msg);
                log.info("Message Sent!!");
                return true;
            }
        }
        catch (AddressException e)
        {
            try
            {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(FROM_EMAIL/* "peterredmondhall@gmail.com" */, "silvermobilityservices.com Admin"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("peterredmondhall@gmail.com", "Silver Mobility"));
                msg.setSubject("Error: Silver Mobility");
                msg.setText(e.getMessage());
                Transport.send(msg);
                log.info("sent message to Admin");
            }
            catch (Exception ex)
            {
                //
            }
            log.log(Level.SEVERE, "address exception :" + e.getMessage());
            throw new RuntimeException(e);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {

        }
        return false;
    }
}
