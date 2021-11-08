package com.everis.Api1.Model;

import com.everis.Api1.Enum.EOperacao;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class OperacaoBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long numeroDaConta;

    private Long numeroDaContaDestino;

    private BigDecimal valorDeTransacao;

    private double taxa;

    @Enumerated(EnumType.STRING)
    private EOperacao tipoOperacao;
}
