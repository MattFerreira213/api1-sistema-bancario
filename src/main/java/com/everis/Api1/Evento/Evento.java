package com.everis.Api1.Evento;

import com.everis.Api1.Enum.ETipoDeConta;
import com.everis.Api1.Enum.ETipoEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    private Long numeroDaConta;

    @Enumerated(EnumType.STRING)
    private ETipoDeConta tipoDeConta;

    @Enumerated(EnumType.STRING)
    private ETipoEvento tipoEvento;

    private Long quantidadeSaque;

    private String dataEvento;
}
