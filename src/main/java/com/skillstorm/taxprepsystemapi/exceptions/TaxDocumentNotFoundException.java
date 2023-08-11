package com.skillstorm.taxprepsystemapi.exceptions;

public class TaxDocumentNotFoundException extends Exception {
    public TaxDocumentNotFoundException() {
        super("Tax document does not exist");
    }
}
