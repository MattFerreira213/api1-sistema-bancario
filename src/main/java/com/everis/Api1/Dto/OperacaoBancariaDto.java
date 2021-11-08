package com.everis.Api1.Dto;

import com.everis.Api1.Enum.EOperacao;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OperacaoBancariaDto {

    @NotNull(message = "Verificar o número da conta.")
    @Column(name = "numero_da_conta")
    private Long numeroDaConta;

    private Long numeroDaContaDestino;

    @NotNull(message = "É necessario um valor para realizar a operação")
    private BigDecimal valorDeTransacao;

    private EOperacao tipoOperacao;
}
