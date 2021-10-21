package com.example.yazilimlab.Model;

public class MyAppItemInfo {
    String type;
    String state;
    String date;
    String petitionPath;
    String documentId;


    public MyAppItemInfo() {

    }

    public MyAppItemInfo(String type, String state, String userUid, String petitionPath, String documentId) {
        this.type = type;
        this.state = state;
        this.date = userUid;
        this.petitionPath = petitionPath;
        this.documentId = documentId;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPetitionPath() {
        return petitionPath;
    }

    public void setPetitionPath(String petitionPath) {
        this.petitionPath = petitionPath;
    }
}
