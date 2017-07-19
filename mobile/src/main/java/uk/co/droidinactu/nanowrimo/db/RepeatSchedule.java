package uk.co.droidinactu.nanowrimo.db;

/**
 * Created by aspela on 28/06/17.
 */

public class RepeatSchedule {

    private int qty=0;
    private String unit="daily";


    public RepeatSchedule() {
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
