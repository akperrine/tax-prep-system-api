package com.skillstorm.taxprepsystemapi.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("Wrong password");
    }
}
