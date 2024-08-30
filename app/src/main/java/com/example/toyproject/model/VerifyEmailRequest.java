package com.example.toyproject.model;

public class VerifyEmailRequest {
    String email;
    String verifyCode;

    public VerifyEmailRequest(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }

    public String getEmail() {
        return email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
