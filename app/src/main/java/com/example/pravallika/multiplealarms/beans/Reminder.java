package com.example.pravallika.multiplealarms.beans;

/**
 * Created by RitenVithlani on 2/20/17.
 */

public class Reminder {
    private String label;
    private String date;
    private String time;
    private String location;

    public Reminder(String label, String date, String time, String location) {
        this.label = label;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public Reminder(String label, String date, String time) {
        this.label = label;
        this.date = date;
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

}
