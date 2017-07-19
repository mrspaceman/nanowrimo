package uk.co.droidinactu.nanowrimo.db;

import java.util.List;

/**
 * Created by aspela on 11/04/17.
 */
public class Event extends AbstractDbObj {
    private static final String LOG_TAG = Event.class.getSimpleName() + ":";

    private String name;
    private String datetime;
    private RepeatSchedule repeats = null;
    private String description;
    private Location location;
    private List<String> invited;
    private List<String> goingYes;
    private List<String> goingMbe;
    private List<String> goingNo;


    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String aValue) {
        this.name = aValue;
    }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String aValue) {
        this.datetime = aValue;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location aValue) {
        this.location = aValue;
    }

    public RepeatSchedule getRepeats() {
        return repeats;
    }

    public void setRepeats(RepeatSchedule aValue) {
        this.repeats = aValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String aValue) {
        this.description = aValue;
    }

    public List<String> getInvited() {
        return invited;
    }

    public void setInvited(List<String> aValue) {
        this.invited = aValue;
    }

    public List<String> getGoingYes() {
        return goingYes;
    }

    public void setGoingYes(List<String> aValue) {
        this.goingYes = aValue;
    }

    public List<String> getGoingMbe() {
        return goingMbe;
    }

    public void setGoingMbe(List<String> aValue) {
        this.goingMbe = aValue;
    }

    public List<String> getGoingNo() {
        return goingNo;
    }

    public void setGoingNo(List<String> aValue) {
        this.goingNo = aValue;
    }
}
