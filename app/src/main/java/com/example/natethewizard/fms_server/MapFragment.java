package com.example.natethewizard.fms_server;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.Event;
import model.Filter;
import model.MapColor;
import model.ModelSingleton;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();
    private Map<String, Person> persons = singleton.getPeople();
    private HashMap<Marker, Event> markerToEvent = new HashMap<>();
    private ImageView personImageView;
    private TextView personTextView;
    final Map<String, Event> events = singleton.getEvents();
    private HashMap<String, MapColor> eventColors = new HashMap<>();
    private List<Filter> currentFilterStatus = singleton.getFilters();
    private List<String> mTypes = singleton.getEventTypes();
    private Event currentEvent = singleton.getCurrentEvent();
    private int fatherSideIndex = 0;
    private int motherSideIndex = 0;
    private int maleIndex = 0;
    private int femaleIndex = 0;
    private Filter fatherFilter;
    private Filter motherFilter;
    private Filter maleFilter;
    private Filter femaleFilter;
    private List<String> dadSide = singleton.getPaternalAncestors();
    private List<String> momSide = singleton.getMaternalAncestors();
    private int currentMapType;


    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (singleton.isFromEventActivity()) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.map_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.filter:
                intent = new Intent(getActivity(), FiltersActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        personTextView = v.findViewById(R.id.event_display);
        personImageView = v.findViewById(R.id.imageView);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.use_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        mapFragment.getMapAsync(this);

        fatherSideIndex = singleton.findIndex("father's side");
        motherSideIndex = singleton.findIndex("mother's side");
        maleIndex = singleton.findIndex("male");
        femaleIndex = singleton.findIndex("female");

        fatherFilter = currentFilterStatus.get(fatherSideIndex);
        motherFilter = currentFilterStatus.get(motherSideIndex);
        maleFilter = currentFilterStatus.get(maleIndex);
        femaleFilter = currentFilterStatus.get(femaleIndex);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMap != null) {
            mMap.clear();
            currentMapType = singleton.getMapType();
            mMap.setMapType(currentMapType);
            List<String> fatherSidePersons = singleton.getPaternalAncestors();
            List<String> motherSidePersons = singleton.getMaternalAncestors();

            // Add event markers from data in the model
            for (Map.Entry<String, Event> entry : events.entrySet()) {
                //for each event grab its longitude and latitude
                Event event = entry.getValue();
                String currentPersonID = event.getPersonID();
                Person eventsPerson = persons.get(currentPersonID);

                //Check to see if a filter has been applied
                //Only place events whose filter type is set to true
                //Get position of event type in the filter list
                //Check to see if that position is checked or not
                String eventType = event.getEventType().toLowerCase();
                int filterPosition = 0;
                for (int i = 0; i < currentFilterStatus.size(); i++) {
                    Filter checkFilter = currentFilterStatus.get(i);
                    if (eventType.equals(checkFilter.getEventType())) {
                        filterPosition = i;
                    }
                }

                Filter currentFilter = currentFilterStatus.get(filterPosition);

                boolean validEvent = currentFilter.isChecked();
                if (!validEvent) {
                    continue;
                }

                if (!fatherFilter.isChecked()) {
                    boolean onFatherSide = false;
                    for (int i = 0; i < fatherSidePersons.size(); i++) {
                        String idToCheck = fatherSidePersons.get(i);

                        if (currentPersonID.equals(idToCheck)) {
                            onFatherSide = true;
                        }
                    }
                    if (onFatherSide) {
                        continue;
                    }
                }
                if (!motherFilter.isChecked()) {
                    boolean onMotherSide = false;
                    for (int i = 0; i < motherSidePersons.size(); i++) {
                        if (event.getPersonID().equals(motherSidePersons.get(i))) {
                            onMotherSide = true;
                        }
                    }

                    if (onMotherSide) {
                        continue;
                    }
                }
                if (!maleFilter.isChecked() && eventsPerson.getGender().equals("m")) {
                    continue;
                }
                if (!femaleFilter.isChecked() && eventsPerson.getGender().equals("f")) {
                    continue;
                }

                String city = event.getCity();
                float markerColor = eventColors.get(event.getEventType().toLowerCase()).getColor();

                LatLng newLocation = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(newLocation).title(city).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                markerToEvent.put(marker, event);
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Marker clickedMarker = marker;
                    Event eventInformation = markerToEvent.get(clickedMarker);
                    String personId = eventInformation.getPersonID();
                    Person personToDisplay = persons.get(personId);

                    StringBuilder sb = new StringBuilder();
                    sb.append(personToDisplay.getFirstName() + " " + personToDisplay.getLastName());
                    sb.append('\n' + eventInformation.getEventType() + ": " + eventInformation.getCity() + ", ");
                    sb.append(eventInformation.getCountry() + " (" + eventInformation.getYear() + ")");

                    personTextView.setText(sb.toString());

                    Drawable drawableIcon;
                    if (personToDisplay.getGender().equals("f")) {
                        drawableIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                                colorRes(R.color.female_icon).sizeDp(60);
                    } else {
                        drawableIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                                colorRes(R.color.male_icon).sizeDp(60);
                    }

                    personImageView.setImageDrawable(drawableIcon);

                    //Set the singleton person object to the person corresponding to the event selected
                    singleton.setUser(personToDisplay);
                    return true;
                }
            });

            personTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //FIGURE OUT HOW TO DRAW LINES FROM EVENTS
        mMap = googleMap;
        currentMapType = singleton.getMapType();
        mMap.setMapType(currentMapType);
        boolean fromEventActivity = singleton.isFromEventActivity();

        if (fromEventActivity) {
            singleton.setFromEventActivity(false);
            double latitude = currentEvent.getLatitude();
            double longitude = currentEvent.getLongitude();
            String personID = currentEvent.getPersonID();
            Person currentPerson = persons.get(personID);

            StringBuilder build = new StringBuilder();
            build.append(currentPerson.getFirstName() + " " + currentPerson.getLastName());
            build.append('\n' + currentEvent.getEventType() + ": " + currentEvent.getCity() + ", ");
            build.append(currentEvent.getCountry() + " (" + currentEvent.getYear() + ")");

            personTextView.setText(build.toString());

            Drawable drawableIcon;
            if (currentPerson.getGender().equals("f")) {
                drawableIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(60);
            } else {
                drawableIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(60);
            }

            personImageView.setImageDrawable(drawableIcon);

            LatLng currentLocation = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)
                    .zoom(4).tilt(30).build();
            //Zoom in and animate the camera.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        int colorPosition = 0;
        for (Map.Entry<String, Event> entry : events.entrySet()) {
            Event event = entry.getValue();
            String eventType = event.getEventType().toLowerCase();
            if (colorPosition > 9) {
                colorPosition = 0;
            }
            if (!eventColors.containsKey(eventType)) {
                MapColor mapColor = new MapColor();

                mapColor.setColor(mapColor.getFloatColors()[colorPosition]);
                colorPosition++;

                eventColors.put(eventType, mapColor);
            }
        }

        singleton.setEventTypeColors(eventColors);

        //Add event markers from data in the model
        for (Map.Entry<String, Event> entry : events.entrySet()) {
            //for each event grab its longitude and latitude.
            Event event = entry.getValue();
            String city = event.getCity();
            String lowerCase = event.getEventType().toLowerCase();

            float markerColor = eventColors.get(lowerCase).getColor();

            LatLng newLocation = new LatLng(event.getLatitude(), event.getLongitude());

            //Check to see if a filter has been applied
            //Only place events whose filter type is set to true
            //Get position of event type in the filter list
            //Check to see if that position is checked or not
            String eventType = event.getEventType().toLowerCase();
            String personID = event.getPersonID();
            Person personToInspect = persons.get(personID);
            String personGender = personToInspect.getGender();

            int filterPosition = 0;
            for (int i = 0; i < currentFilterStatus.size(); i++) {
                Filter checkFilter = currentFilterStatus.get(i);
                if (eventType.equals(checkFilter.getEventType())) {
                    filterPosition = i;
                }
            }

            boolean passPersonCheck = checkPersonFilters(personID, personGender);

            if (currentFilterStatus.get(filterPosition).isChecked() && passPersonCheck) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(newLocation).title(city).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                markerToEvent.put(marker, event);
            } else {
                // Leave blank intentionally to not leave a marker on the map
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Marker clickedMarker = marker;
                Event eventInformation = markerToEvent.get(clickedMarker);
                singleton.setCurrentEvent(eventInformation);
                String personId = eventInformation.getPersonID();
                Person personToDisplay = persons.get(personId);

                StringBuilder sb = new StringBuilder();
                sb.append(personToDisplay.getFirstName() + " " + personToDisplay.getLastName());
                sb.append('\n' + eventInformation.getEventType() + ": " + eventInformation.getCity() + ", ");
                sb.append(eventInformation.getCountry() + " (" + eventInformation.getYear() + ")");

                personTextView.setText(sb.toString());

                Drawable drawableIcon;
                if (personToDisplay.getGender().equals("f")) {
                    drawableIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(60);
                } else {
                    drawableIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(60);
                }

                personImageView.setImageDrawable(drawableIcon);

                //Set the singleton person object to the person corresponding to the event selected
                singleton.setUser(personToDisplay);
                return true;
            }
        });

        personTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPersonFilters(String personID, String gender) {
        boolean allChecksPassed = true;

        if (!fatherFilter.isChecked()) {
            for (int j = 0; j < dadSide.size(); j++) {
                if (personID.equals(dadSide.get(j))) {
                    allChecksPassed = false;
                }
            }
        }

        if (!motherFilter.isChecked()) {
            for (int j = 0; j < momSide.size(); j++) {
                if (personID.equals(momSide.get(j))) {
                    allChecksPassed = false;
                }
            }
        }

        if (!maleFilter.isChecked()) {
            if (gender.equals("m")) {
                allChecksPassed = false;
            }
        }

        if (!femaleFilter.isChecked()) {
            if (gender.equals("f")) {
                allChecksPassed = false;
            }
        }

        return allChecksPassed;
    }
}

