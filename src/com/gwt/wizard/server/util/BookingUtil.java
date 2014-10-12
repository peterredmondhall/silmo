package com.gwt.wizard.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
import com.gwt.wizard.shared.model.BookingInfo;

public class BookingUtil
{

    private static final String FORWARD_PICKUP = "Hinfahrt von:";
    private static final String FORWARD_TIME = "Hinfahrt um:";
    private static final String RETURN_PICKUP = "Rückfahrt von:";
    private static final String RETURN_TIME = "Rückfahrt um:";

    public static String toEmailText(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = toPairList(bookingInfo);
        return toEmailTextFromList(list);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Pair<String, String>> toPairList(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        list.add(new Pair("Veranstaltung am:", bookingInfo.getDate()));
        list.add(new Pair(FORWARD_PICKUP, bookingInfo.getForwardPickupPlace().getPlace()));
        list.add(new Pair(FORWARD_TIME, bookingInfo.getForwardPickupTime()));
        list.add(new Pair(RETURN_PICKUP, bookingInfo.getReturnPickupPlace().getPlace()));
        list.add(new Pair(RETURN_TIME, bookingInfo.getReturnPickupTime()));
        list.add(new Pair("Anzahl Passagiere:", Integer.toString(bookingInfo.getPax())));
        list.add(new Pair("Passagiere mit Rollatoren:", Integer.toString(bookingInfo.getPaxRollatoren())));
        list.add(new Pair("Passagiere mit klappbaren Rollstuhl:", Integer.toString(bookingInfo.getPaxFoldableWheelchair())));
        list.add(new Pair("Passagiere Rollstuhltransport:", Integer.toString(bookingInfo.getPaxRollstuhl())));
        list.add(new Pair("Begleiter - Name:", bookingInfo.getCompanionName()));
        list.add(new Pair("Begleiter - Email:", bookingInfo.getCompanionEmail()));
        list.add(new Pair("Organisator - Name:", bookingInfo.getOrganizerName()));
        list.add(new Pair("Organisator - Email:", bookingInfo.getOrganizerEmail()));
        list.add(new Pair("Andere Details:", bookingInfo.getRequirements()));

        return list;
    }

    public static String toEmailTextFromList(List<Pair<String, String>> list)
    {
        StringBuffer sb = new StringBuffer();
        for (Pair<String, String> pair : list)
        {
            sb.append(pair.first + "\t" + pair.second + "\r");
        }

        String msg = sb.toString();
        System.out.println(msg);
        return msg;
    }

    public static String toEmailHtml(BookingInfo bookingInfo, File file)
    {
        String html = readFile(file);

        int i = 1;
        for (Pair<String, String> pair : toPairList(bookingInfo))
        {
            if (!bookingInfo.isWithReturn() && (pair.first.equals(RETURN_PICKUP) || pair.first.equals(RETURN_TIME)))
            {
                continue;
            }
            String rep = "LINE" + i;
            html = html.replace(rep + "L", pair.first);
            html = html.replace(rep + "R", pair.second);
            i++;
        }

        return html;
    }

    public static String toFahrtenschecksEmailHtml(BookingInfo bookingInfo, File file)
    {
        String html = readFile(file);
        html = html.replace("BOOKING_ID", bookingInfo.getReference());
        if (bookingInfo.getNumTaxis() > 1)
        {
            html = html.replace("NUM_TAXIS", "Es kommen " + bookingInfo.getNumTaxis() + " Taxis.");
        }
        else
        {
            html = html.replace("NUM_TAXIS", "Es kommt " + bookingInfo.getNumTaxis() + " Taxi.");
        }

        return html;
    }

    public static String readFile(File file)
    {
        try
        {

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF8"));)
            {
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
                String ls = System.getProperty("line.separator");

                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                return stringBuilder.toString();
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException();
        }
    }

    // 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
    static int[] taxis = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6 };

    public static int getNumTaxis(int pax, int wheelies)
    {
        return taxis[pax - wheelies];
    }
}
