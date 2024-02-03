package com.makeev.monitoring_service.exceptions;

public class DaoException extends RuntimeException {
    public DaoException(Throwable e) {
        super(e);
    }
}
