package com.example.ashis.earthquake;

/**
 * Created by ashis on 26-03-2018.
 */


/**
 * Created by ashis on 26-03-2018.
 */

public class Earthquake {
    private String location;
    private Long timeInMilliseconds;
    private Double eMagnitude;
    private String url;

    Earthquake(Double mag, String loc, Long date,String Url) {
        eMagnitude = mag;
        location = loc;
        timeInMilliseconds = date;
        url=Url;
    }

    public String getLocation() {
        return location;
    }

    public Long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public Double getEMagnitude() {
        return eMagnitude;
    }

    public String getUrl(){return url;}


}
