package com.example.pravallika.multiplealarms.beans;

import java.io.Serializable;

/**
 * Created by RitenVithlani on 4/26/17.
 */

public class MultipleAlarm implements Serializable {
    private Long id;
    private String fromTime;
    private String toTime;
    private String fromDate;
    private String toDate;
    private String repeat;
    private String selectedDays;
    private String label;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String selectedDays) {
        this.selectedDays = selectedDays;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
