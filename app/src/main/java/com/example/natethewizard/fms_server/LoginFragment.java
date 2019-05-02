package com.example.natethewizard.fms_server;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.ServerProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Filter;
import model.ModelSingleton;
import model.Person;

import request.LoginRequest;
import request.RegisterRequest;
import response.EventResponse;
import response.LoginResponse;
import response.PersonResponse;
import response.RegisterResponse;


public class LoginFragment extends Fragment {

    private EditText mHostNumber;
    private EditText mPortNumber;
    private EditText mUserName;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailAddress;
    private RadioGroup mGender;
    private RadioButton mGenderChecked;
    private Button mLoginButton;
    private Button mRegisterButton;
    private boolean mLoginEnabled;
    private boolean mRegisterEnabled;
    private String mAuthToken;
    private String hostNum;
    private String portNum;
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();
    private List<String> allEvents = new ArrayList<>();
    private List<String> dadSideAncestors = new ArrayList<>();
    private List<String> momSideAncestors = new ArrayList<>();

    public LoginFragment() {
        // Required empty public constructor
    }

    private class LoginAsync extends AsyncTask<LoginRequest, Void, LoginResponse> {

        private LoginAsync() {
            //Blank constructor
        }

        @Override
        protected LoginResponse doInBackground(LoginRequest... requests) {
            //Call the server proxy, return a LoginResponse.. publishProgress(LoginResponse response)
            ServerProxy proxy = new ServerProxy();
            LoginResponse response = proxy.login(hostNum, portNum, requests[0]);

            return response;
        }


        protected void onPostExecute(LoginResponse result) {
            //PRINT TOASTS
            if (result.getOutputMessage() != null) {
                //Print bad toast
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Failed login attempt. Try again!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                //Call async task to get people
                singleton.setmAuthToken(result.getAuthToken());
                singleton.setmPersonID(result.getPersonID());
                new GetDataAsync().execute(result.getAuthToken());
            }
        }
    }

    private class RegisterAsync extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

        private RegisterAsync() {
            //Blank constructor
        }

        @Override
        protected RegisterResponse doInBackground(RegisterRequest... requests) {
            //Call the server proxy, return a LoginResponse.. publishProgress(LoginResponse response)

            ServerProxy proxy = new ServerProxy();
            RegisterResponse response = proxy.register(hostNum, portNum, requests[0]);

            return response;
        }

        protected void onPostExecute(RegisterResponse result) {
            //Do stuff here with the response...
            //PRINT TOASTS
            String resultingMessage = result.getMessage();
            if (resultingMessage != null) {
                //Print bad toast
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Failed register attempt. Try again!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                singleton.setmAuthToken(result.getAuthToken());
                singleton.setmPersonID(result.getPersonID());
                new GetDataAsync().execute(result.getAuthToken());
            }
        }
    }

    private class GetDataAsync extends AsyncTask<String, Void, Boolean> {
        boolean successfulDataRetrieval = false;

        @Override
        protected Boolean doInBackground(String... token) {
            ServerProxy proxy = new ServerProxy();

            PersonResponse personResponse = proxy.getAllPeople(hostNum, portNum, token[0]);

            if (personResponse.getOutputMessage() == null) {
                //Good toast
                Person[] peopleArray = personResponse.getData();
                Person rootUser = peopleArray[0];
                Map<String, Person> persons = new LinkedHashMap<>();

                for (int i = 0; i < peopleArray.length; i++) {
                    persons.put(peopleArray[i].getPersonID(), peopleArray[i]);
                }

                singleton.setPeople(persons);

                // Create the root user and the paternal and maternal side ancestors
                singleton.setUser(rootUser);

                createMaternalSide(rootUser.getMother());
                createPaternalAncestors(rootUser.getFather());

                singleton.setMaternalAncestors(momSideAncestors);
                singleton.setPaternalAncestors(dadSideAncestors);
            }

            EventResponse eventResponse = proxy.getAllEvents(hostNum, portNum, token[0]);

            if (eventResponse.getOutputMessage() == null) {
                //Good toast
                Event[] eventArray = eventResponse.getData();
                Map<String, Event> events = new LinkedHashMap<>();

                for (int i = 0; i < eventArray.length; i++) {
                    events.put(eventArray[i].getEventID(), eventArray[i]);

                    //Insert into List<String> allEvents to keep track of specific event types
                    if (!allEvents.contains(eventArray[i].getEventType().toLowerCase())) {
                        allEvents.add(eventArray[i].getEventType().toLowerCase());
                    }
                }

                singleton.setEvents(events);
                singleton.setEventTypes(allEvents);

                //Set a filter defualt to true for each event given
                List<Filter> defaultFilters = new ArrayList<>();
                for (int i = 0; i < allEvents.size(); i++) {
                    Filter filter = new Filter(allEvents.get(i));
                    defaultFilters.add(filter);
                }

                //Create 4 new filters for gender and mother/father
                Filter motherFilter = new Filter("mother's side");
                Filter fatherFilter = new Filter("father's side");
                Filter femaleFilter = new Filter("female");
                Filter maleFilter = new Filter("male");

                defaultFilters.add(motherFilter);
                defaultFilters.add(fatherFilter);
                defaultFilters.add(femaleFilter);
                defaultFilters.add(maleFilter);

                singleton.setFilters(defaultFilters);
                singleton.setMapType(1);

            }

            if (eventResponse.getOutputMessage() == null && personResponse.getOutputMessage() == null) {
                //populate Map<String, List<Event>>
                Map<String, Person> persons = singleton.getPeople();
                Map<String, Event> events = singleton.getEvents();
                Map<String, List<Event>> personsEvents = new HashMap<>();

                for (Map.Entry<String, Person> entry : persons.entrySet()) {
                    List<Event> userEvents = new ArrayList<>();
                    userEvents.clear();
                    String key = entry.getKey();

                    for (Map.Entry<String, Event> entry1 : events.entrySet()) {
                        Event e = entry1.getValue();
                        if (e.getPersonID().equals(key)) {
                            userEvents.add(e);
                        }
                    }
                    userEvents = sortChronologically(userEvents);
                    personsEvents.put(key, userEvents);
                }

                singleton.setPersonEvents(personsEvents);

                return true;
            } else {
                return false;
            }
        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                MainActivity main = (MainActivity) getActivity();
                main.onLogin();
            }
        }
    }

    private class YearComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            int eventOne = o1.getYear();
            int eventTwo = o2.getYear();

            if (eventOne > eventTwo) {
                return 1;
            } else if (eventOne < eventTwo) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mHostNumber = (EditText) v.findViewById(R.id.host_number);
        mHostNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableLoginButton();
                enableRegisterButton();
                hostNum = s.toString();

                if (!hostNum.equals("")) {
                    singleton.setmHostNumber(hostNum);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Leave blank intentionally
            }
        });

        mPortNumber = (EditText) v.findViewById(R.id.port_number);
        mPortNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableLoginButton();
                enableRegisterButton();
                portNum = s.toString();

                if (!portNum.equals("")) {
                    singleton.setmPortNumber(portNum);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Leave blank intentionally
            }
        });

        mUserName = (EditText) v.findViewById(R.id.user_name);
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableLoginButton();
                enableRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Leave blank intentionally
            }
        });

        mPassword = (EditText) v.findViewById(R.id.password);
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableLoginButton();
                enableRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Leave blank intentionally
            }
        });

        mFirstName = (EditText) v.findViewById(R.id.first_name);
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Leave blank intentionally
            }
        });

        mLastName = (EditText) v.findViewById(R.id.last_name);
        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Leave blank intentionally
            }
        });

        mEmailAddress = (EditText) v.findViewById(R.id.email_address);
        mEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Leave blank intentionally
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Leave blank intentionally
            }
        });

        mGender = (RadioGroup) v.findViewById(R.id.radio_group);
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {               //Check syntax of Radio.OnChe...
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                enableRegisterButton();

                mGenderChecked = (RadioButton) group.findViewById(checkedId);

            }
        });

        mLoginButton = (Button) v.findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create LoginRequest and LoginAsync object. Pass to async function
                LoginRequest request = new LoginRequest(mUserName.getText().toString(), mPassword.getText().toString());
                new LoginAsync().execute(request);
            }
        });

        mRegisterButton = (Button) v.findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do register stuff here
                //Check to see which gender was selected
                String genderChecked = mGenderChecked.getText().toString();
                if (genderChecked.equals("Male")) {
                    genderChecked = "m";
                } else {
                    genderChecked = "f";
                }

                RegisterRequest request = new RegisterRequest(mUserName.getText().toString(), mPassword.getText().toString(), mEmailAddress.getText().toString(),
                        mFirstName.getText().toString(), mLastName.getText().toString(), genderChecked);
                new RegisterAsync().execute(request);
            }
        });

        return v;
    }

    protected List<Event> sortChronologically(List<Event> eventsToSort) {
        List<Event> newEventList = new ArrayList<>();
        Event birthEvent = null;
        Event deathEvent = null;
        List<Event> otherLifeEvents = new ArrayList<>();

        for (int i = 0; i < eventsToSort.size(); i++) {
            Event currentEvent = eventsToSort.get(i);

            if (currentEvent.getEventType().equals("birth")) {
                birthEvent = currentEvent;
                // Strip the birth event if it exists to make sorting algorithm only run on other events
                eventsToSort.remove(currentEvent);
            }
            if (currentEvent.getEventType().equals("death")) {
                deathEvent = currentEvent;
                eventsToSort.remove(currentEvent);
            }
        }

        // Get the sorted chronological list of events beside the birth and death events
        Collections.sort(eventsToSort, new YearComparator());

        if (birthEvent != null) {
            newEventList.add(birthEvent);
        }
        if (eventsToSort != null) {
            for (int i = 0; i < eventsToSort.size(); i++) {
                newEventList.add(eventsToSort.get(i));
            }
        }
        if (deathEvent != null) {
            newEventList.add(deathEvent);
        }

        return newEventList;
    }

    public void enableLoginButton() {
        if (!mHostNumber.getText().toString().isEmpty() && !mPortNumber.getText().toString().isEmpty()
                && !mUserName.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
            mLoginButton.setClickable(true);
            mLoginButton.setEnabled(true);
            mLoginEnabled = true;
        } else {
            mLoginButton.setClickable(false);
            mLoginButton.setEnabled(false);
            mLoginEnabled = false;
        }
    }

    public void enableRegisterButton() {
        //Implement functionality here
        if (mLoginEnabled) {
            if (!mFirstName.getText().toString().isEmpty() && !mLastName.getText().toString().isEmpty()
                    && !mEmailAddress.getText().toString().isEmpty() && mGender.getCheckedRadioButtonId() != -1) {
                mRegisterButton.setClickable(true);
                mRegisterButton.setEnabled(true);
                mRegisterEnabled = true;

            } else {
                mRegisterButton.setClickable(false);
                mRegisterButton.setEnabled(false);
                mRegisterEnabled = false;
            }

        } else {
            mRegisterButton.setClickable(false);
            mRegisterButton.setEnabled(false);
            mRegisterEnabled = false;
        }
    }

    public void createMaternalSide(String rootPersonID) {
        Map<String, Person> personMap = singleton.getPeople();
        Person p = personMap.get(rootPersonID);
        momSideAncestors.add(rootPersonID);

        if (p.getMother() != null) {
            createMaternalSide(p.getMother());
        }

        if (p.getFather() != null) {
            createMaternalSide(p.getFather());
        }
        return;
    }

    public void createPaternalAncestors(String rootPersonID) {
        Map<String, Person> personMap = singleton.getPeople();
        Person p = personMap.get(rootPersonID);
        dadSideAncestors.add(rootPersonID);

        if (p.getMother() != null) {
            createPaternalAncestors(p.getMother());
        }

        if (p.getFather() != null) {
            createPaternalAncestors(p.getFather());
        }
        return;
    }
}