package syllander.models;

import java.time.LocalDate;

public class Event {
    private String eventName;
    private LocalDate localDate;

    public Event(String eventName, LocalDate localDate) {
        this.eventName = eventName;
        this.localDate = localDate;
    }
    public Event(){

    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
