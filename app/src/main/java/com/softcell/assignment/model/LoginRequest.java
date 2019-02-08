package com.softcell.assignment.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("FieldCanBeLocal")
public class LoginRequest {

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    public LoginRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }
}
