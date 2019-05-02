package model;

/**
 * Contain the appropriate data for each person. Getters and setters provided.
 *
 * Person ID: Unique identifier for this person (non-empty string)
 * Descendant: User (Username) to which this person belongs
 * First Name: Person’s first name (non-empty string)
 * Last Name: Person’s last name (non-empty string)
 * Gender: Person’s gender (string: “f” or “m”)
 * Father: ID of person’s father (possibly null)
 * Mother: ID of person’s mother (possibly null)
 * Spouse: ID of person’s spouse (possibly null)
 */
public class Person {
    private String personID;
    private String descendant;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    /**
     * Default constructor for Person if no parameters are provided. This is here only as a stub, most likely will be removed.
     */
    public Person() {};

    /**
     * Constructor for Person
     * @param personID      identifier given to each person
     * @param descendant    descendant of the current user
     * @param firstName     first name of current user
     * @param lastName      last name of current user
     * @param gender        gender of current user
     * @param father      identification of the father of the user
     * @param mother      identification of the mother of the user
     * @param spouse      identification of the spouse of the user.
     */
    public Person(String personID, String descendant, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        setPersonID(personID);
        setDescendant(descendant);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFather(father);
        setMother(mother);
        setSpouse(spouse);
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}
