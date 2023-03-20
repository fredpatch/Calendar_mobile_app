package com.example.customcalendarviewwithsevent;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class customCalendarView extends LinearLayout {

    ImageButton NextButton,PrevieusButton;
    TextView CurrentDate;
    GridView gridView;


    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormate =new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new  ArrayList<>();
    DBOpenHelper dbOpenHelper;
    public customCalendarView(Context context) {
        super(context);
    }

    public customCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        Intialiselayout();
        SetupCalender();
        PrevieusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                SetupCalender();
            }
        });

        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                SetupCalender();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView =LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout,null);
                final EditText EventName =addView.findViewById(R.id.eventname);
                final TextView EventTime=addView.findViewById(R.id.eventtime);
                ImageButton SetTime=addView.findViewById(R.id.seteventtime);
                Button AddEvent=addView.findViewById(R.id.addevent);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar=Calendar.getInstance();
                        final int hours=calendar.get(Calendar.HOUR_OF_DAY);
                        final int minuts=calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(addView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c=Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformate=new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String event_Time=hformate.format(c.getTime());
                                EventTime.setText(event_Time);
                            }
                        },hours,minuts,false);
                        timePickerDialog.show();
                    }
                });

                final String date=eventDateFormate.format(dates.get(position));
                final String month=monthFormat.format(dates.get(position));
                final String year=yearFormat.format(dates.get(position));
                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date,month,year);
                        SetupCalender();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }

    public customCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Context context1) {
        super(context, attrs, defStyleAttr);
        this.context = context1;
    }
    private void SaveEvent(String event,String time,String date,String Month,String Year){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,Month,Year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();

    }

    private void Intialiselayout(){
        LayoutInflater inflar = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflar.inflate(R.layout.calendar_layout,this);
        NextButton = view.findViewById(R.id.nextBTN);
        PrevieusButton=view.findViewById(R.id.previeusBTN);
        CurrentDate=view.findViewById(R.id.current_Date);
        gridView=view.findViewById(R.id.gridview);


    }

    private void SetupCalender(){

        String currentDate=dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthcalendar =(Calendar) calendar.clone();
        monthcalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDaysofMonth =monthcalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthcalendar.add(Calendar.DAY_OF_MONTH,-FirstDaysofMonth);
        //CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));
        while (dates.size()<MAX_CALENDAR_DAYS){
            dates.add(monthcalendar.getTime());
            monthcalendar.add(Calendar.DAY_OF_MONTH,1);
        }
        myGridAdapter=new MyGridAdapter(context,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);


    }
}
