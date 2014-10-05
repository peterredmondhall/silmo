package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.ProfilInfo;

@Entity
public class Profil implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private String taxiFax;

    private static final String TEST_FAX = "004932224063613";

    public static Profil getDefault()
    {
        Profil profil = new Profil();
        profil.setTaxiFax(TEST_FAX);
        return profil;
    }

    public ProfilInfo getInfo()
    {
        ProfilInfo info = new ProfilInfo();
        info.setTaxiFax(taxiFax);
        return info;
    }

    public String getTaxiFax()
    {
        return taxiFax;
    }

    public void setTaxiFax(String taxiFax)
    {
        this.taxiFax = taxiFax;
    }

}
