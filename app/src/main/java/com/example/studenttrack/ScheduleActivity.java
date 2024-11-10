package com.example.studenttrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity {

    private EditText classNameEditText;
    private EditText classTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        classNameEditText = findViewById(R.id.className);
        classTimeEditText = findViewById(R.id.classTime);
        Button addClassButton = findViewById(R.id.addClassButton);

        addClassButton.setOnClickListener(v -> scheduleClassNotification());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleClassNotification() {
        String className = classNameEditText.getText().toString().trim();
        String classTime = classTimeEditText.getText().toString().trim();

        if (className.isEmpty() || classTime.isEmpty()) {
            Toast.makeText(this, "Please enter class name and time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming classTime is in the format "HH:mm"
        String[] timeParts = classTime.split(":");
        if (timeParts.length != 2) {
            Toast.makeText(this, "Please enter a valid time in HH:mm format", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("className", className);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Notification scheduled for " + className + " at " + classTime, Toast.LENGTH_SHORT).show();
        }
    }
}
