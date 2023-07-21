package com.skillstorm.taxprepsystemapi.exceptions;

public class LocationNotFoundException extends Exception {
    public LocationNotFoundException() {
        super("Location does not found");
    }
}
