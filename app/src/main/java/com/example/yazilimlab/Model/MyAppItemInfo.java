package com.example.yazilimlab.Model;

public class MyAppItemInfo {
    String type,state,userUid;

    public MyAppItemInfo(){

    }
    public MyAppItemInfo(String type, String state, String userUid) {
        this.type = type;
        this.state = state;
        this.userUid = userUid;
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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
