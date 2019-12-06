package com.teamarte.arte.api.data.request;

public class RegisterRequest {

    private String username;
    private String googleId;
    private String profilePhotoUrl;

    public RegisterRequest(String username, String googleId, String profilePhotoUrl) {
        this.username = username;
        this.googleId = googleId;
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
