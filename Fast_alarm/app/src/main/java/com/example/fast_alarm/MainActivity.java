package com.example.fast_alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView timeTextView;
    TextView amPmTextView;
    AppCompatButton onOffButton;

    public static final int ALARM_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTextView = (TextView) findViewById(R.id.timeTextView);
        amPmTextView = (TextView) findViewById(R.id.amPmTextView);
        onOffButton = (AppCompatButton) findViewById(R.id.onOffButton);

        updateViews();
    }

    private void turnOnAlarm(int hour, int minute) {
        Calendar alarmTime = Calendar.getInstance();
        Calendar current = Calendar.getInstance();

        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        if (alarmTime.before(current)) alarmTime.add(Calendar.DATE, 1);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 하루에 한번씩 실
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void turnOffAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent != null) {
            pendingIntent.cancel();
        }
    }

    public void onOffClicked(View view) {
        AlarmDisplayModel model = fetchData();

        if (model.onOff) {
            // 상태: ON
            turnOffAlarm();
            model.setOnOff(false);
        } else {
            // 상태: OFF
            turnOnAlarm(model.getHour(), model.getMinute());
            model.setOnOff(true);
        }

        saveData(model);
        updateViews();
    }

    public void setTimeClicked(View view) {
        Calendar calendar = Calendar.getInstance();

        TimePickerListener listener = new TimePickerListener();
        TimePickerDialog picker = new TimePickerDialog(this, listener,
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);

        picker.show();
    }

    class TimePickerListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            AlarmDisplayModel model = new AlarmDisplayModel(hourOfDay, minute, false);
            saveData(model);

            updateViews();
            turnOffAlarm();
        }
    }

    private void saveData(AlarmDisplayModel model) {
        SharedPreferences pref = getSharedPreferences("time", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("hour", model.getHour());
        editor.putInt("minute", model.getMinute());
        editor.putBoolean("onOff", model.getOnOff());

        editor.commit();
    }

    private AlarmDisplayModel fetchData() {
        SharedPreferences pref = getSharedPreferences("time", MODE_PRIVATE);

        int hour = pref.getInt("hour", 0);
        int minute = pref.getInt("minute", 0);
        Boolean onOff = pref.getBoolean("onOff", false);

        AlarmDisplayModel model = new AlarmDisplayModel(hour, minute, onOff);

        return model;
    }

    private void updateViews() {
        AlarmDisplayModel model = fetchData();

        timeTextView.setText(model.timeText);
        amPmTextView.setText(model.amPmText);

        if (model.onOff) {
            onOffButton.setText("OFF");
        } else {
            onOffButton.setText("ON");
        }
    }

}