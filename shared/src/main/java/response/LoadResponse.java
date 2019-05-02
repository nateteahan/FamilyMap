package response;

/**
 * Getters and setters for:
 * addSuccess       if load function worked properly
 * outputMessage    Communicate to user if there was an error
 */
public class LoadResponse {
    private String outputMessage;


    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }
}
