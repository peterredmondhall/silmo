package com.gwt.wizard.server;

import java.util.List;
import java.util.Map;

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

    private final static BookingManager bookingManager = new BookingManager();
    private final static PlaceManager placeManager = new PlaceManager();

    @Override
    public Boolean saveBooking(BookingInfo bookingInfo, boolean mailing) throws IllegalArgumentException
    {
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
        if (success && mailing)
        {
            Mailer.send(bookingInfo);
        }
        else
        {
            // TODO
            // Mailer.error(bookingInfo);
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
