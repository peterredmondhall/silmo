package com.gwt.wizard.server;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.server.entity.User;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

public class BookingServiceImpl extends RemoteServiceServlet implements
        BookingService
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(BookingManager.class.getName());

    private final static BookingManager bookingManager = new BookingManager();
    private final static PlaceManager placeManager = new PlaceManager();

    @Override
    public Boolean saveBooking(BookingInfo bookingInfo, boolean mailing) throws IllegalArgumentException
    {
        logger.info(bookingInfo.getDate() + " mailing=" + mailing);
        PlaceInfo forwardPlaceInfo = bookingInfo.getForwardPickupPlace();
        if (forwardPlaceInfo.getId() == null)
        {
            forwardPlaceInfo = placeManager.addPlace(forwardPlaceInfo);
        }
        PlaceInfo returnPlaceInfo = bookingInfo.getReturnPickupPlace();
        if (returnPlaceInfo.getId() == null)
        {
            returnPlaceInfo = placeManager.addPlace(returnPlaceInfo);
        }

        boolean success = bookingManager.saveBooking(bookingInfo, forwardPlaceInfo, returnPlaceInfo);
        if (mailing)
        {
            if (success)
            {
                Mailer.sendConfirmation(bookingInfo);
            }
            else
            {
                Mailer.sendError("error saving " + bookingInfo.getId());
            }
        }
        return success;
    }

    @Override
    public List<BookingInfo> getBookings() throws IllegalArgumentException
    {
        return bookingManager.getBookings();
    }

    @Override
    public List<PlaceInfo> getPlaceList()
    {
        List<PlaceInfo> result = placeManager.getPlaceList();
        if (result.size() == 0)
        {
            placeManager.loadPlaces(new File("data/places.txt"));
        }
        return placeManager.getPlaceList();
    }

    @Override
    public List<PlaceInfo> getPlaces() throws IllegalArgumentException
    {
        return placeManager.getPlaces();
    }

    @Override
    public PlaceInfo savePlace(PlaceInfo placeInfo) throws IllegalArgumentException
    {
        return placeManager.savePlace(placeInfo);
    }

    @Override
    public Boolean deletePlace(Long id) throws IllegalArgumentException
    {
        return placeManager.deletePlace(id);
    }

    @Override
    public Boolean editPlace(Long id, PlaceInfo placeInfo) throws IllegalArgumentException
    {
        return placeManager.editPlace(id, placeInfo);
    }

    private User getUserFromSession()
    {
        Object obj = getThreadLocalRequest().getSession().getAttribute("user");
        if (obj == null)
        {
            return null;
        }
        return (User) obj;
    }

    @Override
    public Boolean getUser() throws IllegalArgumentException
    {
        User user = getUserFromSession();
        // load user devices
        if (user == null)
        {
            return false;
        }
        return true;
    }

    public Map<Long, PlaceInfo> getPlacesForId()
    {
        return placeManager.getPlacesForId();
    }

    @Override
    public PlaceInfo getPlace(Long id)
    {
        return placeManager.getPlace(id);
    }
}
