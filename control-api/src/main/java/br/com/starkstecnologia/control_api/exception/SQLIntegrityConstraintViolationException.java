package br.com.starkstecnologia.control_api.exception;

public class SQLIntegrityConstraintViolationException extends RuntimeException {

    SQLIntegrityConstraintViolationException(String msg){
        super(msg);
    }
}
