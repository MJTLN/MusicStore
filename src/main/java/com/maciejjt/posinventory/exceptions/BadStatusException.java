package com.maciejjt.posinventory.exceptions;

public class BadStatusException extends RuntimeException{
    public BadStatusException(String message) {
        super(message);
    }
}
