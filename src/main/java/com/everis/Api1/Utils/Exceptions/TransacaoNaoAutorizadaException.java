package com.everis.Api1.Utils.Exceptions;

public class TransacaoNaoAutorizadaException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public TransacaoNaoAutorizadaException(String msg){
        super(msg);
    }
}
