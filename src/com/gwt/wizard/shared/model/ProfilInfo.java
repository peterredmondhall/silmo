package com.gwt.wizard.shared.model;

import java.io.Serializable;

public class ProfilInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    // public static final String BLUE_FAX = "@gate.blue-fax.com"; // eg "004932224063613@gate.blue-fax.com";

    private String taxiFax;

    public String getTaxiFax()
    {
        return taxiFax;
    }

    public void setTaxiFax(String taxiFax)
    {
        this.taxiFax = taxiFax;
    }
}
