package com.rajesh.models;

public class Student {
    private String id;
    private String sFirstName;
    private String sLastName;
    private String sAge;
    private String sSex;

    public Student() {
        // Default Constructor
    }

    public Student(String id, String sFirstName, String sLastName, String sAge, String sSex) {
        this.id = id;
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.sAge = sAge;
        this.sSex = sSex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsFirstName() {
        return sFirstName;
    }

    public void setsFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getsLastName() {
        return sLastName;
    }

    public void setsLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getsAge() {
        return sAge;
    }

    public void setsAge(String sAge) {
        this.sAge = sAge;
    }

    public String getsSex() {
        return sSex;
    }

    public void setsSex(String sSex) {
        this.sSex = sSex;
    }
}
