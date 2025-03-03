package com.maciejjt.posinventory.exceptions;

public class StorageNotEmptiedException extends RuntimeException{
    public StorageNotEmptiedException(String message) {
        super(message);
    }
}
