package com.maciejjt.posinventory.exceptions;

public class WarehouseConflictException extends RuntimeException{
    public WarehouseConflictException(String message) {
        super(message);
    }
}
