package model;

public class Filter {
    private String eventType;
    private boolean isChecked;

    public Filter(String eventType) {
        this.eventType = eventType;
        this.isChecked = true;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
