package com.gwt.wizard.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.gwt.wizard.client.util.EncryptionUtil;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.server.util.PdfUtil;
import com.itextpdf.text.pdf.PdfReader;

public class MailHandlerServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    private final Logger LOGGER = Logger.getLogger("MailHandlerServlet");
    private String subject = "";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        LOGGER.info("Mail Recieved!!");
        Properties props = new Properties();
        Session email = Session.getDefaultInstance(props, null);

        try
        {
            MimeMessage message = new MimeMessage(email, req.getInputStream());
            handleMessage(message);
            String summary = message.getSubject();
            String description = getText(message);
            // Address[] addresses = message.getFrom();
            this.subject = message.getSubject();
            LOGGER.info("Subject:" + summary);
            LOGGER.info("Description: " + description);
            // User user = new User(addresses[0].toString(), "gmail.com");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Return the primary text content of the message.
     */

    private String getText(Part p) throws
            MessagingException, IOException
    {
//        if (p.isMimeType("text/*")) {
//            String s = (String)p.getContent();
//            textIsHtml = p.isMimeType("text/html");
//            return s;
//        }

        if (p.isMimeType("multipart/alternative"))
        {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++)
            {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain"))
                {
                    if (text == null)
                        text = getText(bp);
                    continue;
                }
                else if (bp.isMimeType("text/html"))
                {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                }
                else
                {
                    return getText(bp);
                }
            }
            return text;
        }
        else if (p.isMimeType("multipart/*"))
        {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++)
            {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }

    // handling Simple texts and multipart messages
    public void handleMessage(Message message) throws IOException, MessagingException
    {
        Object content = message.getContent();
        if (content instanceof String)
        {
            // handle string
            // LOGGER.info( "Description: " + content.toString());
        }
        else if (content instanceof Multipart)
        {
            Multipart mp = (Multipart) content;
            handleMultipart(mp);
        }
    }

    public void handleMultipart(Multipart mp) throws IOException, MessagingException
    {
        int count = mp.getCount();
        for (int i = 0; i < count; i++)
        {
            BodyPart bp = mp.getBodyPart(i);
            Object content = bp.getContent();
            if (content instanceof String)
            {
                // handle string
                LOGGER.info("Description: " + content.toString());

            }
            else if (content instanceof InputStream)
            {
                // handle input stream
                try
                {
                    // Assume a obj is an instance of com.sun.mail.util.BASE64DecoderStream.
                    String messageString = null;
                    if (content instanceof InputStream)
                    {
                        InputStream is = (InputStream) content;
                        PdfReader pdfReader = new PdfReader(is);

                        LOGGER.info("ENCODED_ID Field: " + EncryptionUtil.decrypt(pdfReader.getAcroFields().getField(PdfUtil.ENCODED_ID), EncryptionUtil.KEY));
                        if (EncryptionUtil.decrypt(pdfReader.getAcroFields().getField(PdfUtil.ENCODED_ID), EncryptionUtil.KEY) != null)
                        {
                            LOGGER.info("Booking ID: " + EncryptionUtil.decrypt(pdfReader.getAcroFields().getField(PdfUtil.ENCODED_ID), EncryptionUtil.KEY) + "Found!!");
                            EntityManager em = EMF.get().createEntityManager();
                            try
                            {
                                Booking booking = em.find(Booking.class, Long.parseLong(EncryptionUtil.decrypt(pdfReader.getAcroFields().getField(PdfUtil.ENCODED_ID), EncryptionUtil.KEY)));
                                if (booking != null)
                                {
                                    booking.setOrderStatus("CONFIRMATION_RECEIVED");
                                    em.persist(booking);
                                    LOGGER.info("Booking(" + EncryptionUtil.decrypt(pdfReader.getAcroFields().getField(PdfUtil.ENCODED_ID), EncryptionUtil.KEY) + ") order status set to 'CONFIRMATION_RECEIVED'");
                                }
                                else
                                {
                                    LOGGER.info("Booking(" + EncryptionUtil.decrypt(pdfReader.getAcroFields().getField(PdfUtil.ENCODED_ID), EncryptionUtil.KEY) + ") not found");
                                }
                            }
                            catch (Exception e)
                            {
                                LOGGER.info("Error Occured: " + e.getStackTrace().toString());
                            }
                            finally
                            {
                                em.close();
                            }

                        }
                        LOGGER.info("Taxi Number: " + pdfReader.getAcroFields().getField("NUMTAXI").toString());
                        messageString = IOUtils.toString(is, "UTF-8");
                        LOGGER.info("Multipart Message: " + messageString);
                    }
                }
                catch (Exception e)
                {
                    LOGGER.info("Exception while reading file: " + e.getStackTrace().toString());
                }
            }
            else if (content instanceof Message)
            {
                Message message = (Message) content;
                handleMessage(message);
            }
            else if (content instanceof Multipart)
            {
                Multipart mp2 = (Multipart) content;
                handleMultipart(mp2);
            }
        }
    }
}
