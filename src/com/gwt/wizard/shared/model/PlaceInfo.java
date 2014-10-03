package com.gwt.wizard.shared.model;

import java.io.Serializable;

public class PlaceInfo implements Serializable
{

    private static final long serialVersionUID = 1L;

    public enum City
    {
        AUGSBURG
    };

    public PlaceInfo()
    {

    }

    public PlaceInfo(String city, String place)
    {
        this.city = city;
        this.place = place;
    }

    private Long id;
    private String city;
    private String place;
    private String pickup;
    private String ref;

    public String getRef()
    {
        return ref;
    }

    public void setRef(String ref)
    {
        this.ref = ref;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public String getPickup()
    {
        return pickup;
    }

    public void setPickup(String pickup)
    {
        this.pickup = pickup;
    }
}
