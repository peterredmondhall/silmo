package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.shared.model.BookingInfo;

public class SummaryStepUi extends Composite implements Showable
{
    private final BookingInfo bookingInfo;

    private static SummaryStepUiUiBinder uiBinder = GWT.create(SummaryStepUiUiBinder.class);

    interface SummaryStepUiUiBinder extends UiBinder<Widget, SummaryStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    Label labelDate, labelForwardPickupPlace, labelForwardPickupTime, labelReturnPickupPlace, labelReturnPickupTime;
    @UiField
    Label labelPax, labelPaxWithRollatoren, labelPaxWithFoldableWheelchair, labelPaxWithWheelchair;
    @UiField
    Label labelOrganizerName, labelOrganizerEmail, labelCompanionName, labelCompanionEmail;

    @UiField
    Label labelRPP, labelRPT;

    @UiField
    Label labelRequirements;

    public SummaryStepUi(BookingInfo bookingInfo)
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        this.bookingInfo = bookingInfo;
    }

    @Override
    public void show(boolean visible)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
        labelDate.setText(bookingInfo.getDate());
        labelForwardPickupPlace.setText(bookingInfo.getForwardPickupPlace().getPlace());
        labelForwardPickupTime.setText(bookingInfo.getForwardPickupTime());

        labelReturnPickupPlace.setText(bookingInfo.getReturnPickupPlace().getPlace());
        labelReturnPickupTime.setText(bookingInfo.getReturnPickupTime());

//        for (Widget w : new Widget[] { rowRPP, rowRPT })
//        {
//            w.setVisible(bookingInfo.isWithReturn());
//        }

        labelPax.setText("" + bookingInfo.getPax());
        labelPaxWithRollatoren.setText("" + bookingInfo.getPaxRollatoren());
        labelPaxWithFoldableWheelchair.setText("" + bookingInfo.getPaxFoldableWheelchair());
        labelPaxWithWheelchair.setText("" + bookingInfo.getPaxRollstuhl());

        labelOrganizerName.setText(bookingInfo.getOrganizerName());
        labelOrganizerEmail.setText(bookingInfo.getOrganizerEmail());
        labelCompanionName.setText(bookingInfo.getCompanionName());
        labelCompanionEmail.setText(bookingInfo.getCompanionEmail());

        labelRequirements.setText(bookingInfo.getRequirements());

        setReturnVisible(bookingInfo.isWithReturn());

    }

    private void setReturnVisible(boolean visible)
    {
        labelReturnPickupPlace.setVisible(visible);
        labelReturnPickupTime.setVisible(visible);
        labelRPT.setVisible(visible);
        labelRPP.setVisible(visible);
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
