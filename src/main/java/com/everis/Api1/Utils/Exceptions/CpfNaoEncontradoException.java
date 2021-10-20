package com.everis.Api1.Utils.Exceptions;

public class CpfNaoEncontradoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CpfNaoEncontradoException(String msg){
        super(msg);
    }
}
