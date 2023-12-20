package com.example.dentamobile;

public class MedicalRecord {
    private String imageURL;

    // Default constructor
    public MedicalRecord() {
        // Empty constructor for Firebase to deserialize the object
    }

    // Constructor with imageURL parameter
    public MedicalRecord(String imageURL) {
        this.imageURL = imageURL;
    }

    // Getter and setter for imageURL
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}