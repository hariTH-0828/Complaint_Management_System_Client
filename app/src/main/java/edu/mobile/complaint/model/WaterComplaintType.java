package edu.mobile.complaint.model;

import androidx.annotation.NonNull;

public class WaterComplaintType {

    private int id;
    private String issue;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return issue;
    }

}
