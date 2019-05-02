package model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ModelSingleton {
    private static User mUser = new User("", "", "", "", "", "", "");
    private static String mHostNumber;
    private static String mPortNumber;
    private static String mAuthToken;
    private static String mPersonID;
    private static final ModelSingleton INSTANCE = new ModelSingleton();

    private ModelSingleton() {
        //Private constructor
    }

    public static ModelSingleton getInstance() {
        return INSTANCE;
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Settings settings;
    private List<Filter> filters;
    private List<String> eventTypes;
    private Map<String, MapColor> eventTypeColors;
    private Person user;                            //This is the root user of the program
    private Event currentEvent;
    private List<String> paternalAncestors;
    private List<String> maternalAncestors;
    private Map<String, List<Person>> personChildren;
    private int mapType;
    private boolean fromEventActivity;

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public static String getmPersonID() {
        return mPersonID;
    }

    public static void setmPersonID(String mPersonID) {
        ModelSingleton.mPersonID = mPersonID;
    }

    public static String getmAuthToken() {
        return mAuthToken;
    }

    public static void setmAuthToken(String mAuthToken) {
        ModelSingleton.mAuthToken = mAuthToken;
    }

    public static User getmUser() {
        return mUser;
    }

    public static void setmUser(User mUser) {
        ModelSingleton.mUser = mUser;
    }

    public static String getmHostNumber() {
        return mHostNumber;
    }

    public void setmHostNumber(String mHostNumber) {
        ModelSingleton.mHostNumber = mHostNumber;
    }

    public static String getmPortNumber() {
        return mPortNumber;
    }

    public void setmPortNumber(String mPortNumber) {
        ModelSingleton.mPortNumber = mPortNumber;
    }

    public static ModelSingleton getINSTANCE() {
        return INSTANCE;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Map<String, List<Event>> personEvents) {
        this.personEvents = personEvents;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Map<String, MapColor> getEventTypeColors() {
        return eventTypeColors;
    }

    public void setEventTypeColors(Map<String, MapColor> eventTypeColors) {
        this.eventTypeColors = eventTypeColors;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public List<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors(List<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public List<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors(List<String> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public Map<String, List<Person>> getPersonChildren() {
        return personChildren;
    }

    public void setPersonChildren(Map<String, List<Person>> personChildren) {
        this.personChildren = personChildren;
    }

    public int findIndex(String filterName) {
        for (int i = 0; i < filters.size(); i++) {
            Filter currFilter = filters.get(i);

            if (currFilter.getEventType().equals(filterName)) {
                return i;
            }
        }
        return 0;
    }

    public boolean isFromEventActivity() {
        return fromEventActivity;
    }

    public void setFromEventActivity(boolean fromEventActivity) {
        this.fromEventActivity = fromEventActivity;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }
}
