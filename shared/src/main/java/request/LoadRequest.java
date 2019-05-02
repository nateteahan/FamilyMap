package request;

import model.Event;
import model.Person;
import model.User;

/**
 * Send appropriate data to the LoadService class
 */
public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     * Constructor for LoadRequest
     * @param users     user data to put into the database
     * @param persons   person data to put into the database
     * @param events    event data to put into the database
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }
}
