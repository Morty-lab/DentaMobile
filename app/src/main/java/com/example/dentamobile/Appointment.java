package com.example.dentamobile;

public class Appointment {

    private String time;
    private String date;
    private String patientName;
    private String key;
    private String service;

    // Constructors, getters, and setters

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String time, String date, String patientName, String key, String selectedItemText) {
        this.time = time;
        this.date = date;
        this.patientName = patientName;
        this.key = key;
        this.service = selectedItemText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getService() {
        return service;
    }

    public void setService(String selectedItemText) {
        this.service = selectedItemText;
    }

    // Add constructors and other methods as needed
}
