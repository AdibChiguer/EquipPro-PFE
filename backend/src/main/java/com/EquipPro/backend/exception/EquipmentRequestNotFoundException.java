package com.EquipPro.backend.exception;

public class EquipmentRequestNotFoundException extends RuntimeException {
    public EquipmentRequestNotFoundException(String message) {
        super(message);
    }
}
