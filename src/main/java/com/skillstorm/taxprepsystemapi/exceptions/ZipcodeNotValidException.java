package com.skillstorm.taxprepsystemapi.exceptions;

public class ZipcodeNotValidException extends Exception {

    public ZipcodeNotValidException() {
        super("Zipcode must be 5 characters");
    }
}
