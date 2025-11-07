package com.application.model.response;
import com.application.model.Professor;
import com.application.model.User;

public class LoginProfessorRes {
    private Professor user;
    private String token;

    public LoginProfessorRes(Professor user, String token) {
        this.user = user;
        this.token = token;
    }

    public Professor getUser() {
        return user;
    }

    public void setEmail(Professor user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
