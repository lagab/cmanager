package com.lagab.cmanager.ws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * @author gabriel
 * @since 26/07/2018.
 */


@ResponseStatus(HttpStatus.NOT_FOUND)

public class NotFoundException extends RuntimeException {

    /**
     * Constructeur.
     *
     * @param message le message à transmettre
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur.
     *
     * @param message le message à transmettre
     * @param cause la cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Class<?> clazz, Object o) {
        super(clazz.getSimpleName() + " " + o + " does not exist or has not been found.");
    }

}