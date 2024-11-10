package com.example.studenttrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class GoalSettingActivity extends AppCompatActivity {
    private EditText goalEditText;
    private Button setGoalButton;
    private SeekBar progressSeekBar;
    private TextView progressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        goalEditText = findViewById(R.id.goalEditText);
        setGoalButton = findViewById(R.id.setGoalButton);
        progressSeekBar = findViewById(R.id.progressSeekBar);
        progressTextView = findViewById(R.id.progressTextView);
        setGoalButton.setOnClickListener(v -> {
            String goal = goalEditText.getText().toString().trim();
            if (!goal.isEmpty()) {
                Toast.makeText(GoalSettingActivity.this, "Goal set: " + goal, Toast.LENGTH_SHORT).show();
                scheduleGoalNotification(goal);
            } else {
                Toast.makeText(GoalSettingActivity.this, "Please enter a goal", Toast.LENGTH_SHORT).show();
            }
        });
        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressTextView.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optionally, add logic to save or update the progress
            }
        });
    }
    @SuppressLint("ScheduleExactAlarm")
    private void scheduleGoalNotification(String goal) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Goal Reminder");
        intent.putExtra("message", "Don't forget your goal: " + goal);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Reminder set for your goal", Toast.LENGTH_SHORT).show();
    }
}
