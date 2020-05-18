package com.otapp.net.Events.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.interfac.EventDateSelectionInterface;
import com.otapp.net.R;
import com.otapp.net.utils.MyPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerEventDateTimeAdapter extends RecyclerView.Adapter<RecyclerEventDateTimeAdapter.MyViewHolder> {
    public Context context;
    public List<EventsListResponse.Events.EventDates> eventDatesList;
    String apiDate = "", strDate = "";
    String splidDate[] = new String[]{};
    String splitDate2[] = new String[]{};
    String splitChagedDate[] = new String[]{};
    EventsListResponse.Events.EventDates eventDates;
    EventDateSelectionInterface eventDateSelectionInterface;

 /*   public RecyclerEventDateTimeAdapter(Context context, List<EventsListResponse.Events.EventDates> eventDatesList) {
        this.context = context;
        this.eventDatesList = eventDatesList;
    }
*/
    public RecyclerEventDateTimeAdapter(Context context, List<EventsListResponse.Events.EventDates> eventDatesList, EventDateSelectionInterface eventDateSelectionInterface) {
        this.context = context;
        this.eventDatesList = eventDatesList;
        this.eventDateSelectionInterface = eventDateSelectionInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_select_event_date, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        eventDates  = eventDatesList.get(i);
        splidDate = eventDates.event_date.split(" ");
        strDate = splidDate[0];
        splitDate2 = strDate.split("-");


        if(eventDates.isSelected){
            myViewHolder.tvEventMonth.setBackgroundResource(R.drawable.top_rouded_corner_blue_baground);
            String strEventDate = MyPref.getPref(context,MyPref.PREF_EVENT_DATE,"");
          /*  if(!strEventDate.equals(eventDatesList.get(i).event_date)) {*/
            //    eventDateSelectionInterface.getEventDate(eventDatesList.get(i).event_date, eventDatesList.get(i).slider);
          /*  }*/
        }else {
            myViewHolder.tvEventMonth.setBackgroundResource(R.drawable.top_rounded_corner);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(strDate);
            String dayOfTheWeek = (String) DateFormat.format("EEE", date);
            String day = (String) DateFormat.format("dd", date);
            String monthString = (String) DateFormat.format("MMM", date);

            myViewHolder.tvEventMonth.setText(monthString);
            myViewHolder.tvEventTime.setText(splidDate[1]);
            myViewHolder.tvEventDate.setText(dayOfTheWeek + " " + day);
            myViewHolder.tvDate.setText(eventDates.event_date);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return eventDatesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventDate, tvEventTime, tvEventMonth, tvDate;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvEventDate = itemView.findViewById(R.id.tvDate);
            tvEventMonth = itemView.findViewById(R.id.tvMonth);
            tvEventTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.actualDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<eventDatesList.size();i++){
                        if(i==getAdapterPosition()){
                            eventDatesList.get(getAdapterPosition()).isSelected=true;
                            tvEventMonth.setBackgroundResource(R.drawable.top_rouded_corner_blue_baground);
                        }else {
                            eventDatesList.get(i).isSelected=false;
                        }
                    }
                    eventDateSelectionInterface.getEventDate(eventDatesList.get(getAdapterPosition()).event_date,eventDatesList.get(getAdapterPosition()).slider);
                    notifyDataSetChanged();

                }
            });
        }
    }
}
