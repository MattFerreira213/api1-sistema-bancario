package com.everis.Api1.Dto;

import com.everis.Api1.Enum.ETipoDeConta;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ContaDto {

    @NotBlank(message = "Campo CPF deve ser informado.")
    private String cpf;

    @NotNull(message = "Campo agencia deve ser informado.")
    private Long agencia;

    @NotNull(message = "Campo numeroDaConta deve ser informado.")
    @Column(name = "numero_da_conta")
    private Long numeroDaConta;

    @NotNull(message = "Campo digitoVerificador deve ser informado.")
    @Column(name = "digito_verificador")
    private Long digitoVerificador;

    @NotNull
    @Column(name = "tipo_da_conta")
    @Enumerated(EnumType.STRING)
    private ETipoDeConta tipoDaConta;

    @NotNull(message = "Campo saldo deve ser informado.")
    private BigDecimal saldo;
}
