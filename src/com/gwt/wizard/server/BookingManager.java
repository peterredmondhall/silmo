package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Config;
import com.gwt.wizard.server.entity.Place;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.ProfilInfo;

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
            int numBookings = getBookings().size();
            Booking booking = Booking.getBooking(numBookings, bookingInfo, forwardPickupPlaceInfo.getId(), returnPickupPlaceInfo.getId());
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

    public ProfilInfo getProfil()
    {
        EntityManager em = getEntityManager();
        Profil profil = null;
        try
        {
            Config config = null;
            List<Config> configList = em.createQuery("select t from Config t").getResultList();
            if (configList.size() == 0)
            {
                em.getTransaction().begin();
                config = new Config();
                config.setProfil("test");
                em.persist(config);
                em.getTransaction().commit();
            }
            else
            {
                config = configList.get(0);
            }
            logger.info("Using config profil:" + config.getProfil());

            List<Profil> profilList = em.createQuery("select t from Profil t where name ='" + config.getProfil() + "'").getResultList();

            if (profilList.size() == 0)
            {
                em.getTransaction().begin();
                profil = Profil.getDefault();
                em.persist(profil);
                em.getTransaction().commit();
                em.detach(profil);
            }
            else
            {
                profil = profilList.get(0);
                logger.info("Using config profil:" + profil.getName());
            }
        }
        finally
        {
            em.close();
        }
        return profil.getInfo();
    }

}
