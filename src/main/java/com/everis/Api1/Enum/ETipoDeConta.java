package com.everis.Api1.Enum;

public enum ETipoDeConta {

    PESSOA_FISICA("Pessoa Fisica", 5, 10),
    PESSOA_JURIDICA("Pessoa Juridica", 50, 10),
    GOVERNAMENTAL("Governamental", 250, 20);

    private String descricao;

    private int quantidadeSaque;

    private double taxa;

    ETipoDeConta(String descricao, int quantidadeSaque, double taxa) {
        this.descricao = descricao;
        this.quantidadeSaque = quantidadeSaque;
        this.taxa = taxa;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidadeSaque() {
        return quantidadeSaque;
    }

    public double getTaxa() {
        return taxa;
    }

    public static ETipoDeConta verificarTipoDeConta(String tipoDeConta){
        if (ETipoDeConta.PESSOA_FISICA.getDescricao().equalsIgnoreCase(tipoDeConta)){
            return ETipoDeConta.PESSOA_FISICA;
        }else if (ETipoDeConta.PESSOA_JURIDICA.getDescricao().equalsIgnoreCase(tipoDeConta)){
            return ETipoDeConta.PESSOA_JURIDICA;
        }else if (ETipoDeConta.GOVERNAMENTAL.getDescricao().equalsIgnoreCase(tipoDeConta)){
            return ETipoDeConta.GOVERNAMENTAL;
        }
        return null;
    }

}
