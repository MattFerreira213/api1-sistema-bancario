package com.everis.Api1.Model;

import com.everis.Api1.Enum.ETipoDeConta;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "conta_bancaria")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private Long agencia;

    private Long numeroDaConta;

    private Long digitoVerificador;

    private ETipoDeConta tipoDaConta;

    private BigDecimal saldo;

    private int quantidadeDeSaqueSemTaxa;
}
