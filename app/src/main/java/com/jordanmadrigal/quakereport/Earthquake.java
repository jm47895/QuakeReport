package com.jordanmadrigal.quakereport;

public class Earthquake {

    private double mag;
    private String city;
    private long date;
    private String subLocation;

    /**
     *
     * @param mag is the magnitude of the erathquake
     * @param city is the city of reference for earthquake
     * @param date date and time of occurrence
     * @param subLocation is the relative location to city
     */

    public Earthquake(double mag, String city, long date, String subLocation) {
        this.mag = mag;
        this.city = city;
        this.date = date;
        this.subLocation = subLocation;
    }



    public double getMag() {return mag;}

    public String getCity() {
        return city;
    }

    public long getDate() {return date;}

    public String getSubLocation() {
        return subLocation;
    }
}

