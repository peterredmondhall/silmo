package com.gwt.wizard.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.PlaceInfo.City;

public class BookingManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    BookingManager bm = new BookingManager();
    PlaceManager pm = new PlaceManager();

    PlaceInfo forwardPlaceInfo;
    PlaceInfo returnPlaceIdInfo;

    @Before
    public void setUp()
    {
        helper.setUp();
        forwardPlaceInfo = pm.savePlace(new PlaceInfo(City.AUGSBURG.name(), "forward"));
        returnPlaceIdInfo = pm.savePlace(new PlaceInfo(City.AUGSBURG.name(), "return"));
    }

    @After
    public void tearDown()
    {
        pm.deletePlace(forwardPlaceInfo.getId());
        pm.deletePlace(returnPlaceIdInfo.getId());

        helper.tearDown();
    }

    private BookingInfo standardBookingInfo()
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setDate("21.01.1999");
        bookingInfo.setForwardPickupPlace(new PlaceInfo(City.AUGSBURG.name(), "forward"));
        bookingInfo.setReturnPickupPlace(new PlaceInfo(City.AUGSBURG.name(), "return"));
        return bookingInfo;
    }

    @Test
    public void should_create_a_booking_using_known_places()
    {
        // TODO
    }

}
