package com.example.yazilimlab.Model;

public class MyAppItemInfo {
    String type;
    String state;
    String date;

    public String getPetitionPath() {
        return petitionPath;
    }

    public void setPetitionPath(String petitionPath) {
        this.petitionPath = petitionPath;
    }

    String petitionPath;

    public MyAppItemInfo() {

    }

    public MyAppItemInfo(String type, String state, String userUid, String petitionPath) {
        this.type = type;
        this.state = state;
        this.date = userUid;
        this.petitionPath = petitionPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String userUid) {
        this.date = userUid;
    }
}
