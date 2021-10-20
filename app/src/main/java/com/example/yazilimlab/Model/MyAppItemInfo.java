package com.example.yazilimlab.Model;

public class MyAppItemInfo {
    String type,state,date;

    public MyAppItemInfo(){

    }
    public MyAppItemInfo(String type, String state, String userUid) {
        this.type = type;
        this.state = state;
        this.date = userUid;
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
