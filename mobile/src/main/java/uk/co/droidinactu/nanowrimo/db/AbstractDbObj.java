package uk.co.droidinactu.nanowrimo.db;


import java.util.UUID;

/**
 * Created by aspela on 31/08/16.
 */

public abstract class AbstractDbObj {

    private String uuid=null;

    public String getId() {
        return uuid;
    }

    public void setId(final String id) {
        this.uuid = id;
    }
}
