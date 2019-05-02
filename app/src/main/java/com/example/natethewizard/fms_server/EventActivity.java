package com.example.natethewizard.fms_server;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.HashMap;
import java.util.Map;

import model.Event;
import model.MapColor;
import model.ModelSingleton;
import model.Person;

public class EventActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();
    private Event currentEvent = singleton.getCurrentEvent();
    private Map<String, Event> events = singleton.getEvents();
    private Map<String, MapColor> eventColors = singleton.getEventTypeColors();
    private Map<String, Person> persons = singleton.getPeople();
    private TextView personTextView;
    private ImageView personImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        singleton.setFromEventActivity(true);
        fm.beginTransaction().add(R.id.my_map_frag, fragment).commit();
    }
}
