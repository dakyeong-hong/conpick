package com.team1.conpick.auth;

public class InvalidRefreshTokenException extends RuntimeException{
    public InvalidRefreshTokenException(String message){super(message);}
}
