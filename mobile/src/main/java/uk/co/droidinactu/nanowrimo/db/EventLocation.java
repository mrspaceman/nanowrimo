package uk.co.droidinactu.nanowrimo.db;

/**
 * Created by aspela on 01/09/16.
 */
public class EventLocation extends AbstractDbObj {

    public final static String COLUMN_NAME_EVENT_ID = "event_id";
    public final static String COLUMN_NAME_LOCATION_ID = "location_id";

    private Event event;
    private Location tag;

    public EventLocation() {
    }

    public EventLocation(Event ebk, Location tg) {
        this.event = ebk;
        this.tag = tg;
    }

    public String toString() {
        return "Book [" + event + "] tag [" + tag + "]";
    }
}
