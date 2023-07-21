package com.skillstorm.taxprepsystemapi.exceptions;

public class LocationNotFoundException extends Exception {
    public LocationNotFoundException() {
        super("Location not found");
    }
}
