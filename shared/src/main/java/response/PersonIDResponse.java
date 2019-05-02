package response;

/**
 * Getters and setters for:
 *  Person ID: Unique identifier for this person (non-empty string)
 * Descendant: User (Username) to which this person belongs
 * First Name: Person’s first name (non-empty string)
 * Last Name: Person’s last name (non-empty string)
 * Gender: Person’s gender (string: “f” or “m”)
 * Father: ID of person’s father (possibly null)
 * Mother: ID of person’s mother (possibly null)
 * Spouse: ID of person’s spouse (possibly null)
 * outputMessage   string to communicate to user the result of the response
 */
public class PersonIDResponse {
    private String descendant;
    private int personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String outputMessage;

    public String getDescendant() {
        return descendant;
    }

    public int getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public String getOutputMessage() {
        return outputMessage;
    }
}
