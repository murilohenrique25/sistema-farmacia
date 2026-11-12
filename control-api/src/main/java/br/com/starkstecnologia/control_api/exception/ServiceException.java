package br.com.starkstecnologia.control_api.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(String msg){
        super(msg);
    }
}
