package com.example.natethewizard.fms_server;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import model.Filter;
import model.ModelSingleton;

public class FiltersActivity extends AppCompatActivity {
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();
    private List<String> eventTypes = singleton.getEventTypes();
    private RecyclerView mFilterRecycler;
    private FilterAdapter mFilterAdapter;
    private List<Filter> adjustedFilters = singleton.getFilters();


    private class FilterHolder extends RecyclerView.ViewHolder {
        private TextView mEventType;
        private TextView mFilterEvent;
        private Switch mEnableSwitch;

        public FilterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.filter_item_frame, parent, false));

            mEventType = itemView.findViewById(R.id.event_type);
            mFilterEvent = itemView.findViewById(R.id.filter_by_event);
            mEnableSwitch = itemView.findViewById(R.id.filter_switch);

            if (mEnableSwitch.isChecked()) {
                mEnableSwitch.setChecked(true);
            } else {
                mEnableSwitch.setChecked(false);
            }

        }

        protected void bind(final String givenEventType) {
            //Capitalize first letter and set the rest of the substring to lowercase... kinda messy
            StringBuilder s = new StringBuilder(givenEventType.toLowerCase());

            Character c = givenEventType.toUpperCase().charAt(0);
            s.setCharAt(0, c);

            String eventType = s.toString();
            StringBuilder sb = new StringBuilder();
            sb.append(eventType + " Events");
            mEventType.setText(sb.toString());

            sb.setLength(0);
            eventType = eventType.toUpperCase();
            sb.append("FILTER BY " + eventType + " EVENTS");
            mFilterEvent.setText(sb.toString());

            // Check to see if the current filter is set to true or false
            // set the mEnableSwitch accordingly
            eventType = givenEventType.toLowerCase();
            int filterPosition = 0;
            for (int i = 0; i < adjustedFilters.size(); i++) {
                Filter checkFilter = adjustedFilters.get(i);
                if (eventType.equals(checkFilter.getEventType())) {
                    filterPosition = i;
                }
            }

            boolean validEvent = adjustedFilters.get(filterPosition).isChecked();

            if (validEvent) {
                mEnableSwitch.setChecked(true);
            }
            else {
                mEnableSwitch.setChecked(false);
            }

            mEnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Since loginFragment converts all event types to lower case.
                    //find the correct position in the list of filters based off event type
                    //change that specific filters boolean 'isChecked' value
                    String eventClicked = givenEventType.toLowerCase();
                    int correctFilterPosition = 0;

                    for (int i = 0; i < adjustedFilters.size(); i++) {
                        String searchedEvent = adjustedFilters.get((i)).getEventType();
                        if (eventClicked.equals(searchedEvent)) {
                            correctFilterPosition = i;
                        }
                    }
                    adjustedFilters.get(correctFilterPosition).setChecked(isChecked);
                }
            });
            //Set the singleton filters accordingly so I can have them when I draw the map markers
            singleton.setFilters(adjustedFilters);
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {

        public FilterAdapter() {
            //Leave blank because we already have event types from the singleton on line 20
        }

        @NonNull
        @Override
        public FilterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(FiltersActivity.this);

            return new FilterHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull FilterHolder filterHolder, int position) {
            String currentEventType = adjustedFilters.get(position).getEventType();
            filterHolder.bind(currentEventType);
        }

        @Override
        public int getItemCount() {
            return adjustedFilters.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFilterRecycler = findViewById(R.id.filter_recycler_view);
        mFilterRecycler.setLayoutManager(new LinearLayoutManager(FiltersActivity.this));

        mFilterAdapter = new FilterAdapter();
        mFilterRecycler.setAdapter(mFilterAdapter);
    }
}
