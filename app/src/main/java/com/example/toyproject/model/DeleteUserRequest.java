package com.example.toyproject.model;

public class DeleteUserRequest {
    private String currentPassword;

    public DeleteUserRequest(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
