package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.client.util.ReferenceGenerator;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Config;
import com.gwt.wizard.server.entity.Place;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.BookingInfo.TaxiBookingStatus;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.ProfilInfo;

public class BookingManager
{
    private static final Logger logger = Logger.getLogger(BookingManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public void setTaxiBookingStatus(Long id, TaxiBookingStatus taxiBookingStatus)
    {
        EntityManager em = getEntityManager();
        Booking booking = em.find(Booking.class, id);
        booking.setTaxiBookingStatus(taxiBookingStatus);
        em.getTransaction().begin();
        em.persist(booking);
        em.getTransaction().commit();
    }

    public BookingInfo getBookingInfo(Long id)
    {
        EntityManager em = getEntityManager();
        Booking booking = em.find(Booking.class, id);
        Place forwardPlace = em.find(Place.class, booking.getForwardPickupPlace());
        Place returnPlace = em.find(Place.class, booking.getReturnPickupPlace());

        return booking.getBookingInfo(forwardPlace.getInfo(), returnPlace.getInfo());

    }

    public Boolean saveBooking(BookingInfo bookingInfo, PlaceInfo forwardPickupPlaceInfo, PlaceInfo returnPickupPlaceInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        boolean success = false;
        logger.info(bookingInfo.getDate());
        logger.info("forwardPickupPlaceInfo id" + forwardPickupPlaceInfo.getId());
        logger.info("returnPickupPlaceInfo id" + returnPickupPlaceInfo.getId());
        logger.info(bookingInfo.getDate());
        try
        {
            em.getTransaction().begin();
            logger.info("transaction begun");
            String reference = ReferenceGenerator.gen(getBookings().size());
            Booking booking = Booking.getBooking(reference, bookingInfo, forwardPickupPlaceInfo.getId(), returnPickupPlaceInfo.getId());
            logger.info("booking created");
            em.persist(booking);
            logger.info("transaction committing");
            em.getTransaction().commit();
            success = true;
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
            logger.severe("e:" + e);
            e.printStackTrace();
            logger.severe(e.getMessage());
            em.getTransaction().rollback();
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
                Place forwardPickupPlace = em.find(Place.class, booking.getForwardPickupPlace());
                Place returnPickupPlace = em.find(Place.class, booking.getReturnPickupPlace());

                PlaceInfo forwardPlaceInfo = forwardPickupPlace.getInfo();
                PlaceInfo returnPlaceInfo = returnPickupPlace.getInfo();
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
