package com.example.studenttrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private EditText eventEditText;
    private TextView selectedDate;
    private Button checkEventsButton;

    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar); // Ensure this matches your XML file name

        calendarView = findViewById(R.id.calendarView);
        eventEditText = findViewById(R.id.eventEditText);
        selectedDate = findViewById(R.id.selectedDate);
        checkEventsButton = findViewById(R.id.checkEventsButton);

        // Set listener for calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, dayOfMonth);
            selectedDate.setText("Selected Date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
        });
        checkEventsButton.setOnClickListener(v -> {
            String eventDescription = eventEditText.getText().toString();
            if (selectedCalendar != null && !eventDescription.isEmpty()) {
                scheduleNotification(eventDescription);
                Toast.makeText(this, "Event Added and Notification Scheduled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a date and enter an event description", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(String eventDescription) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("event_name", eventDescription);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Schedule the notification to trigger at the selected date and time (midnight for simplicity)
        selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
        selectedCalendar.set(Calendar.MINUTE, 0);
        selectedCalendar.set(Calendar.SECOND, 0);
        selectedCalendar.set(Calendar.MILLISECOND, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectedCalendar.getTimeInMillis(), pendingIntent);
    }
}
