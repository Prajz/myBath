package com.example.mybath;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {

    private LinearLayout overviewContainer;
    private TextView status;
    private final String apiKEY = "08ef4befe127e2bd6b3701cbc3eb99d4";
    public String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=Bath,GB&APPID=08ef4befe127e2bd6b3701cbc3eb99d4";


    private TextView temp;
    private TextView tempMin;
    private TextView tempMax;
    private TextView nextBus;

    private TextView busLocation;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TIMETABLE_URL = "https://www.firstbus.co.uk/bristol-bath-and-west/plan-journey/timetables?service=U1&day=mf";
    private List<Tuples> mTimings = new ArrayList<>();

    LocalTime currentTime = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    int hhmmTime = Integer.parseInt(currentTime.format(formatter));


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {

        //Weather
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, weatherURL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject mainar= (JSONObject) response.get("main");
                    //gets main object from json

                    String realte= mainar.getString("temp");
                    String temprea = temptoc(realte);
                    temp.setText(temprea);
                    JSONArray weatar = (JSONArray) response.get("weather");
                    //gets weather description from weather
                    JSONObject statusdE = (JSONObject) weatar.get(0);
                    String statusd = statusdE.getString("main");
                    status.setText(statusd);
                    //gets feel like temp from weather array
                    String feelte = mainar.getString("feels_like");
                    String tempfee = temptoc(feelte);
                    tempMin.setText("Feels like : " + tempfee);
                    JSONObject windar = (JSONObject) response.get("wind");
                    String winds = windar.getString("speed");
                    float windsp = Float.parseFloat(winds) * 3.61f;
                    int windspe = Math.round(windsp);
                    String windspeed = "Wind speed : " + windspe + " km/h";
                    tempMax.setText(windspeed);
                    String iconsp = statusdE.getString("icon");
                    new WeatherIm((ImageView) rootView.findViewById(R.id.weathericonc)).execute("http://openweathermap.org/img/wn/" + iconsp + "@2x.png");
                } catch (JSONException e) {
                    status.setText(e.getMessage());
                    e.printStackTrace();
                }

            }
        }, error -> Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show());
        queue.add(request);



        // TextViews
        TextView title = rootView.findViewById(R.id.title);
        Date d = new Date();
        CharSequence todaysda = DateFormat.format("dd MMM yy", d.getTime());

        TextView time = rootView.findViewById(R.id.time);

        nextBus = rootView.findViewById(R.id.nextBus);
        busLocation = rootView.findViewById(R.id.busLocation);
        status = rootView.findViewById(R.id.status);
        temp = rootView.findViewById(R.id.temp);
        tempMin = rootView.findViewById(R.id.temp_min);
        tempMax = rootView.findViewById(R.id.temp_max);

        // Layouts
        overviewContainer = rootView.findViewById(R.id.overviewContainer);

        // ImageViews
        ImageView samisImage = rootView.findViewById(R.id.samis_button);
        ImageView timetableImage = rootView.findViewById(R.id.timetable_button);
        Button bus_web = rootView.findViewById(R.id.bus_website_button);

        // Set values to Views
        title.setText("MyBath");
        time.setText(todaysda);


        new FetchTimetableTask().execute(TIMETABLE_URL);
        // Set onClickListener to Layouts and ImageViews



        samisImage.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://samis.bath.ac.uk/urd/sits.urd/run/siw_lgn?STU");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        timetableImage.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://mytimetable.bath.ac.uk/schedule");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        bus_web.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.firstbus.co.uk/bristol-bath-and-west/plan-journey/journey-planner/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });




    }

    private class WeatherIm extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public WeatherIm(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                tempMin.setText(e.toString());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
    private class FetchTimetableTask extends AsyncTask<String, Void, List<String>> {

        protected List<String> doInBackground(String... urls) {
            List<String> timings = new ArrayList<>();

            try {
                Document doc = Jsoup.connect(urls[0]).get();
                Tuples temp = null;
                String temp_s = "";
                int cnt = 0;
                List<String> temp_t =  new ArrayList<>();
                Elements trs = doc.select(".table_main, .table_alt");
                Elements elements = trs.select("td");
                for (int i = 0; i < elements.size(); i++){
                    if(elements.get(i).className().equals("first-column")) {
                        if (cnt == 0) {
                            temp_s = elements.get(i).text();
                        }
                        if (cnt != 0) {
                            temp = new Tuples(temp_s, temp_t);
                            mTimings.add(temp);
                        }
                        temp_s = elements.get(i).text();
                    }
                       else{
                           temp_t.add(elements.get(i).text());
                           if(cnt == 0) {
                               cnt++;
                           }
                       }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching timetable", e);
            }
            return timings;
        }


        @Override
        protected void onPostExecute(List<String> timings) {
        try{
            String loc = mTimings.get(116).getName();
            List<String> time1 = mTimings.get(116).getValues();

        int i = 0;
        for(; i < time1.size();i++){
                if(time1.get(i).equals("----") == false) {
                    int time_temp = Integer.parseInt(time1.get(i));
                    if(time_temp > hhmmTime)
                    {
                        System.out.println("ENTERED" + hhmmTime);
                        String a = "";
                        String b = "";
                        if(time_temp/100 < 10){
                            a = "0" + time_temp / 100;
                        }
                        else {
                            a = String.valueOf(time_temp/100);
                        }
                        if(time_temp%100 < 10){
                            b = "0" + time_temp % 100;
                        }
                        else {
                            b = String.valueOf(time_temp%100);
                        }
                        nextBus.setText("Next U1 Bus: " + a+ ":" + b); // Replace "New text" with the text you want to set
                        busLocation.setText("Bus Location: " + loc.substring(0,loc.indexOf('-')));
                        break;
                    }
                }
            }
        //to make sure app doesnt crash if firstbus timetable hasn't loaded
        }catch(IndexOutOfBoundsException i){
            i.printStackTrace();
            }
    }
}

    public String temptoc(String bongo){
        float tempr = Float.parseFloat(bongo);
        int tempre = Math.round(tempr - 273);
        //kelvin to celcius
        String temprea = tempre + "°C";
        return temprea;
    }}