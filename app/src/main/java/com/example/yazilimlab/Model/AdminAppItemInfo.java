package com.example.yazilimlab.Model;

public class AdminAppItemInfo {

    String type;
    String state;
    String date;
    String petitionPath;
    String transcriptPath;
    String lessonPath;
    String subScorePath;
    String documentId;
    String studentNumber;

    public AdminAppItemInfo() {

    }

    public AdminAppItemInfo(String type, String state, String date, String petitionPath, String transcriptPath, String lessonPath, String subScorePath, String documentId, String studentNumber) {
        this.type = type;
        this.state = state;
        this.date = date;
        this.petitionPath = petitionPath;
        this.transcriptPath = transcriptPath;
        this.lessonPath = lessonPath;
        this.subScorePath = subScorePath;
        this.documentId = documentId;
        this.studentNumber = studentNumber;
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getPetitionPath() {
        return petitionPath;
    }

    public void setPetitionPath(String petitionPath) {
        this.petitionPath = petitionPath;
    }

    public String getTranscriptPath() {
        return transcriptPath;
    }

    public void setTranscriptPath(String transcriptPath) {
        this.transcriptPath = transcriptPath;
    }

    public String getLessonPath() {
        return lessonPath;
    }

    public void setLessonPath(String lessonPath) {
        this.lessonPath = lessonPath;
    }

    public String getSubScorePath() {
        return subScorePath;
    }

    public void setSubScorePath(String subScorePath) {
        this.subScorePath = subScorePath;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
