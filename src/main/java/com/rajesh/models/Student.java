package com.rajesh.models;

public class Student {
    private String sFirstName;
    private String sLastName;
    private int sAge;
    private char sSex;

    public Student() {
        // Default Constructor
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

    public int getsAge() {
        return sAge;
    }

    public void setsAge(int sAge) {
        this.sAge = sAge;
    }

    public char getsSex() {
        return sSex;
    }

    public void setsSex(char sSex) {
        this.sSex = sSex;
    }
}
