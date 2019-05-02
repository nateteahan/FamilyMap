package response;

/**
 * Getters and setters for:
 * clearSuccess     boolean to determine if clear function worked or not
 * outputMessage    Communicate to user if there was an error
 */
public class ClearResponse {
    private String outputMessage;

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }
}
