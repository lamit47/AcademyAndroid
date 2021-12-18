package com.example.academy_project.entities;

import java.util.Date;

public class Token {
    private String accessToken;
    private Date accessTokenExpires;
    private String refreshToken;
    private Date refreshTokenExpires;

    public Token(String accessToken, Date accessTokenExpires, String refreshToken, Date refreshTokenExpires) {
        this.accessToken = accessToken;
        this.accessTokenExpires = accessTokenExpires;
        this.refreshToken = refreshToken;
        this.refreshTokenExpires = refreshTokenExpires;
    }

    public Token() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getAccessTokenExpires() {
        return accessTokenExpires;
    }

    public void setAccessTokenExpires(Date accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(Date refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", accessTokenExpires=" + accessTokenExpires +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenExpires=" + refreshTokenExpires +
                '}';
    }
}
