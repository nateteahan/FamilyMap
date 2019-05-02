package service;

import java.util.ArrayList;

import dataAccess.AuthTokenDAO;
import dataAccess.DatabaseException;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import model.Person;
import model.User;
import response.PersonResponse;

/**
 * Service class for Person. Performs one request and returns an PersonResponse object.
 */
public class PersonService {

    /**
     * Returns ALL family members of the current user. The current user is
     determined from the provided auth token.
     * @param p PersonRequest object
     * @return  PersonResponse object
     */
    public PersonResponse person(String identification, String authenticationToken) throws DatabaseException {
        PersonResponse responseData = new PersonResponse();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        UserDAO userDAO = new UserDAO();
        PersonDAO personDAO = new PersonDAO();

        try {
            //Check to see if id == "" for ALL persons or just a single one
            if (!identification.equals("")) {
                //If authenticationToken exists in database
                if (authTokenDAO.checkAuthToken(authenticationToken)) {
                    //If personID is valid
                    if (personDAO.checkPersonID(identification)) {
                        AuthToken authToken = authTokenDAO.getUserToken(authenticationToken);
                        User user = userDAO.getUser(authToken.getUsername());
                        Person person = personDAO.getPerson(identification);

                        if (user.getUsername().equals(person.getDescendant())) {
                            responseData.setPerson(person);
                            responseData.setData(null);
                            responseData.setOutputMessage(null);
                        } else {
                            responseData.setPerson(null);
                            responseData.setData(null);
                            responseData.setOutputMessage("AuthToken and personID do not match the same user in the database");
                        }
                    }
                    else {
                        responseData.setPerson(null);
                        responseData.setData(null);
                        responseData.setOutputMessage("PersonID does not match anyone in the database");
                    }
                }
                else {
                    responseData.setPerson(null);
                    responseData.setData(null);
                    responseData.setOutputMessage("AuthToken does not exist in the database");
                }
            }
            else {
                if (authTokenDAO.checkAuthToken(authenticationToken)) {
                    AuthToken authToken = authTokenDAO.getUserToken(authenticationToken);
                    User user = userDAO.getUser(authToken.getUsername());
                    Person person = personDAO.getPerson(user.getPersonID());

                    ArrayList<Person> allPeopleFamily = new ArrayList<>();
                    allPeopleFamily = personDAO.getAllPeople(person.getDescendant());
                    Person[] peopleData = new Person[allPeopleFamily.size()];

                    for (int i = 0; i < allPeopleFamily.size(); i++) {
                        peopleData[i] = allPeopleFamily.get(i);
                    }

                    responseData.setPerson(null);
                    responseData.setData(peopleData);
                    responseData.setOutputMessage(null);

                }
                else {
                    responseData.setPerson(null);
                    responseData.setData(null);
                    responseData.setOutputMessage("AuthToken does not exist in the database");
                }
            }
//            if (!identification.equals("")) {
//                AuthToken token = authTokenDAO.getUserToken(authenticationToken);
//                /* Get the user from the database with the username from the authToken */
//                UserDAO userDao = new UserDAO();
//                User user = userDao.getUser(token.getUsername());
//
//                //personID that was provided from server. Use this to compare to the user personID
//                String idToCompare = p.personID;
//
//                //If the input provided a specific person ID --> Do this function. Else, return all persons associated with user
//
//                if (user.getPersonID().equals(idToCompare)) {
//                   PersonDAO pDAO = new PersonDAO();
//                   Person person = pDAO.getPerson(idToCompare);
//
//                   responseData.setPerson(person);
//                   responseData.setData(null);
//                   responseData.setOutputMessage(null);
//
//                } else {
//                 responseData.setPerson(null);
//                 responseData.setData(null);
//                 responseData.setOutputMessage("The given authToken & personID do not match any user in the database.");
//                                }
//            }

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
