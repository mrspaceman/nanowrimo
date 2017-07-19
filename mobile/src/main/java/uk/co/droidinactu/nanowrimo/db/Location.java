package uk.co.droidinactu.nanowrimo.db;

/**
 * Created by aspela on 11/04/17.
 */
public class Location extends AbstractDbObj {
    private static final String LOG_TAG = Location.class.getSimpleName() + ":";

    private String name;
    private double latitude;
    private double longitude;
    private String address = null;
    private String website = null;


    public Location() {
    }

    public Location(final String aName, final double aLat, final double aLong) {
        name = aName.trim();
        latitude = aLat;
        longitude = aLong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
