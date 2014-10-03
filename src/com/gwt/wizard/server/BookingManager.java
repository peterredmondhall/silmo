package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Place;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookingManager
{
    private static final Logger logger = Logger.getLogger(BookingManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public Boolean saveBooking(BookingInfo bookingInfo, PlaceInfo forwardPickupPlaceInfo, PlaceInfo returnPickupPlaceInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        boolean success = false;
        logger.info(bookingInfo.getDate());
        try
        {
            em.getTransaction().begin();

            Booking booking = Booking.getBooking(bookingInfo, forwardPickupPlaceInfo.getId(), returnPickupPlaceInfo.getId());
            em.persist(booking);
            em.getTransaction().commit();
            success = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
        finally
        {
            em.close();
        }
        return success;
    }

    public List<BookingInfo> getBookings() throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<BookingInfo> bookings = new ArrayList<>();
        try
        {
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery("select t from Booking t").getResultList();
            for (Booking booking : resultList)
            {
                PlaceInfo forwardPlaceInfo = em.find(Place.class, booking.getForwardPickupPlace()).getInfo();
                PlaceInfo returnPlaceInfo = em.find(Place.class, booking.getReturnPickupPlace()).getInfo();
                BookingInfo bookingInfo = booking.getBookingInfo(forwardPlaceInfo, returnPlaceInfo);
                bookings.add(bookingInfo);
            }
        }
        finally
        {
            em.close();
        }
        return bookings;
    }
}
