package com.skillstorm.taxprepsystemapi.exceptions;

public class TaxPayerNotFoundException extends Exception {

    public TaxPayerNotFoundException() {
        super("User not found");
    }
}
