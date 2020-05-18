package com.otapp.net.Events.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.otapp.net.Events.Activity.EventDetailsActivity;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.R;
import com.otapp.net.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class EventListRecyclerAdapter extends RecyclerView.Adapter<EventListRecyclerAdapter.MyViewHolder> {

    private List<EventsListResponse.Events> eventsList;
    private Context context;
    EventsListResponse eventsListResponse;

    public EventListRecyclerAdapter(List<EventsListResponse.Events> eventsList, Context context, EventsListResponse eventsListResponse) {
        this.eventsList = eventsList;
        this.context = context;
        this.eventsListResponse = eventsListResponse;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_event_list_recycler, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        EventsListResponse.Events events=eventsList.get(i);
        myViewHolder.tvEventName.setText(events.event_name);
        myViewHolder.tvDate.setText(events.event_short_date_title);
        myViewHolder.tvPrice.setText(events.event_start_fare);
       myViewHolder.avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Glide.with(context).load(events.event_slider).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                myViewHolder.imageViewEvent.setImageResource(R.drawable.bg_no_event_img);
                myViewHolder.avLoadingIndicatorView.setVisibility(View.GONE);
                return false;

            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                myViewHolder.avLoadingIndicatorView.setVisibility(View.GONE);
                return false;
            }
        }).into( myViewHolder.imageViewEvent);

        myViewHolder.tvGener.setText(eventsListResponse.eventsGenresList.get(Integer.parseInt(events.event_genre)-1).genre_name);
        try {
            byte[] data = Base64.decode(events.event_address, Base64.DEFAULT);
            myViewHolder.tvEventAddress.setText(new String(data, "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewEvent;
        TextView tvEventName,tvEventAddress,tvGener,tvPrice,tvDate;
        AVLoadingIndicatorView avLoadingIndicatorView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewEvent=itemView.findViewById(R.id.ivEvent);
            tvEventAddress=itemView.findViewById(R.id.tvAddress);
            tvEventName=itemView.findViewById(R.id.tvName);
            tvGener=itemView.findViewById(R.id.tvGenre);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvDate=itemView.findViewById(R.id.tvDate);
            avLoadingIndicatorView= itemView.findViewById(R.id.aviProgress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("evnetName",tvEventName.getText().toString());
                    intent.putExtra("data",  new Gson().toJson(eventsListResponse));
                    context.startActivity(intent);
                }
            });
        }
    }
}
