package com.skillstorm.taxprepsystemapi.exceptions;

public class UserExistsException extends Exception {
    public UserExistsException() {
        super("User Exists");
    }
}
