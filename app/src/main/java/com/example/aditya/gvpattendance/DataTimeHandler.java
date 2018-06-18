package com.example.aditya.gvpattendance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Aditya on 15-03-2018.
 */

public class DataTimeHandler {
    public String weekDay , TodayDate , ActionBarDate;

    SimpleDateFormat ActionDate = new SimpleDateFormat("d-MM-YYYY EEEE", Locale.getDefault());
    SimpleDateFormat dayFormat = new SimpleDateFormat("YYYY-MM-d", Locale.getDefault());
    SimpleDateFormat weekName = new SimpleDateFormat("E" , Locale.getDefault());
    Calendar calendar = Calendar.getInstance();

    public DataTimeHandler(){
        ActionBarDate = ActionDate.format(calendar.getTime());
        TodayDate = dayFormat.format(calendar.getTime());
        weekDay = weekName.format(calendar.getTime());
    }

    public String getTodayDate(){

        return TodayDate;
    }
    public  String getWeekDay(){

        return weekDay;
    }

    public  String getActionBarDate(){

        return ActionBarDate;
    }
}
