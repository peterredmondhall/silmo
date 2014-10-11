package com.gwt.wizard.client.dashboard.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.BookingInfo;

public class BookingManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private static BookingManagementVeiwUiBinder uiBinder = GWT.create(BookingManagementVeiwUiBinder.class);

    interface BookingManagementVeiwUiBinder extends UiBinder<Widget, BookingManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    private final List<BookingInfo> BOOKINGS = new ArrayList<>();

    CellTable<BookingInfo> bookingManagementTable = new CellTable<BookingInfo>(13, tableRes);

    @UiField
    HTMLPanel mainPanel;
    private final SelectionModel<BookingInfo> selectionModel = new MultiSelectionModel<BookingInfo>(null);

    // The list of data to display.

    public BookingManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        fetchBookings();
        Timer timer = new Timer()
        {
            @Override
            public void run()
            {
                // Setting up bookingManagementPanel
                setBookingCellTable();
            }
        };
        // Execute the timer to expire 1/2 seconds in the future
        timer.schedule(500);

    }

    private void fetchBookings()
    {
        service.getBookings(new AsyncCallback<List<BookingInfo>>()
        {

            @Override
            public void onSuccess(List<BookingInfo> result)
            {
                if (result != null && result.size() > 0)
                {
                    for (BookingInfo booking : result)
                    {
                        BOOKINGS.add(booking);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to Server!");
            }
        });
    }

    private void setBookingCellTable()
    {
        bookingManagementTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<BookingInfo>createCheckboxManager());
        // Checkbox
        Column<BookingInfo, Boolean> checkColumn = new Column<BookingInfo, Boolean>(
                new CheckboxCell(true, false))
        {
            @Override
            public Boolean getValue(BookingInfo object)
            {
                return selectionModel.isSelected(object);
            }
        };

        // Create date column.
        TextColumn<BookingInfo> dateColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getDate();
            }
        };

        // Create Time column.
        TextColumn<BookingInfo> withReturnColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.isWithReturn());
            }
        };

        // Create Place column.
        TextColumn<BookingInfo> forwardPickupPlaceColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getForwardPickupPlace().getPlace();
            }
        };

        // Create refrence column.
        TextColumn<BookingInfo> referenceColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getReference();
            }
        };

        // Create forwardPickupTime column.
        TextColumn<BookingInfo> forwardPickupTimeColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getForwardPickupTime();
            }
        };
        // Create returnPickupPlace column.
        TextColumn<BookingInfo> returnPickupPlaceColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getReturnPickupPlace().getPlace();
            }
        };
        // Create returnPickupTime column.
        TextColumn<BookingInfo> returnPickupTimeColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getReturnPickupTime();
            }
        };
        // Create begleiterName column.
        TextColumn<BookingInfo> companionNameColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getCompanionName();
            }
        };
        // Create begleiterEmail column.
        TextColumn<BookingInfo> companionEmailColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getCompanionEmail();
            }
        };
        // Create organizerName column.
        TextColumn<BookingInfo> organizerNameColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getOrganizerName();
            }
        };
        // Create organizerEmail column.
        TextColumn<BookingInfo> organizerEmailColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getOrganizerEmail();
            }
        };
        // Create pax column.
        TextColumn<BookingInfo> paxColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.getPax());
            }
        };
        // Create paxRollatoren column.
        TextColumn<BookingInfo> paxRollatorenColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.getPaxRollatoren());
            }
        };
        // Create paxFolcableWheelchair column.
        TextColumn<BookingInfo> paxFoldableWheelchairColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.getPaxFoldableWheelchair());
            }
        };
        // Create paxRollstuhl column.
        TextColumn<BookingInfo> paxWheelchairColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.getPaxRollstuhl());
            }
        };
        // Create paxRollstuhl column.
        TextColumn<BookingInfo> numTaxiColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.getNumTaxis());
            }
        };
        // Create requirements column.
        TextColumn<BookingInfo> requirementsColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getRequirements();
            }
        };

        // Create requirements column.
        TextColumn<BookingInfo> taxiBookingStatusColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                if (booking.getTaxiBookingStatus() != null)
                {
                    return booking.getTaxiBookingStatus().name();
                }
                else
                {
                    return "error";
                }
            }
        };

        Column<BookingInfo, SafeHtml> viewTaxiOrderPdfColumn = new Column<BookingInfo, SafeHtml>(new SafeHtmlCell())
        {
            @Override
            public SafeHtml getValue(BookingInfo obj)
            {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant("<a href='/gwt_wizard/renderpdf?generate=taxiorder&key=" + obj.getId() + "'" + "target='_blank'>Veiw</a>");
                return sb.toSafeHtml();
            }
        };
        Column<BookingInfo, SafeHtml> viewFahrenschecksPdfColumn = new Column<BookingInfo, SafeHtml>(new SafeHtmlCell())
        {
            @Override
            public SafeHtml getValue(BookingInfo obj)
            {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant("<a href='/gwt_wizard/renderpdf?generate=fahrtenschecks&key=" + obj.getId() + "'" + "target='_blank'>Veiw</a>");
                return sb.toSafeHtml();
            }
        };
        Column<BookingInfo, SafeHtml> orderTaxiColumn = new Column<BookingInfo, SafeHtml>(new SafeHtmlCell())
        {
            @Override
            public SafeHtml getValue(BookingInfo obj)
            {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant("<a href='/gwt_wizard/renderpdf?action=sendOrderTaxiEmail&generate=taxiorder&key=" + obj.getId() + "'" + "target='_blank'>order</a>");
                return sb.toSafeHtml();
            }
        };
        bookingManagementTable.setTableLayoutFixed(true);
        // Add the columns.
        bookingManagementTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        bookingManagementTable.addColumn(dateColumn, "Date");
        bookingManagementTable.addColumn(withReturnColumn, "With Return");
        bookingManagementTable.addColumn(forwardPickupPlaceColumn, "Forward Pickup Place");
        bookingManagementTable.addColumn(referenceColumn, "Reference");
        bookingManagementTable.addColumn(forwardPickupTimeColumn, "Forward Pickup Time");
        bookingManagementTable.addColumn(returnPickupPlaceColumn, "Return Pickup Place");
        bookingManagementTable.addColumn(returnPickupTimeColumn, "Return Pickup Time");
        bookingManagementTable.addColumn(companionNameColumn, "Begleiter Name");
        bookingManagementTable.addColumn(companionEmailColumn, "Begleiter Email");
        bookingManagementTable.addColumn(organizerNameColumn, "Organizer Name");
        bookingManagementTable.addColumn(organizerEmailColumn, "Organizer Email");
        bookingManagementTable.addColumn(paxColumn, "Pax");
        bookingManagementTable.addColumn(paxRollatorenColumn, "Pax Rollatoren");
        bookingManagementTable.addColumn(paxFoldableWheelchairColumn, "Pax Foldable Wheelchair");
        bookingManagementTable.addColumn(paxWheelchairColumn, "Pax Rollstuhl");
        bookingManagementTable.addColumn(numTaxiColumn, "No. taxis");

        bookingManagementTable.addColumn(requirementsColumn, "Requirements");
        bookingManagementTable.addColumn(taxiBookingStatusColumn, "Taxi Booking");

        bookingManagementTable.addColumn(viewTaxiOrderPdfColumn, "View Taxiorder");
        bookingManagementTable.addColumn(viewFahrenschecksPdfColumn, "View Fahrtenschecks");
        bookingManagementTable.addColumn(orderTaxiColumn, "Order Taxi");

        bookingManagementTable.setColumnWidth(checkColumn, 40, Unit.PX);
        bookingManagementTable.setColumnWidth(dateColumn, 65, Unit.PX);
        bookingManagementTable.setColumnWidth(withReturnColumn, 100, Unit.PX);
        bookingManagementTable.setColumnWidth(forwardPickupPlaceColumn, 145, Unit.PX);
        bookingManagementTable.setColumnWidth(referenceColumn, 70, Unit.PX);
        bookingManagementTable.setColumnWidth(forwardPickupTimeColumn, 135, Unit.PX);
        bookingManagementTable.setColumnWidth(returnPickupPlaceColumn, 130, Unit.PX);
        bookingManagementTable.setColumnWidth(returnPickupTimeColumn, 125, Unit.PX);
        bookingManagementTable.setColumnWidth(companionNameColumn, 100, Unit.PX);
        bookingManagementTable.setColumnWidth(companionEmailColumn, 100, Unit.PX);
        bookingManagementTable.setColumnWidth(organizerNameColumn, 105, Unit.PX);
        bookingManagementTable.setColumnWidth(organizerEmailColumn, 105, Unit.PX);
        bookingManagementTable.setColumnWidth(paxColumn, 40, Unit.PX);
        bookingManagementTable.setColumnWidth(paxRollatorenColumn, 96, Unit.PX);
        bookingManagementTable.setColumnWidth(paxFoldableWheelchairColumn, 160, Unit.PX);
        bookingManagementTable.setColumnWidth(paxWheelchairColumn, 95, Unit.PX);
        bookingManagementTable.setColumnWidth(numTaxiColumn, 95, Unit.PX);
        bookingManagementTable.setColumnWidth(requirementsColumn, 95, Unit.PX);
        bookingManagementTable.setColumnWidth(taxiBookingStatusColumn, 95, Unit.PX);
        bookingManagementTable.setColumnWidth(viewTaxiOrderPdfColumn, 80, Unit.PX);
        bookingManagementTable.setColumnWidth(viewFahrenschecksPdfColumn, 95, Unit.PX);
        bookingManagementTable.setColumnWidth(orderTaxiColumn, 80, Unit.PX);

        // Create a data provider.
        ListDataProvider<BookingInfo> dataProvider = new ListDataProvider<BookingInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(bookingManagementTable);

        // Add the data to the data provider, which automatically pushes it to the
        // widget.
        List<BookingInfo> list = dataProvider.getList();
        for (BookingInfo booking : BOOKINGS)
        {
            list.add(booking);
        }

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(bookingManagementTable);

        // We know that the data is sorted alphabetically by default.
        bookingManagementTable.getColumnSortList().push(forwardPickupPlaceColumn);
        bookingManagementTable.getElement().getStyle().setMarginTop(2, Unit.PX);
        bookingManagementTable.setWidth("100%");
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setWidth(100, Unit.PCT);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(bookingManagementTable);
        panel.add(pager);
        mainPanel.add(panel);
    }
}
