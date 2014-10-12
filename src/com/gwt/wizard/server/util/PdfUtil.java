package com.gwt.wizard.server.util;

import java.io.FileInputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.gwt.wizard.shared.model.BookingInfo;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfUtil
{
    private final Logger log = Logger.getLogger("PdfUtil");

    public final static String ENCODED_ID = "ENCODED_ID";
    public final static String UUID = "UUID";
    public final static String DATE = "DATE";
    public final static String ROUTE = "ROUTE";
    public final static String NAME = "NAME";
    public final static String NUMTAXI = "NUMTAXI";
    public final static String VEHICLETYPE = "VEHICLETYPE";
    public final static String FORWARDTIME = "FORWARDTIME";
    public final static String RETURNTIME = "RETURNTIME";
    public final static String FORWARD1 = "FORWARD1";
    public final static String FORWARD2 = "FORWARD2";
    public final static String FORWARD3 = "FORWARD3";

    public final static String RETURN1 = "RETURN1";
    public final static String RETURN2 = "RETURN2";
    public final static String RETURN3 = "RETURN3";
    public final static String PASSNAME = "PASSNAME";

    public final static String RPUP = "RPUP";
    public final static String RPUT = "RPUT";
    public final static String REF = "REF";
    public final static String WR = "WR";
    public final static String COMPNAME = "COMPNAME";
    public final static String COMPEMAIL = "COMPEMAIL";
    public final static String ORGNAME = "ORGNAME";
    public final static String ORGEMAIL = "ORGEMAIL";
    public final static String PAX = "PAX";
    public final static String PAXROLLATRON = "PAXROLLATRON";
    public final static String PAXFCWC = "PAXFCWC";
    public final static String PAXROLLSTHUL = "PAXROLLSTHUL";
    public final static String REQUIREMENTS = "REQUIREMENTS";

    @SuppressWarnings("resource")
    public byte[] generateTaxiOrder(BookingInfo bookingInfo)
    {
        PdfReader reader;
        final FileInputStream fis;
        try
        {
            fis = bookingInfo.isWithReturn() ? new FileInputStream("template/Taxi_with_fields.pdf") : new FileInputStream("template/TaxiOhneReturn.pdf");
            reader = new PdfReader(IOUtils.toByteArray(fis));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            AcroFields form = stamper.getAcroFields();
            form.setField(NUMTAXI, "" + bookingInfo.getNumTaxis());
            form.setField(DATE, bookingInfo.getDate());
            form.setField(VEHICLETYPE, "Kombi");
            form.setField(FORWARDTIME, bookingInfo.getForwardPickupTime());
            form.setField(FORWARD1, bookingInfo.getForwardPickupPlace().getPickup());
            form.setField(FORWARD2, bookingInfo.getForwardPickupPlace().getPickup());
            form.setField(PASSNAME, bookingInfo.getCompanionName());
            if (bookingInfo.isWithReturn())
            {
                form.setField(RETURNTIME, bookingInfo.getReturnPickupTime());
                form.setField(RETURN1, bookingInfo.getReturnPickupPlace().getPlace());
                form.setField(RETURN2, bookingInfo.getReturnPickupPlace().getPickup());
            }

            // barcode start
            // CODE 128
            Long orderId = bookingInfo.getId();
            BarcodeQRCode codeOR = new BarcodeQRCode(Long.toString(orderId), 100, 100, null);
            // codeOR.setCode(Long.toString(orderId));
            // code128.setCode("0123456789\uffffbestellung");
            // codeOR.setCodeType(Barcode.CODE128_RAW);
            PdfContentByte contentbyte = stamper.getOverContent(1);
            Image image = codeOR.getImage();
            image.setAbsolutePosition(0, 0);

            contentbyte.addImage(image);

            // barcode finish

            stamper.close();
            reader.close();
            fis.close();

            return out.toByteArray();
        }
        catch (Exception e)
        {
            String msg = "error generating taxiorder";
            Mailer.sendError(msg);
            log.severe(msg);
            e.printStackTrace();
        }
        return null;
    }

    public byte[] generateFahrtenscheck(BookingInfo bookingInfo)
    {
        try
        {
            Document doc = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfSmartCopy copy = new PdfSmartCopy(doc, bos);
            doc.open();

            PdfReader reader;
            PdfStamper stamper;
            AcroFields form;
            ByteArrayOutputStream baos;

            for (int i = 0; i < bookingInfo.getNumTaxis() * 2; i++)
            {
                boolean forward = i < bookingInfo.getNumTaxis();
                byte[] input = IOUtils.toByteArray(new FileInputStream("template/Fahrtenscheck_with_fields.pdf"));
                reader = new PdfReader(input);
                baos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, baos);
                form = stamper.getAcroFields();
                // methods to fill forms
                form.setField(DATE, bookingInfo.getDate());
                String route = bookingInfo.getForwardPickupPlace().getPlace() + " -> " + bookingInfo.getReturnPickupPlace().getPlace();
                if (!forward)
                    route = bookingInfo.getReturnPickupPlace().getPlace() + " -> " + bookingInfo.getForwardPickupPlace().getPlace();

                form.setField(ROUTE, route);
                form.setField(NAME, bookingInfo.getCompanionName());

                stamper.setFormFlattening(true);
                stamper.close();

                reader = new PdfReader(baos.toByteArray());
                copy.addPage(copy.getImportedPage(reader, 1));
            }

            doc.close();
            return bos.toByteArray();
        }
        catch (Exception e)
        {
            String msg = "error generating schecks";
            Mailer.sendError(msg);
            log.severe(msg);
            e.printStackTrace();
        }
        return null;
    }

}
