package service;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import location.LocationArray;
import location.NameArray;
import model.Event;
import model.Person;
import model.User;
import response.FillResponse;

/**
 * Service class for Fill. Performs one request and returns an FillResponse object.
 */
public class FillService {
        int numPeopleAdded = 0;
        int numEventsAdded = 0;

    /**
     * Populates the server.server's database with generated data for the specified user name.
     *
     * @return  FillResponse object
     */
    public FillResponse fill(String userName, int numGenerations) throws DatabaseException {
        // UserDAO and User objects to access database. Check for existence.
        UserDAO userDAO = new UserDAO();
        User userToFill = userDAO.getUser(userName);
        FillResponse response = new FillResponse();

        // If both of the provided parameters were bad input
        if (userToFill == null && numGenerations < 0) {
            response.setOutputMessage("Both the userName and the number of generations that were provided are erroneous. Please supply valid input.");
        }
        // User did not previously exist in the database
        else if (userToFill == null) {
            response.setOutputMessage("This user is not registered in the database. Try again.");
        }
        // The number of generations to fill was a negative number
        else if (numGenerations < 0) {
            response.setOutputMessage("Please provide a non-negative integer for the number of generations to fill.");
        }
        // User exists in the database and the number of generations to fill is a positive number
        else {
            PersonDAO personDAO = new PersonDAO();
            EventDAO eventDAO = new EventDAO();
            // Get the correct person from the database using the personID that has been generated for them. Make sure personID is being correctly inserted in database.
            Person currentPerson = personDAO.getPerson(userToFill.getPersonID());
            eventDAO.deleteDescendantEvents(currentPerson.getDescendant());
            //Delete the person data from database
            personDAO.deletePerson(currentPerson.getDescendant());

            // Create a year for the current user. Can be up to 50 years old. After that, they don't even know how to work a computer anyway...
            Random rand = new Random();
            int randomYear = rand.nextInt(51);
            int currentUserBirthYear = 2019 - randomYear;

            //Create birth event for root user
            String locationFilePath = "/Users/NatetheWizard/AndroidStudioProjects/Family_Map/Server/src/main/json/locations.json";

            try {
                Gson gsonObj = new Gson();
                LocationArray locations = gsonObj.fromJson(new FileReader(locationFilePath), LocationArray.class);

                int randomLocationIndex = rand.nextInt(978);
                String rootBirthCity = locations.getLocations()[randomLocationIndex].getCity();
                String rootBirthCountry = locations.getLocations()[randomLocationIndex].getCountry();
                Double rootBirthLatitude = locations.getLocations()[randomLocationIndex].getLatitude();
                Double rootBirthLongitude = locations.getLocations()[randomLocationIndex].getLongitude();
                String rootBirthEventID = UUID.randomUUID().toString();

                Event event = new Event(rootBirthEventID, currentPerson.getDescendant(), currentPerson.getPersonID(),
                                        rootBirthLatitude, rootBirthLongitude, rootBirthCountry, rootBirthCity, "birth", currentUserBirthYear);
                eventDAO.createEvent(event);
                numEventsAdded++;
            } catch (IOException e) {
                e.printStackTrace();
            }

            generateParents(currentPerson, numGenerations, currentUserBirthYear);

            response.setOutputMessage("Successfully added " + numPeopleAdded + " persons and " + numEventsAdded + " events to the database");
        }

        return response;
    };

    private void generateParents(Person p, int userGenerations, int currentYear) {
        String locationFile = "/Users/NatetheWizard/AndroidStudioProjects/Family_Map/Server/src/main/json/locations.json";
        String femaleNameFile = "/Users/NatetheWizard/AndroidStudioProjects/Family_Map/Server/src/main/json/fnames.json";
        String maleNameFile = "/Users/NatetheWizard/AndroidStudioProjects/Family_Map/Server/src/main/json/mnames.json";
        String lastNameFile = "/Users/NatetheWizard/AndroidStudioProjects/Family_Map/Server/src/main/json/snames.json";

        // Base case
        if (userGenerations == -1) {
            return;
        }

        try {
            // Get arrays for female, male, and last names. Also an array for locations.
            Gson myGson = new Gson();
            NameArray femaleNames = myGson.fromJson(new FileReader(femaleNameFile), NameArray.class);
            NameArray maleNames = myGson.fromJson(new FileReader(maleNameFile), NameArray.class);
            NameArray lastNames = myGson.fromJson(new FileReader(lastNameFile), NameArray.class);
            LocationArray locations = myGson.fromJson(new FileReader(locationFile), LocationArray.class);

            /* DAD STUFF */
            /* BIRTH EVENT */
            //Create Dad's name
            Random rand = new Random();
            int randomIndex = rand.nextInt(144);
            String dadFirstName = maleNames.getData(randomIndex);
            randomIndex = rand.nextInt(152);
            String dadLastName = lastNames.getData(randomIndex);

            //Create random location
            randomIndex = rand.nextInt(978);
            String dadBirthCity = locations.getLocations()[randomIndex].getCity();
            String dadBirthCountry = locations.getLocations()[randomIndex].getCountry();
            Double dadBirthLatitude = locations.getLocations()[randomIndex].getLatitude();
            Double dadBirthLongitude = locations.getLocations()[randomIndex].getLongitude();

            //Make dad's birth year --> Parents are 21-40 years older than child,
            randomIndex = rand.nextInt(21) + 21;
            int dadBirthYear = currentYear - randomIndex;

            //Generate an EventID and personID for dad
            String dadBirthEventID = UUID.randomUUID().toString();
            String dadPersonID = UUID.randomUUID().toString();

            //Create event and dad's personID.
            Event birthEvent = new Event(dadBirthEventID, p.getDescendant(), dadPersonID, dadBirthLatitude, dadBirthLongitude, dadBirthCountry, dadBirthCity, "birth", dadBirthYear);
            Person dad = new Person(dadPersonID, p.getDescendant(), dadFirstName, dadLastName, "m", null, null,  null);


            /* BAPTISM EVENT */
            // User will get baptized in different place than birth.
            randomIndex = rand.nextInt(978);
            String baptismCity = locations.getLocations()[randomIndex].getCity();
            String baptismCountry = locations.getLocations()[randomIndex].getCountry();
            Double baptismLatitude = locations.getLocations()[randomIndex].getLatitude();
            Double baptismLongitude = locations.getLocations()[randomIndex].getLongitude();

            // Create baptism year. A person will get baptized between 0-10 years old.
            int baptismYear = rand.nextInt(11);
            baptismYear += dadBirthYear;

            // Generate baptismEventID
            String baptismEventID = UUID.randomUUID().toString();

            //Create an event for dad's baptism
            Event baptismEvent = new Event(baptismEventID, p.getDescendant(), dadPersonID, baptismLatitude, baptismLongitude, baptismCountry, baptismCity, "baptism", baptismYear);


            /* MARRIAGE EVENT */
            //Get married in same place as birth
            //Get married between 18-35
            int dadMarriageYear = rand.nextInt(28) + 18;
            dadMarriageYear += dadBirthYear;

            //Generate eventID
            String dadMarriageEventID = UUID.randomUUID().toString();

            //Create an event for dad's marriage --> Will be the same as the mom's year of marriage
            Event marriageEvent = new Event(dadMarriageEventID, p.getDescendant(), dadPersonID, dadBirthLatitude, dadBirthLongitude, dadBirthCountry, dadBirthCity, "marriage", dadMarriageYear);

            //Insert Dad events into database
            EventDAO eventDataAccess = new EventDAO();
            if (userGenerations > 0) {
                eventDataAccess.createEvent(birthEvent);
                eventDataAccess.createEvent(baptismEvent);
                eventDataAccess.createEvent(marriageEvent);
                numEventsAdded += 3;
            }


            /* MOM STUFF */
            /* BIRTH EVENT */
            randomIndex = rand.nextInt(147);
            String momFirstName = femaleNames.getData(randomIndex);
            randomIndex = rand.nextInt(152);
            String momLastName = lastNames.getData(randomIndex);

            //Create random location
            randomIndex = rand.nextInt(978);
            String city = locations.getLocations()[randomIndex].getCity();
            String country = locations.getLocations()[randomIndex].getCountry();
            Double latitude = locations.getLocations()[randomIndex].getLatitude();
            Double longitude = locations.getLocations()[randomIndex].getLongitude();

            //Make mom's birth year --> Parents are 21-40 years older than child,
            randomIndex = rand.nextInt(21) + 21;
            int momBirthYear = currentYear - randomIndex;

            //Generate an EventID and personID for mom
            String momBirthEventID = UUID.randomUUID().toString();
            String momPersonID = UUID.randomUUID().toString();

            //Create event and dad's personID.
            Event momBirthEvent = new Event(momBirthEventID, p.getDescendant(), momPersonID, latitude, longitude, country, city, "birth", momBirthYear);
            Person mom = new Person(momPersonID, p.getDescendant(), momFirstName, momLastName, "f", null, null,  null);


            /* MOM BAPTISM EVENT */
            // User will get baptized in different place than birth.
            randomIndex = rand.nextInt(978);
            baptismCity = locations.getLocations()[randomIndex].getCity();
            baptismCountry = locations.getLocations()[randomIndex].getCountry();
            baptismLatitude = locations.getLocations()[randomIndex].getLatitude();
            baptismLongitude = locations.getLocations()[randomIndex].getLongitude();

            // Create baptism year. A person will get baptized between 0-10 years old.
            baptismYear = rand.nextInt(11);
            baptismYear += momBirthYear;

            // Generate baptismEventID
            String momBaptismEventID = UUID.randomUUID().toString();

            //Create an event for mom's baptism
            Event momBaptismEvent = new Event(momBaptismEventID, p.getDescendant(), momPersonID, baptismLatitude, baptismLongitude, baptismCountry, baptismCity, "baptism", baptismYear);


            /* MARRIAGE EVENT */
            //Generate eventID
            String momMarriageEventID = UUID.randomUUID().toString();

            //Create an event for dad's marriage --> Will be the same as the mom's year of marriage
            Event momMarriageEvent = new Event(momMarriageEventID, p.getDescendant(), momPersonID, dadBirthLatitude, dadBirthLongitude, dadBirthCountry, dadBirthCity, "marriage", dadMarriageYear);

            //Insert Mom events into database
            if (userGenerations > 0) {
                eventDataAccess.createEvent(momBirthEvent);
                eventDataAccess.createEvent(momBaptismEvent);
                eventDataAccess.createEvent(momMarriageEvent);
                numEventsAdded += 3;
            }


            //ID's
            mom.setSpouse(dadPersonID);
            dad.setSpouse(momPersonID);

            //Don't allow last generation to have a mother of father ID
            if (userGenerations > 0) {
                p.setMother(momPersonID);
                p.setFather(dadPersonID);
            }

            //Insert mom & dad and current user into the database
            PersonDAO personDataAccess = new PersonDAO();
            personDataAccess.createPerson(p);
            //increment users added
            numPeopleAdded++;

            //Decrement number of generations, pass it in as the new parameter.
            userGenerations--;

            generateParents(mom, userGenerations, momBirthYear);
            generateParents(dad, userGenerations, dadBirthYear);


        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception f) {
            f.printStackTrace();
        }
    }


}
