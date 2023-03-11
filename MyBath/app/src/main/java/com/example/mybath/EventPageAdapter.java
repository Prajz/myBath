package com.example.mybath;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventPageAdapter extends RecyclerView.Adapter<EventPageAdapter.EventPageViewHolder> {

    private List<EventPage> eventPage;

    public EventPageAdapter(List<EventPage> eventPage) {
        this.eventPage = eventPage;
    }


    @NonNull
    @Override
    public EventPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventPageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_events,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull EventPageViewHolder holder, int position) {
        holder.setData(eventPage.get(position));
    }

    @Override
    public int getItemCount() {
        return eventPage.size();
    }

    static class EventPageViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView kbevents;
        private TextView textTitle;
        private TextView textEvent;

        public EventPageViewHolder(@NonNull View itemView, TextView textTime) {
            super(itemView);
            this.textTime = textTime;
        }

        private TextView textTime;



        EventPageViewHolder(@NonNull View itemView){
            super(itemView);
            kbevents = itemView.findViewById(R.id.kbevents);
            textTitle = itemView.findViewById(R.id.textTitle);
            textEvent = itemView.findViewById(R.id.textEvent);
            textTime = itemView.findViewById(R.id.textTime);

        }

        void setData(EventPage eventPage){
            Picasso.get().load(eventPage.imageURL).into(kbevents);
            textTitle.setText(eventPage.title);
            textEvent.setText(eventPage.location);
            textTime.setText(eventPage.time);
        }
    }
}
