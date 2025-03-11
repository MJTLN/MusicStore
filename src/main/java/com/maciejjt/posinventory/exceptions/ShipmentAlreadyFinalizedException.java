package com.maciejjt.posinventory.exceptions;

public class ShipmentAlreadyFinalizedException extends RuntimeException{
    public ShipmentAlreadyFinalizedException(String message) {
        super(message);
    }
}
