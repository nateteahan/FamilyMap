package com.example.natethewizard.fms_server;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {
    private Button mSubmit;
    private String mQuery;
    private EditText mSearchString;
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();
    private Map<String, Person> persons = singleton.getPeople();
    private Map<String, Event> events = singleton.getEvents();
    private List<Person> matchingPersonQuery = new ArrayList<>();
    private List<Event> matchingEventQuery = new ArrayList<>();
    private List<Object> searchResults = new ArrayList<>();
    private RecyclerView mSearchRecyclerView;
    private SearchAdapter mAdapter;
    private List<String> momSide = singleton.getMaternalAncestors();
    private List<String> dadSide = singleton.getPaternalAncestors();
    private List<Filter> currentFilterSettings = singleton.getFilters();
    private Filter maleFilter;
    private Filter femaleFilter;
    private Filter fatherFilter;
    private Filter motherFilter;

    private class SearchResultHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private ImageView mImageView;

        public SearchResultHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_search, parent, false));

            mNameView = (TextView) itemView.findViewById(R.id.item_name);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        protected void bind(Object obj) {
            Drawable drawableIcon;

            if (obj.getClass() == Person.class) {
                final Person person = (Person) obj;

                mNameView.setText(person.getFirstName() + " " + person.getLastName());

                String personGender = person.getGender();

                if (personGender.equals("f")) {
                    drawableIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(40);
                } else {
                    drawableIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                }

                mImageView.setImageDrawable(drawableIcon);

                mNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        singleton.setUser(person);

                        Intent intent = new Intent(getApplication(), PersonActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                final Event event = (Event) obj;
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

                drawableIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.event_icon).sizeDp(40);

                mImageView.setImageDrawable(drawableIcon);

                mNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        singleton.setCurrentEvent(event);
                        Intent intent = new Intent(getApplication(), EventActivity.class);
                        startActivity(intent);
                    }
                });

            }
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchResultHolder> {

        private List<Object> results = new ArrayList<>();

        public SearchAdapter(List<Object> searchResult) {
            results = searchResult;
        }

        @NonNull
        @Override
        public SearchResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);

            return new SearchResultHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultHolder searchResultHolder, int position) {
            //bind function
            Object object = searchResults.get(position);
            searchResultHolder.bind(object);
        }

        @Override
        public int getItemCount() {
            //Return total number of persons and events combined
            int size = results.size();

            return size;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchString = (EditText) findViewById(R.id.edit_text_view);
        mSearchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mQuery = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Leave blank intentionally
            }
        });

        mSubmit = (Button) findViewById(R.id.submit_search_button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do search stuff here
                //function to check persons names and mQuery
                checkPersonQuery();
                checkEventQuery();

                //Set up recyclerview... check 2 lines down --> context:this
                mSearchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
                mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                updateUI();
            }
        });
    }

    private void updateUI() {
        //PUT IN THE FILTERED PEOPLE AND EVENTS
        //Clear searchResults so multiple search query results don't overlap
        searchResults.clear();

        searchResults.addAll(matchingPersonQuery);
        searchResults.addAll(matchingEventQuery);

        searchResults = updateSearchResults(searchResults);

        mAdapter = new SearchAdapter(searchResults);
        mSearchRecyclerView.setAdapter(mAdapter);
    }

    private void checkPersonQuery() {
        //Clear the previous results so that they wont be stored again on another submit
        matchingPersonQuery.clear();

        for (Map.Entry<String, Person> entry : persons.entrySet()) {
            Person person = entry.getValue();
            String query = mQuery.toLowerCase();
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();

            if (firstName.contains(query) || lastName.contains(query)) {
                matchingPersonQuery.add(person);
            }

        }
    }

    private void checkEventQuery() {
        //Clear the previous results so that they wont be stored again on another submit
        matchingEventQuery.clear();

        for (Map.Entry<String, Event> entry : events.entrySet()) {
            Event event = entry.getValue();
            String countryName = event.getCountry().toLowerCase();
            String cityName = event.getCity().toLowerCase();
            String eventType = event.getEventType().toLowerCase();
            String eventYear = Integer.toString(event.getYear());
            String query = mQuery.toLowerCase();

            if (countryName.contains(query) || cityName.contains(query)
                    || eventType.contains(query) || eventYear.contains(query)) {
                matchingEventQuery.add(event);
            }


        }
    }

    private List<Object> updateSearchResults(List<Object> oldSearchResults) {
        List<Object> updatedSearchResults = new ArrayList<>();
        updatedSearchResults.clear();

        for (int i = 0; i < oldSearchResults.size(); i++) {
            if (oldSearchResults.get(i).getClass() == Person.class) {
                Person p = (Person) oldSearchResults.get(i);

                // Check against male and female filter
                // Check against mom and dad side
                String currentPersonID = p.getPersonID();
                String currentPersonGender = p.getGender();

                getFilterIndices();

                if (validPersonCheck(currentPersonID, currentPersonGender)) {
                    updatedSearchResults.add(p);
                }
            } else {
                Event e = (Event) oldSearchResults.get(i);
                String eventTypeLowerCase = e.getEventType().toLowerCase();
                String personID = e.getPersonID();
                Person eventPerson = persons.get(personID);
                String personGender = eventPerson.getGender();

                boolean eventChecksPassed = true;
                boolean personCheckPassed = true;
                getFilterIndices();

                for (int j = 0; j < currentFilterSettings.size(); j++) {
                    Filter currFilter = currentFilterSettings.get(j);
                    if (eventTypeLowerCase.equals(currFilter.getEventType())) {
                        if (!currFilter.isChecked()) {
                            eventChecksPassed = false;
                        }

                        personCheckPassed = validPersonCheck(personID, personGender);

                    }
                }
                if (eventChecksPassed && personCheckPassed) {
                    updatedSearchResults.add(e);
                }

            }
        }
        return updatedSearchResults;
    }

    private void getFilterIndices() {
        int maleFilterIndex = 0;
        for (int j = 0; j < currentFilterSettings.size(); j++) {
            if (currentFilterSettings.get(j).getEventType().equals("male")) {
                maleFilterIndex = j;
            }
        }
        maleFilter = currentFilterSettings.get(maleFilterIndex);

        int femaleFilterIndex = 0;
        for (int j = 0; j < currentFilterSettings.size(); j++) {
            if (currentFilterSettings.get(j).getEventType().equals("female")) {
                femaleFilterIndex = j;
            }
        }
        femaleFilter = currentFilterSettings.get(femaleFilterIndex);

        int fatherFilterIndex = 0;
        for (int j = 0; j < currentFilterSettings.size(); j++) {
            if (currentFilterSettings.get(j).getEventType().equals("father's side")) {
                fatherFilterIndex = j;
            }
        }
        fatherFilter = currentFilterSettings.get(fatherFilterIndex);

        int motherFilterIndex = 0;
        for (int j = 0; j < currentFilterSettings.size(); j++) {
            if (currentFilterSettings.get(j).getEventType().equals("mother's side")) {
                motherFilterIndex = j;
            }
        }
        motherFilter = currentFilterSettings.get(motherFilterIndex);
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
