package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.server.util.BookingUtil;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

@Entity
public class Booking implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String date;
    private boolean withReturn;
    private Long forwardPickupPlace;
    private String reference;
    private String forwardPickupTime;
    private Long returnPickupPlace;
    private String returnPickupTime;

    private String begleiterName;
    private String begleiterEmail;
    private String organizerName;
    private String organizerEmail;
    private int pax = 0;
    private int paxRollatoren = 0;
    private int paxFolcableWheelchair = 0;
    private int paxRollstuhl = 0;
    private String requirements = "";
    private Blob pdf;
    private int noTaxi;
    private String orderStatus;

    public Key getKey()
    {
        return key;
    }

    public void setKey(Key key)
    {
        this.key = key;
    }

    public boolean isWithReturn()
    {
        return withReturn;
    }

    public void setWithReturn(boolean withReturn)
    {
        this.withReturn = withReturn;
    }

    public Long getForwardPickupPlace()
    {
        return forwardPickupPlace;
    }

    public void setForwardPickupPlace(Long forwardPickupPlace)
    {
        this.forwardPickupPlace = forwardPickupPlace;
    }

    public String getForwardPickupTime()
    {
        return forwardPickupTime;
    }

    public void setForwardPickupTime(String forwardPickupTime)
    {
        this.forwardPickupTime = forwardPickupTime;
    }

    public Long getReturnPickupPlace()
    {
        return returnPickupPlace;
    }

    public void setReturnPickupPlace(Long returnPickupPlace)
    {
        this.returnPickupPlace = returnPickupPlace;
    }

    public String getReturnPickupTime()
    {
        return returnPickupTime;
    }

    public void setReturnPickupTime(String returnPickupTime)
    {
        this.returnPickupTime = returnPickupTime;
    }

    public String getBegleiterName()
    {
        return begleiterName;
    }

    public void setBegleiterName(String begleiterName)
    {
        this.begleiterName = begleiterName;
    }

    public String getBegleiterEmail()
    {
        return begleiterEmail;
    }

    public void setBegleiterEmail(String begleiterEmail)
    {
        this.begleiterEmail = begleiterEmail;
    }

    public String getOrganizerName()
    {
        return organizerName;
    }

    public void setOrganizerName(String organizerName)
    {
        this.organizerName = organizerName;
    }

    public String getOrganizerEmail()
    {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail)
    {
        this.organizerEmail = organizerEmail;
    }

    public int getPax()
    {
        return pax;
    }

    public void setPax(int pax)
    {
        this.pax = pax;
    }

    public int getPaxRollatoren()
    {
        return paxRollatoren;
    }

    public void setPaxRollatoren(int paxRollatoren)
    {
        this.paxRollatoren = paxRollatoren;
    }

    public int getPaxFolcableWheelchair()
    {
        return paxFolcableWheelchair;
    }

    public void setPaxFolcableWheelchair(int paxFolcableWheelchair)
    {
        this.paxFolcableWheelchair = paxFolcableWheelchair;
    }

    public int getPaxRollstuhl()
    {
        return paxRollstuhl;
    }

    public void setPaxRollstuhl(int paxRollstuhl)
    {
        this.paxRollstuhl = paxRollstuhl;
    }

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public String getReference()
    {
        return reference;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public Blob getPdf()
    {
        return pdf;
    }

    public void setPdf(Blob pdf)
    {
        this.pdf = pdf;
    }

    public static Booking getBooking(BookingInfo bookingInfo, Long forwardPickupPlaceId, Long returnPickupPlaceId)
    {
        Booking booking = new Booking();
        booking.setDate(bookingInfo.getDate());
        booking.setWithReturn(bookingInfo.isWithReturn());
        booking.setForwardPickupPlace(forwardPickupPlaceId);
        booking.setReturnPickupPlace(returnPickupPlaceId);

        booking.setReference(bookingInfo.getReference());
        booking.setForwardPickupTime(bookingInfo.getForwardPickupTime());

        booking.setReturnPickupTime(bookingInfo.getReturnPickupTime());

        booking.setBegleiterName(bookingInfo.getCompanionName());
        booking.setBegleiterEmail(bookingInfo.getCompanionEmail());
        booking.setOrganizerName(bookingInfo.getOrganizerName());
        booking.setOrganizerEmail(bookingInfo.getOrganizerEmail());
        booking.setPax(bookingInfo.getPax());
        booking.setPaxRollatoren(bookingInfo.getPaxRollatoren());
        booking.setPaxFolcableWheelchair(bookingInfo.getPaxFoldableWheelchair());
        booking.setPaxRollstuhl(bookingInfo.getPaxRollstuhl());
        booking.setRequirements(bookingInfo.getRequirements());

        booking.setNoTaxi(BookingUtil.getNumTaxis(booking.getPax(), booking.getPaxRollstuhl()));
        return booking;
    }

    public BookingInfo getBookingInfo(PlaceInfo forwardPlaceInfo, PlaceInfo returnPlaceInfo)
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(getKey().getId());
        bookingInfo.setDate(getDate());
        bookingInfo.setWithReturn(isWithReturn());
        bookingInfo.setForwardPickupPlace(forwardPlaceInfo);
        bookingInfo.setReturnPickupPlace(returnPlaceInfo);

        bookingInfo.setForwardPickupTime(getForwardPickupTime());
        bookingInfo.setReturnPickupTime(getReturnPickupTime());
        bookingInfo.setCompanionName(getBegleiterName());
        bookingInfo.setCompanionEmail(getBegleiterEmail());
        bookingInfo.setOrganizerName(getOrganizerName());
        bookingInfo.setOrganizerEmail(getOrganizerEmail());
        bookingInfo.setPax(getPax());
        bookingInfo.setPaxRollatoren(getPaxRollatoren());
        bookingInfo.setPaxFoldableWheelchair(getPaxFolcableWheelchair());
        bookingInfo.setPaxRollstuhl(getPaxRollstuhl());
        bookingInfo.setNumTaxis(getNumTaxi());
        bookingInfo.setRequirements(getRequirements());
        return bookingInfo;

    }

    public int getNumTaxi()
    {
        return noTaxi;
    }

    public void setNoTaxi(int noTaxi)
    {
        this.noTaxi = noTaxi;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

}