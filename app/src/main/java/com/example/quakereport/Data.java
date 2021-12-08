package com.example.quakereport;


public class Data {
    //    stores information about magnitude of earthquake
    private final double magnitude;
    //    store names of nearby place where earthquake occur
    private final String place;
    //    stores date of occurrence of earthquake
    private final long timeinMillisecond;
    //    stores url for usgs website for each earthquake
    private final String url;

    public Data(double magnitude, String place, long timeinMillisecond, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.timeinMillisecond = timeinMillisecond;
        this.url = url;
    }

    //  returns the url of earthquake
    public String getUrl() {
        return url;
    }

    //  returns the magnitude of earthquake
    public double getMagnitude() {
        return magnitude;
    }

    //  returns the place of occurrence of earthquake
    public String getPlace() {
        return place;
    }

    //  returns the date of occurrence of earthquake
    public long getTimeinMillisecond() {
        return timeinMillisecond;

    }

}
