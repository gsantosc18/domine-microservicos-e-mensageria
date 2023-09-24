package com.gedalias.msavaliadorcredito.application.exception;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException() {
        super("O Cliente informado não foi encontrado");
    }
}
