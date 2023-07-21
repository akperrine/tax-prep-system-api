package com.skillstorm.taxprepsystemapi.exceptions;

public class StateNotValidException extends Exception {

    public StateNotValidException() {
        super("State must be two characters");
    }
}
