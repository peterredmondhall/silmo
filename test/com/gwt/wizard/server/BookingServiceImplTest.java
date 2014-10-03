package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.PlaceInfo.City;

public class BookingServiceImplTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    BookingServiceImpl service = new BookingServiceImpl();

    PlaceInfo forwardPlaceInfo;
    PlaceInfo returnPlaceIdInfo;

    @Before
    public void setUp()
    {
        helper.setUp();
        forwardPlaceInfo = service.savePlace(new PlaceInfo(City.AUGSBURG.name(), "forward"));
        returnPlaceIdInfo = service.savePlace(new PlaceInfo(City.AUGSBURG.name(), "return"));
    }

    @After
    public void tearDown()
    {
        service.deletePlace(forwardPlaceInfo.getId());
        service.deletePlace(returnPlaceIdInfo.getId());

        helper.tearDown();
    }

    private BookingInfo standardBookingInfo()
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setDate("21.01.1999");
        bookingInfo.setForwardPickupPlace(forwardPlaceInfo);
        bookingInfo.setReturnPickupPlace(returnPlaceIdInfo);
        return bookingInfo;
    }

    @Test
    public void should_create_a_booking_using_known_places()
    {
        BookingInfo bookingInfo = standardBookingInfo();
        bookingInfo.setForwardPickupPlace(forwardPlaceInfo);
        bookingInfo.setReturnPickupPlace(returnPlaceIdInfo);

        boolean success = service.saveBooking(bookingInfo, false);
        assertEquals(true, success);
        List<BookingInfo> bookings = service.getBookings();
        assertEquals(1, bookings.size());
        bookingInfo = bookings.get(0);
        assertEquals("forward", bookingInfo.getForwardPickupPlace().getPlace());
        assertEquals("return", bookingInfo.getReturnPickupPlace().getPlace());

    }

    @Test
    public void should_create_a_booking_using_unknown_places()
    {
        BookingInfo bookingInfo = standardBookingInfo();
        bookingInfo.setForwardPickupPlace(new PlaceInfo(City.AUGSBURG.name(), "unknownForwardPickup"));
        bookingInfo.setReturnPickupPlace(new PlaceInfo(City.AUGSBURG.name(), "unknownReturnPickup"));

        boolean success = service.saveBooking(bookingInfo, false);
        assertEquals(true, success);

        List<BookingInfo> bookings = service.getBookings();
        assertEquals(1, bookings.size());
        bookingInfo = bookings.get(0);
        assertEquals(bookingInfo.getForwardPickupPlace().getPlace(), "unknownForwardPickup");
        assertEquals(bookingInfo.getReturnPickupPlace().getPlace(), "unknownReturnPickup");

    }

}
