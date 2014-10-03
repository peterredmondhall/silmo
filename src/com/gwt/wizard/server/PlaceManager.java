package com.gwt.wizard.server;

import static com.google.gwt.thirdparty.guava.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.server.entity.Place;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.PlaceInfo;

/**
 * The server-side implementation of the RPC service.
 */
public class PlaceManager
{
    private static final Logger logger = Logger.getLogger(PlaceManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public Map<String, Place> getPlacesAsMap()
    {
        EntityManager em = getEntityManager();
        Map<String, Place> places = new HashMap<String, Place>();
        @SuppressWarnings("unchecked")
        List<Place> listPlaces = em.createQuery("select t from Place t").getResultList();
        for (Place place : listPlaces)
        {
            places.put(place.getPlace(), place);
        }
        return places;
    }

    public List<PlaceInfo> getPlaceList()
    {
        EntityManager em = getEntityManager();
        List<PlaceInfo> placeList = newArrayList();
        try
        {
            @SuppressWarnings("unchecked")
            List<Place> resultList = em.createQuery("select t from Place t").getResultList();
            for (Place place : resultList)
            {
                placeList.add(place.getInfo());
            }
        }
        finally
        {
            em.close();
        }

        return placeList;
    }

    public PlaceInfo addPlace(PlaceInfo placeInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Place place = Place.getPlace(placeInfo);
            em.persist(place);
            em.flush();
            placeInfo = place.getInfo();
            em.getTransaction().commit();
        }
        catch (Exception ex)
        {
            placeInfo = null;
            logger.severe("adding placeInfo");
        }
        finally
        {
            em.close();
        }
        return placeInfo;

    }

    public Map<String, PlaceInfo> getPlacesForName() throws IllegalArgumentException
    {
        Map<String, PlaceInfo> map = new HashMap<String, PlaceInfo>();
        for (PlaceInfo placeInfo : getPlaces())
        {
            map.put(placeInfo.getPickup(), placeInfo);
        }
        return map;
    }

    public Map<Long, PlaceInfo> getPlacesForId() throws IllegalArgumentException
    {
        Map<Long, PlaceInfo> map = new HashMap<Long, PlaceInfo>();
        for (PlaceInfo placeInfo : getPlaces())
        {
            map.put(placeInfo.getId(), placeInfo);
        }
        return map;
    }

    public List<PlaceInfo> getPlaces() throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<PlaceInfo> places = new ArrayList<>();
        try
        {
            @SuppressWarnings("unchecked")
            List<Place> resultList = em.createQuery("select t from Place t").getResultList();
            for (Place place : resultList)
            {
                places.add(place.getInfo());
            }
        }
        finally
        {
            em.close();
        }

        return places;
    }

    public PlaceInfo savePlace(PlaceInfo placeInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Place place = Place.getPlace(placeInfo);
            em.persist(place);
            em.flush();
            em.getTransaction().commit();
            placeInfo = place.getInfo();

        }
        catch (Exception e)
        {
            placeInfo = null;
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return placeInfo;
    }

    public Boolean deletePlace(Long id) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        try
        {
            try
            {
                Place place = em.find(Place.class, id);
                em.remove(place);
            }
            finally
            {
                em.close();
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean editPlace(Long id, PlaceInfo placeInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        try
        {
            try
            {
                Place place = em.find(Place.class, id);
                place.setCity(placeInfo.getCity());
                place.setPickup(placeInfo.getPickup());
                place.setPlace(placeInfo.getPlace());
                em.persist(place);
            }
            finally
            {
                em.close();
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public PlaceInfo getPlace(Long id)
    {
        EntityManager em = getEntityManager();
        return em.find(PlaceInfo.class, id);
    }
}
