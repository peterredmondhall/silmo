package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BookingInfo implements IsSerializable
{
    public enum TaxiBookingStatus
    {
        INACTIVE,
        AWAITING_CONFIRMATION,
        CONFIRMED
    }

    private Long id;
    private String date;
    private boolean withReturn;
    private PlaceInfo forwardPickupPlace;
    private String reference;

    private String forwardPickupTime;
    private PlaceInfo returnPickupPlace;
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
    private int numTaxis;
    TaxiBookingStatus taxiBookingStatus;

    public TaxiBookingStatus getTaxiBookingStatus()
    {
        return taxiBookingStatus;
    }

    public void setTaxiBookingStatus(TaxiBookingStatus taxiBookingStatus)
    {
        this.taxiBookingStatus = taxiBookingStatus;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public boolean isWithReturn()
    {
        return withReturn;
    }

    public void setWithReturn(boolean withReturn)
    {
        this.withReturn = withReturn;
    }

    public PlaceInfo getForwardPickupPlace()
    {
        return forwardPickupPlace;
    }

    public void setForwardPickupPlace(PlaceInfo forwardPickupPlace)
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

    public PlaceInfo getReturnPickupPlace()
    {
        return returnPickupPlace;
    }

    public void setReturnPickupPlace(PlaceInfo returnPickupPlace)
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

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getCompanionName()
    {
        return begleiterName;
    }

    public void setCompanionName(String begleiterName)
    {
        this.begleiterName = begleiterName;
    }

    public String getCompanionEmail()
    {
        return begleiterEmail;
    }

    public void setCompanionEmail(String begleiterEmail)
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

    public int getPaxFoldableWheelchair()
    {
        return paxFolcableWheelchair;
    }

    public void setPaxFoldableWheelchair(int paxFolcableWheelchair)
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

    public int getNumTaxis()
    {
        return numTaxis;
    }

    public void setNumTaxis(int numTaxis)
    {
        this.numTaxis = numTaxis;
    }

}
