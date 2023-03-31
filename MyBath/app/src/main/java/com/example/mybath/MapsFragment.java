package com.example.mybath;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String buildingstest = "https://praj.co.uk/assets/buildingt.json";
    MapView map = null;
    EditText et_text1;
    EditText et_text2;
    // Defining two MultiAutoCompleteTextView
    // This is to recognize comma separated.

    // As a sample, few text are given below which can be populated in dropdown, when user starts typing
    // For example, when user types "a", text whichever starting with "a" will be displayed in dropdown
    // As we are using two MultiAutoCompleteTextView components, using two string array separately
    String[] fewRandomSuggestedText = {"Prayer Room","6 West","Woodland Court","Wessex House","Eastwood","Westwood","Management Building","STV","The SU", "3 West North","The Market", "Norwood House", "Lime Tree", "Parade", "10 West","4 West Cafe","Applied Biomechanics Suite","Architecture & Civil Engineering","Bakeaway","Bale Haus","Bus Stop","CAFÉ at Polden", "CAFÉ at The Edge" ,"Campus Infrastructure","Central Stores","Chancellors’ Building", "Chaplaincy Centre", "Chemical Engineering", "Chemistry", "Claverton Rooms", "Communications", "Computer Science", "Cotswold House", "Dental Centre", "Development & Alumni Relations", "Doctoral College", "East Accommodation Centre","Economics", "The Edge Arts","Education","Electronic & Electrical Engineering","Founders Hall","Fountain Canteen","Fresh Grocery Store", "Goods Received","Health","Library"};
    String[] fewTags = {"Prayer Room","6 West","Woodland Court","Wessex House","Eastwood","Westwood","Management Building","STV","The SU", "3 West North", "The Market", "Norwood House", "Lime Tree", "Parade", "10 West","4 West Cafe","Applied Biomechanics Suite","Architecture & Civil Engineering","Bakeaway","Bale Haus","Bus Stop","CAFÉ at Polden", "CAFÉ at The Edge" ,"Campus Infrastructure","Central Stores","Chancellors’ Building", "Chaplaincy Centre", "Chemical Engineering", "Chemistry", "Claverton Rooms", "Communications", "Computer Science", "Cotswold House", "Dental Centre", "Development & Alumni Relations", "Doctoral College", "East Accommodation Centre","Economics", "The Edge Arts","Education","Electronic & Electrical Engineering","Founders Hall","Fountain Canteen","Fresh Grocery Store", "Goods Received","Health","Library"};
    RotationGestureOverlay mRotationGestureOverlay = null;
    String MY_USER_AGENT = Configuration.getInstance().getUserAgentValue();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        double north = 51.3830;
        double south = 51.3750;
        double east = -2.3201;
        double west = -2.3334;

        // initialise a map
        map = (MapView) rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(51.380001, -2.326944);
        mapController.setCenter(startPoint);
        mapController.setZoom(19.0);

        // Create a BoundingBox object for the campus and restrict scrolling
        BoundingBox campusBounds = new BoundingBox(north, east, south, west);
        map.setMultiTouchControls(true);
        map.setScrollableAreaLimitDouble(campusBounds);
        map.setHorizontalMapRepetitionEnabled(false);
        map.setVerticalMapRepetitionEnabled(false);
        map.setMaxZoomLevel(21.0);
        map.setMinZoomLevel(17.0);
        mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(true);
        map.getOverlays().add(this.mRotationGestureOverlay);
        //Display user location
        MyLocationNewOverlay uLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        uLocationOverlay.enableMyLocation();
        map.getOverlays().add(uLocationOverlay);
        // Input box for starting and end destination
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, fewRandomSuggestedText);
        AutoCompleteTextView textView1 = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextOne);
        textView1.setAdapter(adapter1);
        textView1.setThreshold(1);//will start working from first character
        textView1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, fewTags);
        AutoCompleteTextView textView2 = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextTwo);
        textView2.setAdapter(adapter2);
        textView2.setThreshold(1);//will start working from first character
        textView2.setAdapter(adapter2);

        et_text1 = rootView.findViewById(R.id.autoCompleteTextOne);
        et_text2 = rootView.findViewById(R.id.autoCompleteTextTwo);
        Button goButton = rootView.findViewById(R.id.simpleButton);
        RadioButton centButton = rootView.findViewById(R.id.centloc);
        TextView aprotme = rootView.findViewById(R.id.outpTime);
        Button clButton = rootView.findViewById(R.id.clearbut);
        ImageButton clockstart = rootView.findViewById(R.id.clocstart);

        aprotme.setText(" ");



        clockstart.setOnClickListener(v -> {

            et_text1.setText("");

            String str2 = et_text2.getText().toString();

            RequestQueue queuemd = Volley.newRequestQueue(getContext());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, buildingstest,null, response -> {
                try {
                    aprotme.setText(" ");
                    JSONObject ening = (JSONObject) response.get(str2);
                    String endesc = ening.getString("description");
                    ArrayList<GeoPoint>StartPoints = new ArrayList<>();
                    ArrayList<GeoPoint>StartPoints1 = new ArrayList<>();
                    ArrayList<GeoPoint>EndPoints = hiarl(ening);
                    String stdesc1 = "Your location";
                    try{
                        if (oncampus(uLocationOverlay.getMyLocation())) {
                            JSONArray keys = response.names();
                            Double curdiff = 1231.232123;
                            GeoPoint acloc = uLocationOverlay.getMyLocation();
                            System.out.println(acloc);
                            GeoPoint ste1cord = new GeoPoint(uLocationOverlay.getMyLocation());
                            StartPoints1.add(ste1cord);
                            String bil = "";
                            for (int i = 1; i < (response.length()); i++) {
                                String curob = keys.getString(i);
                                JSONObject curaob = (JSONObject) response.get(curob);
                                JSONArray st1 = curaob.getJSONArray("start1");
                                JSONArray st2 = curaob.getJSONArray("start2");
                                Double checkdif = acloc.distanceToAsDouble(new GeoPoint(st1.getDouble(0), st1.getDouble(1)));
                                if (st2.length() == 2) {
                                    Double checkdif2 = acloc.distanceToAsDouble(new GeoPoint(st2.getDouble(0), st2.getDouble(1)));
                                    if (checkdif2 < checkdif) {
                                        checkdif = checkdif2;
                                        System.out.println(checkdif2);
                                        System.out.println(checkdif);
                                    }
                                }
                                if (checkdif < curdiff) {
                                    curdiff = checkdif;
                                    bil = curob;
                                }
                            }
                            JSONObject sting = (JSONObject) response.get(bil);

                            StartPoints = hiarl(sting);

                            String stdesc = sting.getString("description");
                            String starttitle = "You";

                            if (curdiff > 50.0){
                                String timece = pathFinder(StartPoints1, StartPoints,starttitle,bil, stdesc1, stdesc, ctx);
                                String timecec = pathFinder1(StartPoints, EndPoints,bil,str2, stdesc, endesc, ctx);
                                String aproxtimeece = times(timece, timecec);
                                aprotme.setText("Total " + aproxtimeece);
                        }else{
                                String actualpath = pathFinder(StartPoints, EndPoints,starttitle, str2, stdesc1, endesc, ctx);
                                String thrubuild = String.valueOf((curdiff / 1.2));
                                String totalaprox = times(actualpath, thrubuild);
                                aprotme.setText(totalaprox);
                            }
                        } else {
                            aprotme.setText("You can only use this feature on campus");
                        }

                    }catch (NullPointerException e){
                        e.printStackTrace();
                        aprotme.setText("Enable location access to use this feature");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    aprotme.setText("Enter a valid Destination");

                }
            }, error -> aprotme.setText("Enter a valid destination"));

            queuemd.add(request);

        });



        clButton.setOnClickListener( v -> {
            et_text1.setText("");
            et_text2.setText("");
            aprotme.setText(" ");
            map.getOverlays().clear();
            map.getOverlays().add(this.mRotationGestureOverlay);
            map.getOverlays().add(uLocationOverlay);
            try{
                mapController.animateTo(uLocationOverlay.getMyLocation(),19.0,(long)2500,0f);
            }catch (NullPointerException em) {
                em.printStackTrace();
                mapController.animateTo(startPoint,19.0,(long)2500,0f);
            }
                });

        centButton.setOnClickListener(v -> {
            try {
                String topt = aprotme.getText().toString();
                if (topt.startsWith("You")){
                    aprotme.setText(" ");
                }if (topt.startsWith("Enable")){
                    aprotme.setText(" ");
                }

                if (oncampus(uLocationOverlay.getMyLocation())) {
                    mapController.animateTo(uLocationOverlay.getMyLocation(), 20.1, (long) 2111,0f);
                } else {
                    aprotme.setText("You are not on campus");
                }
            }catch (NullPointerException r) {
                r.printStackTrace();
                aprotme.setText("Enable location access to use this feature");
            }
        });

        goButton.setOnClickListener(view -> {
            // NEED TO VALIDATE INPUT
            String str1 = et_text1.getText().toString();
            String str2 = et_text2.getText().toString();
            // this is where we will call the function to access coordinates and information

            RequestQueue queuemd = Volley.newRequestQueue(getContext());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, buildingstest,null, response -> {
                try {
                    aprotme.setText(" ");

                    JSONObject sting = (JSONObject) response.get(str1);
                    JSONObject ening = (JSONObject) response.get(str2);
                    String stdesc = sting.getString("description");
                    String endesc = ening.getString("description");
                    //pass to function to compare different paths
                    ArrayList<GeoPoint>StartPoints;
                    StartPoints = hiarl(sting);

                    ArrayList<GeoPoint>EndPoints;
                    EndPoints = hiarl(ening);
                    String times = pathFinder(StartPoints, EndPoints,str1, str2 , stdesc,endesc, ctx);
                    String aproxtimee = estimtime(times);
                    aprotme.setText(aproxtimee);

                } catch (JSONException md) {
                    System.out.println("bong2");
                    md.printStackTrace();
                    aprotme.setText("Enter a valid Location/Destination.");

                }
            }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show());
            queuemd.add(request);
        });
        et_text1.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                String strValue = et_text1.getText().toString();
                System.out.printf("text1: %s%n", strValue);
                return true;
            }
            return false;
        });
        et_text2.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                String strValue = et_text2.getText().toString();
                System.out.printf("text2: %s%n", strValue);
                return true;
            }
            return false;
        });
        return rootView;
    }

    public String createPath(GeoPoint start, GeoPoint end, String startn, String endn, String DescriptionStart, String DescriptionEnd, Context ctx){
        map.getOverlays().clear();
        MyLocationNewOverlay uLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        uLocationOverlay.enableMyLocation();
        map.getOverlays().add(uLocationOverlay);
        map.getOverlays().add(this.mRotationGestureOverlay);
        IMapController mapController = map.getController();
        mapController.animateTo(start, 19.5, (long) 2400, 0f);
        String timet = distancecalc(start,end,ctx)[1];

        // Waypoint array
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        System.out.println(start);
        System.out.println(end);
        // Start markers
        Marker startMarker = new Marker(map);
        startMarker.setPosition(start);
        String stPoint = startn;
        if (stPoint.startsWith(" ")){
            startMarker.setTitle("You");
        }else{
            startMarker.setTitle(stPoint);
        }
        Drawable startIcon = ResourcesCompat.getDrawable(getResources(),R.drawable.marker_departure, null);
        startMarker.setIcon(startIcon);
        startMarker.setSubDescription(DescriptionStart);
        waypoints.add(start);

        // End markers
        Marker endMarker = new Marker(map);
        Drawable endIcon = ResourcesCompat.getDrawable(getResources(),R.drawable.marker_destination, null);
        endMarker.setPosition(end);
        endMarker.setTitle(endn);
        endMarker.setIcon(endIcon);
        endMarker.setSubDescription(DescriptionEnd);
        waypoints.add(end);

        RoadManager roadManager = new OSRMRoadManager(ctx, MY_USER_AGENT);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT); // Calculate on foot

        Road road = roadManager.getRoad(waypoints);// Find path using waypoints
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        // Apply overlay
        map.getOverlays().add(roadOverlay);
        map.getOverlays().add(startMarker);
        map.getOverlays().add(endMarker);
        return timet;
    }

    public String pathFinder(ArrayList<GeoPoint> startPoints, ArrayList<GeoPoint> endPoints,String startn, String endn, String DescriptionStart, String DescriptionEnd, Context ctx){

        // Waypoint array
        GeoPoint shortest_start = null;
        GeoPoint shortest_end = null;
        double distance = 9999999.9;
        double current_distance;

        for (GeoPoint start : startPoints) {
            for (GeoPoint end : endPoints) {
                current_distance = Double.parseDouble(distancecalc(start,end,ctx)[0]);
                if(distance > current_distance){
                    distance = current_distance;
                    shortest_start = start;
                    shortest_end = end;

                }
            }
        }
        return createPath(shortest_start,shortest_end,startn,endn,DescriptionStart,DescriptionEnd,ctx);

    }
    public String pathFinder1(ArrayList<GeoPoint> startPoints, ArrayList<GeoPoint> endPoints,String startn,String endn, String DescriptionStart, String DescriptionEnd, Context ctx){

        // Waypoint array
        GeoPoint shortest_start = null;
        GeoPoint shortest_end = null;
        double distance = 9999999.9;
        double current_distance;

        for (GeoPoint start : startPoints) {
            for (GeoPoint end : endPoints) {
                current_distance = Double.parseDouble(distancecalc(start,end,ctx)[0]);
                if(distance > current_distance){
                    distance = current_distance;
                    shortest_start = start;
                    shortest_end = end;

                }
            }
        }
        return createPath1(shortest_start,shortest_end,startn,endn, DescriptionStart,DescriptionEnd,ctx);

    }

    public String[] distancecalc(GeoPoint start, GeoPoint end, Context ctx){

        // Waypoint array
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

        waypoints.add(start);
        waypoints.add(end);

        RoadManager roadManager = new OSRMRoadManager(ctx, MY_USER_AGENT);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT); // Calculate on foot

        Road road = roadManager.getRoad(waypoints);// Find path using waypoints


        // Distance and time
        double distance = road.mLength;
        System.out.println("dist1 : " + distance);
        String distance1 = String.valueOf(distance);
        double time = road.mDuration;
        String time1 = String.valueOf(time);


        return new String[]{distance1,time1};

    }
    public boolean oncampus(GeoPoint uloc) {
        double north = 51.3830;
        double south = 51.3750;
        double east = -2.3201;
        double west = -2.3334;
        if (uloc.getLatitude() < north) {
            if (uloc.getLatitude() > south) {
                if (uloc.getLongitude() < east) {
                    if (uloc.getLongitude() > west) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String estimtime(String ti){
        Double timed = Double.valueOf(ti);
        Double timemind = (double) timed / 60.0;
        int timemini = (int) Math.floor(timemind);
        Double timesecd = (double) timed - (60.0*timemini);
        int timeseci = (int) Math.floor(timesecd);
        String timesecs = String.valueOf(timeseci);
        String timemins = String.valueOf(timemini);
        String aproxtimee = "Estimated Time - " + timemins + " m : " + timesecs + " s";
        if  (timemini == 0){
            aproxtimee = "Estimated Time - " + timesecs + " seconds";
        }
        return aproxtimee;
    }

    public ArrayList<GeoPoint> hiarl(JSONObject response) {
        try {
            ArrayList<GeoPoint> bong = new ArrayList<>();

            JSONArray ste1 = (JSONArray) response.get("start1");

            JSONArray ste2 = (JSONArray) response.get("start2");
            GeoPoint ste1cord = new GeoPoint(ste1.getDouble(0), ste1.getDouble(1));
            if (ste2.length() == 2) {
                GeoPoint ste2cord = new GeoPoint(ste2.getDouble(0), ste2.getDouble(1));
                bong.add(ste2cord);
            }
            bong.add(ste1cord);

            return bong;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<GeoPoint> emp = new ArrayList<>();
        return emp;
    }

    public String createPath1(GeoPoint start, GeoPoint end,String startn, String endn, String DescriptionStart, String DescriptionEnd, Context ctx){
        String timet = distancecalc(start,end,ctx)[1];

        // Waypoint array
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        System.out.println(start);
        System.out.println(end);
        // Start markers
        Marker startMarker = new Marker(map);
        startMarker.setPosition(start);
        String stPoint = startn;
        if (stPoint.startsWith("")){
            startMarker.setTitle("Your next location");
        }else{
            startMarker.setTitle(stPoint);
        }
        Drawable startIcon = ResourcesCompat.getDrawable(getResources(),R.drawable.marker_departure, null);
        startMarker.setIcon(startIcon);
        startMarker.setSubDescription(DescriptionStart);
        waypoints.add(start);

        // End markers
        Marker endMarker = new Marker(map);
        Drawable endIcon = ResourcesCompat.getDrawable(getResources(),R.drawable.marker_destination, null);
        endMarker.setPosition(end);
        endMarker.setTitle(endn);
        endMarker.setIcon(endIcon);
        endMarker.setSubDescription(DescriptionEnd);
        waypoints.add(end);

        RoadManager roadManager = new OSRMRoadManager(ctx, MY_USER_AGENT);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT); // Calculate on foot

        Road road = roadManager.getRoad(waypoints);// Find path using waypoints
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        // Apply overlay
        map.getOverlays().add(roadOverlay);
        map.getOverlays().add(startMarker);
        map.getOverlays().add(endMarker);
        return timet;
    }

    public String times(String t1, String t2){
        String out= "";
        Double time1 = Double.valueOf(t1);
        Double time2 = Double.valueOf(t2);
        Double total = time1 + time2;
        String totalt = total.toString();
        out = estimtime(totalt);


        return out;
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }
}
