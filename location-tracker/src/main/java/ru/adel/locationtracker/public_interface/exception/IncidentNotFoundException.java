package ru.adel.locationtracker.public_interface.exception;

public class IncidentNotFoundException extends RuntimeException{
    public IncidentNotFoundException(String message) {
        super(message);
    }
}
