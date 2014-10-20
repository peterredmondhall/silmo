package com.gwt.wizard.server;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.PlaceInfo;

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
    }

    @After
    public void tearDown()
    {

        helper.tearDown();
    }

    @Test
    public void should_get_a_list_of_places_from_file()
    {
        for (PlaceInfo placeInfo : pm.getPlaceList())
        {
            pm.deletePlace(placeInfo.getId());
        }
        pm.loadPlaces(new File("war/data/places.txt"));
    }

}
