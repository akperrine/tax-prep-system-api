package com.skillstorm.taxprepsystemapi.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException() {
        super("User not found");
    }
}
