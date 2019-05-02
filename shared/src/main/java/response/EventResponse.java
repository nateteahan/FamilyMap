package response;

import model.Event;

/**
 * Getters and setters for:
 * Event[]      array of Event objects
 * outputMessage    Communicate to user if there was an error
 */
public class EventResponse {
    Event event;
    private Event[] data;
    private String outputMessage;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public Event[] getData() {
        return data;

    }

    public String getOutputMessage() {
        return outputMessage;
    }
}
