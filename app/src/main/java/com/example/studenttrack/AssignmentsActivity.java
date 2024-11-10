package com.example.studenttrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssignmentsActivity extends AppCompatActivity {
    private EditText assignmentName, assignmentDueDate;
    private Button addAssignmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        assignmentName = findViewById(R.id.assignment_name);
        assignmentDueDate = findViewById(R.id.assignment_due_date);
        addAssignmentButton = findViewById(R.id.add_assignment_button);

        addAssignmentButton.setOnClickListener(v -> {
            String name = assignmentName.getText().toString();
            String dueDateStr = assignmentDueDate.getText().toString();

            if (!name.isEmpty() && !dueDateStr.isEmpty()) {
                // Save assignment logic (optional, can use Firebase here)

                try {
                    // Schedule the notification based on the due date
                    scheduleAssignmentNotification(name, dueDateStr);
                } catch (ParseException e) {
                    Toast.makeText(this, "Invalid date format. Please use 'MM/dd/yyyy'.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter both the assignment name and due date.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleAssignmentNotification(String assignmentName, String dueDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date dueDate = sdf.parse(dueDateStr);

        if (dueDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dueDate);

            // Schedule the notification 1 day before the assignment due date
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("title", "Assignment Reminder");
            intent.putExtra("message", "Your assignment \"" + assignmentName + "\" is due tomorrow!");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Schedule the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Notification set for one day before the due date.", Toast.LENGTH_SHORT).show();
        }
    }
}
