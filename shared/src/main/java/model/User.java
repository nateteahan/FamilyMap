package model;

/**
 * Contain the appropriate data for each user. Getters and setters provided.
 *
 * userName: Unique identifier for this person (non-empty string)
 * password: User (Username) to which this person belongs
 * email_address: email address of the user
 * First Name: Person’s first name (non-empty string)
 * Last Name: Person’s last name (non-empty string)
 * Gender: Person’s gender (string: “f” or “m”)
 * Person: ID of person
 */

public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * Default constructor for User if no parameters are provided. This is here only as a stub, most likely will be removed.
     */
    public User() {};

    /**
     * Constructor
     * @param username  unique userName (non-empty string)
     * @param password  user's password (non-empty string)
     * @param email_address User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName  User’s last name (non-empty string)
     * @param gender    User’s gender (string: “f” or “m”)
     * @param personID  Unique Person ID assigned to this user’s generated Person object
     */
    public User(String username, String password, String email_address, String firstName, String lastName, String gender, String personID) {
        setUsername(username);
        setPassword(password);
        setEmail_address(email_address);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setPersonID(personID);
    };

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail_address() {
        return email;
    }

    public void setEmail_address(String email_address) {
        this.email = email_address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    //NEED AN EQUAL FUNCTION TO COMPARE TO USERS.
}
