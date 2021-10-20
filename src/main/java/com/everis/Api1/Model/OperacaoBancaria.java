package com.everis.Api1.Model;

import com.everis.Api1.Enum.EOperacao;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private EOperacao tipoOperacao;
}
