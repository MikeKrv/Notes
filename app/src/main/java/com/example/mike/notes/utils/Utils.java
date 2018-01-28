package com.example.mike.notes.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mike on 28.01.2018.
 */

public  class Utils {

    public static String parseDate(long initialDate, Context context){
        Locale currentLocale = context.getResources().getConfiguration().locale;

        Date date = new Date(initialDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy в H:mm", currentLocale);

        if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
            sdf = new SimpleDateFormat("сегодня в H:mm", currentLocale);
        } else if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
            sdf = new SimpleDateFormat("d MMM в H:mm", currentLocale);
        }

        return sdf.format(date);
    }
}
