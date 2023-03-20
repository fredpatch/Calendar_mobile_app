package com.example.customcalendarviewwithsevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    customCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCalendarView=findViewById(R.id.custom_calendar_view);
    }
}