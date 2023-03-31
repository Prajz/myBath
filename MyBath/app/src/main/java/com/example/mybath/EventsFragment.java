package com.example.mybath;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {

    Button su_info;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        ViewPager2 eventsViewPager = view.findViewById(R.id.eventsViewPager);
        List<EventPage> eventPages = new ArrayList<>();

            EventPage eventPageNetball = new EventPage();
            eventPageNetball.imageURL = "https://www.thesubath.com/asset/Event/6349/bluecrestnetball1.jpg?thumbnail_width=280&thumbnail_height=100&resize_type=ResizeWidth";
            eventPageNetball.location = "STV Courts";
            eventPageNetball.title = "Recreational Netball";
            eventPageNetball.time = "27/03/23: 15:00-19:00";
            eventPages.add(eventPageNetball);

            EventPage eventPosterSale = new EventPage();
            eventPosterSale.imageURL = "https://www.thesubath.com/asset/Event/23895/DSC_0012.jpg?thumbnail_width=550&thumbnail_height=550&resize_type=ResizeFitAll";
            eventPosterSale.location = "Chancellors' Building";
            eventPosterSale.title = "Art & Poster Sale";
            eventPosterSale.time = "28/03/23: 10:00-16:00";
            eventPages.add(eventPosterSale);

            EventPage eventPagePeachy = new EventPage();
            eventPagePeachy.imageURL = "https://www.thesubath.com/asset/Event/15195/Peachy_Logo_Full-01.png?thumbnail_width=280&thumbnail_height=100&resize_type=ResizeWidth";
            eventPagePeachy.location = "The Plug and The Tub";
            eventPagePeachy.title = "Peachy Saturdays";
            eventPagePeachy.time = "01/04/23: 10:30-03:00";
            eventPages.add(eventPagePeachy);

            EventPage eventPagePizza = new EventPage();
            eventPagePizza.imageURL = "https://www.thesubath.com/asset/Event/15195/PandB-thumb.jpg?thumbnail_width=280&thumbnail_height=100&resize_type=ResizeWidth";
            eventPagePizza.location = "The SU";
            eventPagePizza.title = "Pizza and Boardgames";
            eventPagePizza.time = "02/04/23: 18:30-22:30";
            eventPages.add(eventPagePizza);

            EventPage eventPageSalon = new EventPage();
            eventPageSalon.imageURL = "https://www.thesubath.com/asset/Event/6003/213-plasma-wide.jpg?thumbnail_width=280&thumbnail_height=100&resize_type=ResizeWidth";
            eventPageSalon.location = "The SU";
            eventPageSalon.title = "213 The Salon - Gillian";
            eventPageSalon.time = "04/04/23: 09:00-17:00";
            eventPages.add(eventPageSalon);

            eventsViewPager.setAdapter(new EventPageAdapter(eventPages));

            eventsViewPager.setClipToPadding(false);
            eventsViewPager.setClipChildren(false);
            eventsViewPager.setOffscreenPageLimit(3);
            eventsViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.95f + r * 0.05f);
                }
            });

            eventsViewPager.setPageTransformer(compositePageTransformer);


        final DrawerLayout drawerLayout = view.findViewById(R.id.drawerLayout);
        view.findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = view.findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        // for su button
        su_info = view.findViewById(R.id.su_info);
        su_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLink("https://www.thesubath.com/whats-on/");
            }
        });

        return view;
    }

    private void goLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}









