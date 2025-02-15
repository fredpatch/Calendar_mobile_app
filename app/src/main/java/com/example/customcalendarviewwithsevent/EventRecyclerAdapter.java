package com.example.customcalendarviewwithsevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder>{

    Context context;
    ArrayList<Events> arrayList;
    DBOpenHelper dbOpenHelper;


    public EventRecyclerAdapter(Context context, ArrayList<Events> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {

      final Events events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.Time.setText(events.getTIME());
        holder.Date.setText(events.getDATE());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletecalendarevent(events.getEVENT(),events.getDATE(),events.getTIME());
                arrayList.remove(holder.getAdapterPosition());//position
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Event,Date,Time;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Event = itemView.findViewById(R.id.eventname);
            Date = itemView.findViewById(R.id.eventdate);
            Time   = itemView.findViewById(R.id.eventime);
            delete=itemView.findViewById(R.id.delete);
        }
    }

    private void deletecalendarevent(String event, String date, String time){
        dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(event,date,time,database);
        dbOpenHelper.close();

    }
}
