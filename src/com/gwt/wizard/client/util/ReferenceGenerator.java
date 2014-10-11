package com.gwt.wizard.client.util;

public class ReferenceGenerator
{
    private static final String[] NAMES = {
            "Anton", "Berta", "Charlie", "Charlotte", "Dora", "Emil", "Friedrich", "Gustav", "Heinrich", "Ida",
            "Julius", "Kaufmann", "Ludwig", "Martha", "Nordpol", "Otto", "Paula", "Quelle", "Richard", "Samuel",
            "Theodor", "Ulrich", "Viktor", "Wilhelm"
    };

    public static String gen(int i)
    {
        int index = i % NAMES.length;
        return NAMES[index];
    }
}
