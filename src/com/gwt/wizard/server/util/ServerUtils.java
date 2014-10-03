package com.gwt.wizard.server.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.gwt.wizard.shared.model.BookingInfo;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ServerUtils
{

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random rnd = new Random();

    private static String DATE_FORMATTER = "yyyy-MM-dd";

    public static String generateUUID(int len)
    {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        return sb.toString();

//        return UUID.fromString(UUID.randomUUID().toString()).toString().replace("-", "");
    }

    public static String formate(Date date)
    {
        return new SimpleDateFormat(DATE_FORMATTER).format(date);
    }

    public static byte[] getPdf(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        byte[] pdf = null;
        try
        {
            String uuid = ("R" + generateUUID(9)).toUpperCase();

//            LOGGER.info("--------------------------------- " +getThreadLocalRequest().getRealPath("template/template_sample.pdf"));

            final FileInputStream fis = new FileInputStream("template/template_sample.pdf");

            pdf = manipulatePdf(IOUtils.toByteArray(fis), uuid, bookingInfo);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pdf;
    }

    static byte[] manipulatePdf(byte[] template, String uuid, BookingInfo bookingInfo) throws IOException, DocumentException
    {
        PdfReader reader = new PdfReader(template);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, out);
        AcroFields form = stamper.getAcroFields();
        form.setField("UUID", uuid);
        form.setField("DATE", bookingInfo.getDate());
        form.setField("TIME", bookingInfo.getForwardPickupTime());
        form.setField("PLACE", bookingInfo.getForwardPickupPlace().getPlace());
        stamper.close();
        reader.close();
        return out.toByteArray();
    }

}
