package com.example.toyproject.model;

public class ChangePasswordRequest {
    String currentPassword;
    String newPassword;
    String confirmPassword;

    public ChangePasswordRequest(String currentPassword, String newPassword, String confirmNewPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmNewPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmPassword = confirmNewPassword;
    }
}
