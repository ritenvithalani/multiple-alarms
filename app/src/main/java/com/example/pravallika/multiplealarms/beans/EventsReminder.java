package com.example.pravallika.multiplealarms.beans;

import java.io.Serializable;

/**
 * Created by RitenVithlani on 2/20/17.
 */

public class EventsReminder implements Serializable {
    private Integer id;
    private String label;
    private String date;
    private String time;
    private String location;
    private Boolean active;

    public EventsReminder(Integer id, String label, String date, String time, String location, Boolean active) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.time = time;
        this.location = location;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
