package com.example.medapp;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

public class Alarm_set extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    TimePicker timePicker;
    String alarm_info;

    private ArrayList<String> old_alarm_time = new ArrayList<String>();
    private ArrayList<String> old_status_data= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);
        mDatabaseHelper = new DatabaseHelper(this);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        Intent in=getIntent();
        alarm_info = in.getStringExtra("alarm_code");

        String[] arrOfStr = alarm_info.split(",");

        final int alarm_position = Integer.parseInt(arrOfStr[0])+100;
        final int alarm_state = Integer.parseInt(arrOfStr[1]);
        Log.d ("anik",Integer.toString (alarm_position));
        Log.d ("anik",Integer.toString(alarm_state));




        findViewById(R.id.buttonSetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= 23) {
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(),
                            timePicker.getMinute(),
                            0
                    );
                } else {
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute(),
                            0
                    );
                }
                //Setting alarm
                //setAlarm(calendar.getTimeInMillis(), alarm_position);

                Cursor cursor_data = mDatabaseHelper.getData();
                while(cursor_data.moveToNext()){
                    //get the value from the database in column 1
                    //then add it to the ArrayList
                    old_alarm_time.add(cursor_data.getString(2));
                    old_status_data.add(cursor_data.getString(3));
                }

                if (alarm_state == 0 ) {
                    cancelAlarm(calendar.getTimeInMillis(), alarm_position);
                    mDatabaseHelper.updateAlarm_Time("0",alarm_position-100+1,old_alarm_time.get(alarm_position-100));
                    mDatabaseHelper.updateAlarm_status(Integer.toString(alarm_state),alarm_position-100+1,old_status_data.get(alarm_position-100));
                }
                else {
                    setAlarm(calendar.getTimeInMillis(), alarm_position);
                    //Date date = Calendar.getInstance().getTime();
                    int hour = timePicker.getCurrentHour ();
                    int min = timePicker.getCurrentMinute ();
                    //DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    //String strDate = dateFormat.format(date);

                    mDatabaseHelper.updateAlarm_Time(Integer.toString (hour)+":"+Integer.toString (min),alarm_position-100+1,old_alarm_time.get(alarm_position-100));
                    mDatabaseHelper.updateAlarm_status(Integer.toString(alarm_state),alarm_position-100+1,old_status_data.get(alarm_position-100));
                }

            }
        });
        findViewById(R.id.buttonCancelAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= 23) {
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(),
                            timePicker.getMinute(),
                            0
                    );
                } else {
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute(),
                            0
                    );
                }
                //Setting alarm
                //setAlarm(calendar.getTimeInMillis(), alarm_position);

                Cursor cursor_data = mDatabaseHelper.getData();
                while(cursor_data.moveToNext()){
                    //get the value from the database in column 1
                    //then add it to the ArrayList
                    old_alarm_time.add(cursor_data.getString(2));
                    old_status_data.add(cursor_data.getString(3));
                }

                if (alarm_state == 0 ) {
                    cancelAlarm(calendar.getTimeInMillis(), alarm_position);
                    mDatabaseHelper.updateAlarm_Time("0",alarm_position-100+1,old_alarm_time.get(alarm_position-100));
                    mDatabaseHelper.updateAlarm_status(Integer.toString(alarm_state),alarm_position-100+1,old_status_data.get(alarm_position-100));
                }
                else {
                    setAlarm(calendar.getTimeInMillis(), alarm_position);
                    int hour = timePicker.getCurrentHour ();
                    int min = timePicker.getCurrentMinute ();
                    mDatabaseHelper.updateAlarm_Time(Integer.toString (hour)+":"+Integer.toString (min),alarm_position-100+1,old_alarm_time.get(alarm_position-100));
                    mDatabaseHelper.updateAlarm_status(Integer.toString(alarm_state),alarm_position-100+1,old_status_data.get(alarm_position-100));
                }

            }
        });

    }

    private void setAlarm(long timeInMillis,int alarm_code) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,MyAlarm.class);
        intent.putExtra("alarm_code",Integer.toString(alarm_code));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,alarm_code,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent); //RTC_WAKEUP = works without running & INTERVAL_DAY
        Toast.makeText(this,"Alarm is set",Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm(long timeInMillis,int alarm_code) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,MyAlarm.class);
        intent.putExtra("alarm_code",Integer.toString(alarm_code));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,alarm_code,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent); //RTC_WAKEUP = works without running & INTERVAL_DAY
        Toast.makeText(this,"Alarm is cancel",Toast.LENGTH_SHORT).show();
        alarmManager.cancel(pendingIntent);

    }


}