package com.example.natethewizard.fms_server;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Filter;
import model.ModelSingleton;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();
    private Person selectedPerson = singleton.getUser();
    private Map<String, Person> persons = singleton.getPeople();
    private List<Pair<Person, String>> personsToDisplay = new ArrayList<>();
    private TextView mLifeEventTextview;
    private TextView mFamilyTextview;
    private RecyclerView mEventRecycler;
    private RecyclerView mPersonRecycler;
    private EventAdapter mEventAdapter;
    private PersonAdapter mPersonAdapter;
    private Map<String, List<Event>> personToEvents = singleton.getPersonEvents();
    private List<Event> eventsToDisplay = personToEvents.get(selectedPerson.getPersonID());
    private Map<String, Person> personToRelatives = singleton.getPeople();
    List<Filter> currentFilterSettings = singleton.getFilters();
    private boolean eventClicked = false;
    private boolean familyClicked = false;
    private int fatherSideIndex = 0;
    private int motherSideIndex = 0;
    private int maleIndex = 0;
    private int femaleIndex = 0;
    Filter fatherFilter;
    Filter motherFilter;
    Filter maleFilter;
    Filter femaleFilter;
    List<String> dadSide = singleton.getPaternalAncestors();
    List<String> momSide = singleton.getMaternalAncestors();

    private class EventHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private ImageView mImageView;
        private String eventID;

        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            //Use same recyclerview for the items as the search activity
            super(inflater.inflate(R.layout.list_item_search, parent, false));

            mNameView = (TextView) itemView.findViewById(R.id.item_name);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        protected void bind(Event e) {
            Drawable drawableIcon;

            final Event event = e;
            String pId = event.getPersonID();
            Person person = new Person();

            for (Map.Entry<String, Person> entry : persons.entrySet()) {
                Person p = entry.getValue();

                if (p.getPersonID().equals(pId)) {
                    person = p;
                }
            }

            StringBuilder sb = new StringBuilder();

            sb.append(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            sb.append('\n' + person.getFirstName() + " " + person.getLastName());

            mNameView.setText(sb.toString());
            drawableIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.event_icon).sizeDp(40);

            mImageView.setImageDrawable(drawableIcon);

            mNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleton.setCurrentEvent(event);
                    Intent intent = new Intent(getApplication(), EventActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {

        private List<Event> lifeEvents = new ArrayList<>();

        public EventAdapter(List<Event> events) {
            lifeEvents = events;
        }

        @NonNull
        @Override
        public EventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(PersonActivity.this);

            return new EventHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull EventHolder eventHolder, int position) {
            Event eventToBind = lifeEvents.get(position);
            eventHolder.bind(eventToBind);
        }

        @Override
        public int getItemCount() {
            return lifeEvents.size();
        }
    }

    private class PersonHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private ImageView mImageView;

        public PersonHolder(LayoutInflater inflater, ViewGroup parent) {
            //Use same recyclerview for the items as the search activity
            super(inflater.inflate(R.layout.list_item_search, parent, false));

            mNameView = (TextView) itemView.findViewById(R.id.item_name);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        protected void bind(Person p, String relation) {
            Drawable drawableIcon;

            final Person person = p;
            String relationToP = relation;

            mNameView.setText(person.getFirstName() + " " + person.getLastName() + '\n' + relationToP);

            String personGender = person.getGender();

            if (personGender.equals("f")) {
                drawableIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            } else {
                drawableIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            }

            mImageView.setImageDrawable(drawableIcon);

            mNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleton.setUser(person);

                    Intent intent = new Intent(getApplication(), PersonActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {

        private List<Pair<Person, String>> family = new ArrayList<>();

        public PersonAdapter(List<Pair<Person, String>> fam) {
            family = fam;
        }

        @NonNull
        @Override
        public PersonHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(PersonActivity.this);

            return new PersonHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonHolder personHolder, int position) {
            Person personToBind = family.get(position).first;
            String relationToPerson = family.get(position).second;
            personHolder.bind(personToBind, relationToPerson);
        }

        @Override
        public int getItemCount() {
            return family.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fatherSideIndex = singleton.findIndex("father's side");
        motherSideIndex = singleton.findIndex("mother's side");
        maleIndex = singleton.findIndex("male");
        femaleIndex = singleton.findIndex("female");

        fatherFilter = currentFilterSettings.get(fatherSideIndex);
        motherFilter = currentFilterSettings.get(motherSideIndex);
        maleFilter = currentFilterSettings.get(maleIndex);
        femaleFilter = currentFilterSettings.get(femaleIndex);

        if (selectedPerson.getMother() != null) {
            Person mother = personToRelatives.get(selectedPerson.getMother());
            Pair pair = new Pair(mother, "Mother");
            personsToDisplay.add(pair);
        }
        if (selectedPerson.getFather() != null) {
            Person father = personToRelatives.get(selectedPerson.getFather());
            Pair pair = new Pair(father, "Father");
            personsToDisplay.add(pair);

        }
        if (selectedPerson.getSpouse() != null) {
            Person spouse = personToRelatives.get(selectedPerson.getSpouse());
            Pair pair = new Pair(spouse, "Spouse");
            personsToDisplay.add(pair);
        }

        for (Map.Entry<String, Person> entry : personToRelatives.entrySet()) {
            Person p = entry.getValue();

            String fatherId = p.getFather();
            String motherId = p.getMother();

            //At any point the selected person's id is the id of a persons father or mother, create a child
            if (fatherId != null && motherId != null) {
                if (fatherId.equals(selectedPerson.getPersonID()) || motherId.equals(selectedPerson.getPersonID())) {
                    Pair pair = new Pair(p, "Child");
                    personsToDisplay.add(pair);
                }
            }
        }


        mFirstName = (TextView) findViewById(R.id.person_firstName);
        mLastName = (TextView) findViewById(R.id.person_lastName);
        mGender = (TextView) findViewById(R.id.person_gender);
        mLifeEventTextview = (TextView) findViewById(R.id.life_events);
        mFamilyTextview = (TextView) findViewById(R.id.family);

        mFirstName.setText(selectedPerson.getFirstName().toString());
        mLastName.setText(selectedPerson.getLastName().toString());

        String genderOfPerson = selectedPerson.getGender().toString();
        if (genderOfPerson.equals("f")) {
            genderOfPerson = "Female";
        } else {
            genderOfPerson = "Male";
        }
        mGender.setText(genderOfPerson);

        mEventRecycler = (RecyclerView) findViewById(R.id.event_recycler_view);
        mEventRecycler.setNestedScrollingEnabled(false);
        mPersonRecycler = (RecyclerView) findViewById(R.id.person_recycler_view);
        mPersonRecycler.setNestedScrollingEnabled(false);

        mEventRecycler.setLayoutManager(new LinearLayoutManager(PersonActivity.this));

        // Update eventsToDisplay based on filter settings
        eventsToDisplay = adjustEventsByFilter(eventsToDisplay);
        mEventAdapter = new EventAdapter(eventsToDisplay);
        mEventRecycler.setAdapter(mEventAdapter);

        mPersonRecycler.setLayoutManager(new LinearLayoutManager(PersonActivity.this));
        mPersonAdapter = new PersonAdapter(personsToDisplay);
        mPersonRecycler.setAdapter(mPersonAdapter);

        mLifeEventTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventClicked) {
                    eventClicked = true;
                    mEventRecycler.setVisibility(View.VISIBLE);
                } else {
                    eventClicked = false;
                    mEventRecycler.setVisibility(View.GONE);
                }
            }
        });

        mFamilyTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!familyClicked) {
                    familyClicked = true;
                    mPersonRecycler.setVisibility(View.VISIBLE);
                } else {
                    familyClicked = false;
                    mPersonRecycler.setVisibility(View.GONE);
                }
            }
        });

    }

    private List<Event> adjustEventsByFilter(List<Event> currentEvents) {
        List<Event> updatedEvents = new ArrayList<>();

        for (int i = 0; i < currentEvents.size(); i++) {
            String personID = currentEvents.get(i).getPersonID();
            Person p = persons.get(personID);
            String personGender = p.getGender();
            String eventType = currentEvents.get(i).getEventType().toLowerCase();
            int eventTypeIndex = 0;

            for (int j = 0; j < currentFilterSettings.size(); j++) {
                if (currentFilterSettings.get(j).getEventType().equals(eventType)) {
                    eventTypeIndex = j;
                }
            }

            Filter eventFilter = currentFilterSettings.get(eventTypeIndex);
            boolean passAllChecks = validPersonCheck(personID, personGender);

            if (eventFilter.isChecked() && passAllChecks) {
                updatedEvents.add(currentEvents.get(i));
            }

        }

        return updatedEvents;
    }

    private boolean validPersonCheck(String personID, String gender) {
        boolean allChecksPassed = true;

        if (!fatherFilter.isChecked()) {
            for (int j = 0; j < dadSide.size(); j++) {
                if (personID.equals(dadSide.get(j))) {
                    allChecksPassed = false;
                }
            }
        }

        if(!motherFilter.isChecked()) {
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
