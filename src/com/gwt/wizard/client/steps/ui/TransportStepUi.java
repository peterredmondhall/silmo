package com.gwt.wizard.client.steps.ui;

import static com.gwt.wizard.client.steps.ui.StepUtil.initPaxBox;
import static com.gwt.wizard.client.steps.ui.StepUtil.initTimeBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.PlaceInfo.City;

public class TransportStepUi extends Composite implements Showable
{
    public static final String OTHER = "andere Abholadresse...";

    public enum PassengerDetail
    {
        TOT_PASS,
        PASS_WITH_ROLLATOR,
        PASS_WITH_FOLDABLE,
        PASS_REQ_WHEELCHAIR
    }

    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);
    private final BookingServiceAsync bookingService = GWT.create(BookingService.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, TransportStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;
    @UiField
    DateBox dateBox;
    @UiField
    ListBox forwardPickupBox;
    @UiField
    ListBox forwardTimeBox;
    @UiField
    ListBox returnTimeBox;
    @UiField
    ListBox returnPickupBox;

    @UiField
    SuggestBox forwardPickupSuggestionBox, returnPickupSuggestionBox;

    @UiField
    RadioButton rb_oneway, rb_return;

    @UiField
    Label errMsg, labelReturnPickup, labelReturnTime;

    @UiField
    Label forwardPickupSuggestionLabel, returnPickupSuggestionLabel;

    boolean withReturn = true;

    @UiField
    ListBox totalPassengersBox,
            passengersWithRollatorBox,
            passengersWithFoldableWheelchairBox,
            passengersWithheelchairTransportBox;

    Map<PassengerDetail, ListBox> listBoxMap = new HashMap<PassengerDetail, ListBox>();

    private List<PlaceInfo> placeList;

    public PlaceInfo getForwardPickup()
    {
        return placeList.get(forwardPickupBox.getSelectedIndex());
    }

    public SuggestBox getForwardSuggestionBox()
    {
        return forwardPickupSuggestionBox;
    }

    public SuggestBox getReturnSuggestionBox()
    {
        return returnPickupSuggestionBox;
    }

    public ListBox getForwardTimeBox()
    {
        return forwardTimeBox;
    }

    public ListBox getReturnTimeBox()
    {
        return returnTimeBox;
    }

    public PlaceInfo getReturnPickup()
    {
        return placeList.get(returnPickupBox.getSelectedIndex());
    }

    public TransportStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        // createMap();
        setupElementIds();
        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
        initTimeBox(forwardTimeBox);
        initTimeBox(returnTimeBox);

        bookingService.getPlaceList(new AsyncCallback<List<PlaceInfo>>()
        {

            @Override
            public void onSuccess(List<PlaceInfo> result)
            {
                placeList = result;
                placeList.add(new PlaceInfo(City.AUGSBURG.name(), OTHER));
                if (placeList != null && placeList.size() > 0)
                {
                    for (PlaceInfo place : result)
                    {
                        forwardPickupBox.addItem(place.getPlace());
                        returnPickupBox.addItem(place.getPlace());
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to fetch places from server!");
            }
        });

        forwardPickupBox.addChangeHandler(getHandler(forwardPickupBox, forwardPickupSuggestionLabel, forwardPickupSuggestionBox));
        returnPickupBox.addChangeHandler(getHandler(returnPickupBox, returnPickupSuggestionLabel, returnPickupSuggestionBox));

        forwardPickupSuggestionLabel.setVisible(false);
        forwardPickupSuggestionBox.setVisible(false);
        returnPickupSuggestionLabel.setVisible(false);
        returnPickupSuggestionBox.setVisible(false);

        listBoxMap.put(PassengerDetail.TOT_PASS, totalPassengersBox);
        listBoxMap.put(PassengerDetail.PASS_WITH_ROLLATOR, passengersWithRollatorBox);
        listBoxMap.put(PassengerDetail.PASS_WITH_FOLDABLE, passengersWithFoldableWheelchairBox);
        listBoxMap.put(PassengerDetail.PASS_REQ_WHEELCHAIR, passengersWithheelchairTransportBox);

        for (PassengerDetail pdDetail : PassengerDetail.values())
        {
            initPaxBox(listBoxMap.get(pdDetail));
        }
        ClickHandler handler = new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                withReturn = !withReturn;
                update();
            }
        };
        rb_return.addClickHandler(handler);
        rb_oneway.addClickHandler(handler);
        update();
    }

    private void setupElementIds()
    {
        dateBox.ensureDebugId("transport_date_id");
        forwardPickupBox.ensureDebugId("transport_forward_pickup_id");
        forwardTimeBox.ensureDebugId("transport_forward_time_id");
        returnTimeBox.ensureDebugId("transport_return_time_id");
        returnPickupBox.ensureDebugId("transport_return_pickup_id");
        rb_oneway.ensureDebugId("transport_rb_oneway_id");
        rb_return.ensureDebugId("transport_rb_return_id");
        totalPassengersBox.ensureDebugId("transport_pax_id");
        passengersWithRollatorBox.ensureDebugId("transport_pax_rollator_id");
        passengersWithFoldableWheelchairBox.ensureDebugId("transport_pax_foldable_id");
        passengersWithheelchairTransportBox.ensureDebugId("transport_pax_wheelchair_id");

    }

    private void update()
    {
        rb_return.setValue(withReturn);
        rb_oneway.setValue(!withReturn);
        labelReturnPickup.setVisible(withReturn);
        labelReturnTime.setVisible(withReturn);
        returnPickupBox.setVisible(withReturn);
        returnTimeBox.setVisible(withReturn);

    }

    public boolean getReturn()
    {
        return withReturn;
    }

    public DateBox getDateBox()
    {
        return dateBox;
    }

    public Integer getPassengerDetail(PassengerDetail passengerDetail)
    {
        String selection = listBoxMap.get(passengerDetail).getItemText(listBoxMap.get(passengerDetail).getSelectedIndex());
        return Integer.parseInt(selection);
    }

    public Label getErrMsg()
    {
        return errMsg;
    }

    @Override
    public void setVisible(boolean visible)
    {
        mainPanel.setVisible(visible);
        // mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
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

    @Override
    public void show(boolean visible)
    {
        // TODO Auto-generated method stub

    }

    public boolean getWithReturn()
    {
        return withReturn;
    }

    private ChangeHandler getHandler(final ListBox listBox, final Label label, final SuggestBox suggestBox)
    {
        return new ChangeHandler()
        {

            @Override
            public void onChange(ChangeEvent event)
            {
                int index = listBox.getSelectedIndex();
                boolean showVariable = (OTHER.equals(listBox.getItemText(index)));
                label.setVisible(showVariable);
                suggestBox.setVisible(showVariable);
            }
        };
    }

}
