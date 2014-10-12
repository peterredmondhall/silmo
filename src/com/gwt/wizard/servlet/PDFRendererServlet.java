package com.gwt.wizard.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.server.BookingManager;
import com.gwt.wizard.server.BookingServiceImpl;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.server.util.PdfUtil;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.BookingInfo.TaxiBookingStatus;
import com.gwt.wizard.shared.model.ProfilInfo;

public class PDFRendererServlet extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    BookingServiceImpl bookingService = new BookingServiceImpl();
    BookingManager bookingManager = new BookingManager();
    PdfUtil pdfUtil = new PdfUtil();
    private final Logger LOGGER = Logger.getLogger("PDFRendererServlet");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String key = req.getParameter("key");
        String generate = req.getParameter("generate");
        String action = req.getParameter("action");

        byte[] bytes = null;
        BookingInfo bookingInfo = bookingManager.getBookingInfo(Long.parseLong(key));
        try
        {
            if (bookingInfo != null)
            {
                if ("taxiorder".equals(generate))
                {
                    bytes = pdfUtil.generateTaxiOrder(bookingInfo);
                }
                else
                {
                    bytes = pdfUtil.generateFahrtenscheck(bookingInfo);
                }

                if (action != null && "sendOrderTaxiEmail".equals(action))
                {
                    ProfilInfo profilInfo = bookingManager.getProfil();
                    LOGGER.info("faxEmailAddress:" + profilInfo.getTaxiFax());
                    if (Mailer.send(profilInfo.getTaxiFax(), "Bestellung", bytes))
                    {
                        try
                        {
                            bookingManager.setTaxiBookingStatus(bookingInfo.getId(), TaxiBookingStatus.AWAITING_CONFIRMATION);
                            LOGGER.info("Booking order status set to 'AWAITING_CONFIRMATION' successfully");
                        }
                        catch (Exception e)
                        {
                            LOGGER.info("Error occured: " + e.getStackTrace().toString());
                        }
                    }
                    resp.getWriter().write("Taxi Order with Booking id: " + key + " is emailed");
                }
                else if ("sendChecks".equals(action))
                {

                    {
                        Mailer.sendChecks(bookingInfo);
                    }
                }
                else
                {
                    resp.setContentType("application/pdf");
                    resp.addHeader("Content-Disposition", "inline; filename=\"data.pdf\"");
                    resp.setContentLength(bytes.length);

                    ServletOutputStream sos = resp.getOutputStream();
                    sos.write(bytes);
                    sos.flush();
                    sos.close();
                }
            }
            else
            {
                resp.getWriter().write("Booking with id: " + key + " not fetched!");
            }
        }
        catch (Exception ex)
        {
            String msg = "action:" + action;
            msg += "key:" + req.getParameter("key");
            msg += "generate:" + req.getParameter("generate");
            Mailer.sendError(msg);
            LOGGER.severe(msg);
            resp.getWriter().write("Ein Fehler ist aufgetreten.");
            ex.printStackTrace();
        }
    }

}
