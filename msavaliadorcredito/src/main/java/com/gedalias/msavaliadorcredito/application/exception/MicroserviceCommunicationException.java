package com.gedalias.msavaliadorcredito.application.exception;

public class MicroserviceCommunicationException extends Exception {
    private final int status;

    public MicroserviceCommunicationException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
