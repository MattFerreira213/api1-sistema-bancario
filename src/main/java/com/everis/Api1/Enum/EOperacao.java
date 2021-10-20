package com.everis.Api1.Enum;

public enum EOperacao {

    SAQUE("Saque"),
    DEPOSITO("Deposito"),
    TRANFERENCIA("Transferencia");

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    EOperacao(String descricao) {
        this.descricao = descricao;
    }
}
