package edu.mobile.complaint.model;

import androidx.annotation.NonNull;

public class ElectricityComplaintType {

    private int id;
    private String issue;

    public int getId() {
        return id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @NonNull
    @Override
    public String toString() {
        return issue;
    }

}

