package com.example.free.grad;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class NotificationAlarmService {
    private Context context;
    private PendingIntent mAlarmSender;
    public NotificationAlarmService(Context context) {
        this.context = context;
        mAlarmSender = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
    }

    public void startAlarm(){

        Database db = new Database(context.getApplicationContext());
        HashMap<String, String> map2 = db.getProperDate();
        String date=(map2.get("time_stamp"));

        Log.d("date_from_db",date);


        String dateInString = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        //Set the alarm to 10 seconds from now
        Calendar c = Calendar.getInstance();
        //Log.d("ceee",c.toString());
        c.add(Calendar.SECOND, 5);
        //long firstTime = c.getTimeInMillis();

//        Log.d("firstTime",Long.toString(firstTime));
//        try {
//            c.setTime(sdf.parse(dateInString));
//            //Log.d("ceee",c.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        long firstTime = c.getTimeInMillis();
        Log.d("firstTime1",Long.toString(firstTime));
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
        Log.d("startAlarm", "start");
    }
}