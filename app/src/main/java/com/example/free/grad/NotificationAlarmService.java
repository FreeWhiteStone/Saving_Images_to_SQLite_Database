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
        //Set the alarm to 10 seconds from now
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 5);
        long firstTime = c.getTimeInMillis();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
    }
}
