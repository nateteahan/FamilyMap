package service;

import java.util.ArrayList;

import dataAccess.AuthTokenDAO;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import model.Event;
import model.User;
import response.EventResponse;

/**
 * Service class for Event. Performs one request and returns an EventResponse object.
 */
public class EventService {
    /**
     * Returns ALL events for ALL family members of the current user. The current
     user is determined from the provided auth token.
     *
     * @return  EventResponse object
     */
    public EventResponse event(String eventIdentification, String authenticationToken) throws DatabaseException {
        EventResponse responseData = new EventResponse();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        UserDAO userDAO = new UserDAO();
        EventDAO eventDAO = new EventDAO();

        try {
            //Check to see if id == "" for ALL persons or just a single one
            if (!eventIdentification.equals("")) {
                //If authenticationToken exists in database
                if (authTokenDAO.checkAuthToken(authenticationToken)) {
                    //If eventID is valid
                    if (eventDAO.checkEventID(eventIdentification)) {
                        AuthToken authToken = authTokenDAO.getUserToken(authenticationToken);
                        User user = userDAO.getUser(authToken.getUsername());               //CHECK THIS SHIZZZZZZZZ
                        Event event = eventDAO.getEvent(eventIdentification);

                        if (user.getUsername().equals(event.getDescendant())) {
                            responseData.setEvent(event);
                            responseData.setData(null);
                            responseData.setOutputMessage(null);
                        } else {
                            responseData.setEvent(null);
                            responseData.setData(null);
                            responseData.setOutputMessage("AuthToken and eventID do not match the same event in the database");
                        }
                    }
                    else {
                        responseData.setEvent(null);
                        responseData.setData(null);
                        responseData.setOutputMessage("eventID does not match any event in the database");
                    }
                }
                else {
                    responseData.setEvent(null);
                    responseData.setData(null);
                    responseData.setOutputMessage("AuthToken does not exist in the database");
                }
            }
            else {
                if (authTokenDAO.checkAuthToken(authenticationToken)) {
                    AuthToken authToken = authTokenDAO.getUserToken(authenticationToken);
                    User user = userDAO.getUser(authToken.getUsername());
                    Event event = eventDAO.getEventFromDescendant(user.getUsername());                //CHECKKKK THIS SHIZ!!!! FIXME if needed

                    ArrayList<Event> allEventFamily = new ArrayList<>();
                    allEventFamily = eventDAO.getAllPeople(event.getDescendant());
                    Event[] eventData = new Event[allEventFamily.size()];

                    for (int i = 0; i < allEventFamily.size(); i++) {
                        eventData[i] = allEventFamily.get(i);
                    }

                    responseData.setEvent(null);
                    responseData.setData(eventData);
                    responseData.setOutputMessage(null);

                } else {
                    responseData.setEvent(null);
                    responseData.setData(null);
                    responseData.setOutputMessage("AuthToken doesn't exist in the database");
                }
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
