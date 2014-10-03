package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;

public class ContactStepUi extends Composite implements Showable
{

    private static ContactStepUiUiBinder uiBinder = GWT.create(ContactStepUiUiBinder.class);

    interface ContactStepUiUiBinder extends UiBinder<Widget, ContactStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    TextBox organizerName, organizerEmail;

    @UiField
    Label organizerNameErrorMsg, organizerEmailErrorMsg;

    @UiField
    TextBox companionName, companionEmail;
    @UiField
    Label companionNameErrorMsg, companionEmailErrorMsg;

    public ContactStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
    }

    public String getOrganizerName()
    {
        return organizerName.getValue();
    }

    public String getOrganizerEmail()
    {
        return organizerEmail.getValue();
    }

    public String getCompanionName()
    {
        return companionName.getValue();
    }

    public String getCompanionEmail()
    {
        return companionEmail.getValue();
    }

    public void setOrganizerNameErrorMsg(String msg)
    {
        organizerNameErrorMsg.setText(msg);
    }

    public void setOrganizerEmailErrorMsg(String msg)
    {
        organizerEmailErrorMsg.setText(msg);
    }

    public void setCompanionNameErrorMsg(String msg)
    {
        companionNameErrorMsg.setText(msg);
    }

    public void setCompanionEmailErrorMsg(String msg)
    {
        companionEmailErrorMsg.setText(msg);
    }

    @Override
    public void show(boolean visible)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
    }

    @Override
    public void setHeight(String height)
    {
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width)
    {
        super.setWidth(width);
    }
}
