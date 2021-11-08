package com.everis.Api1.Enum;

public enum ETipoDeConta {

    PESSOA_FISICA("Pessoa Fisica"),
    PESSOA_JURIDICA("Pessoa Juridica"),
    GOVERNAMENTAL("Governamental");

    private String descricao;

    ETipoDeConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
