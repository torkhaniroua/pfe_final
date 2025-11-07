package com.application.model.response;
import com.application.model.User;

public class LoginRes {
    private User user;
    private String token;

    public LoginRes(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setEmail(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
