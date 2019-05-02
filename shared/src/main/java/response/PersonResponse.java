package response;

import model.Person;

/**
 * Getters and Setters for:
 * Person[]        array of Person objects
 * outputMessage   string to communicate to user the result of the response
 */
public class PersonResponse {
    Person person;
    private Person[] data;
    private String outputMessage;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }
}
