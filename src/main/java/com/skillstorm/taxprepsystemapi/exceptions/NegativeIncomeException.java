package com.skillstorm.taxprepsystemapi.exceptions;

public class NegativeIncomeException extends Exception {

    public NegativeIncomeException() {
        super("Can't enter negative amounts for income");
    }
}
