package com.gwt.wizard.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.qrcode.GetImagesFromPDF;
import com.gwt.wizard.server.BookingManager;
import com.gwt.wizard.server.BookingServiceImpl;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.server.util.PdfUtil;
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
    private final Logger LOGGER = Logger.getLogger("PDFRendererServlet");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String key = req.getParameter("key");
        String generate = req.getParameter("generate");
        String action = req.getParameter("action");
        if (key != null && key.length() > 0)
        {
            byte[] bytes = null;
            try
            {
                EntityManager em = EMF.get().createEntityManager();
                try
                {
                    Booking booking = em.find(Booking.class, Long.parseLong(key));
                    if (booking != null)
                    {
                        if ("taxiorder".equals(generate))
                        {
                            bytes = PdfUtil.generateTaxiOrder(booking, bookingService.getPlacesForId());
                        }
                        else
                        {
                            bytes = PdfUtil.generateFahrtenscheck(booking, bookingService.getPlacesForId());
                        }

                        if (action != null && "sendOrderTaxiEmail".equals(action))
                        {
                            // test only
                            GetImagesFromPDF.encode(new FileInputStream(new File("taxi_order.pdf")));

                            ProfilInfo profilInfo = bookingManager.getProfil();
                            LOGGER.info("faxEmailAddress:" + profilInfo.getTaxiFax());
                            if (Mailer.send(profilInfo.getTaxiFax(), "bluefax", bytes))
                            {
                                try
                                {
                                    booking.setTaxiBookingStatus(TaxiBookingStatus.AWAITING_CONFIRMATION);
                                    em.persist(booking);
                                    LOGGER.info("Booking order status set to 'AWAITING_CONFIRMATION' successfully");
                                }
                                catch (Exception e)
                                {
                                    LOGGER.info("Error occured: " + e.getStackTrace().toString());
                                }
                            }
                            resp.getWriter().write("Taxi Order with Booking id: " + key + " is emailed");
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
                finally
                {
                    em.close();
                }
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
        // super.doGet(req, resp);
    }
}
