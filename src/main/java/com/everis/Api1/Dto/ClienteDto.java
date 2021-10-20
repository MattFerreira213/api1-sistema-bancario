package com.everis.Api1.Dto;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ClienteDto {

    @NotBlank(message = "Campo nome deve ser informado.")
    private String nome;

    @CPF(message = "Formato de CPF inválido. Insira os 11 dígitos do cpf sem ponto e sem traço.")
    private String cpf;

    @Pattern(regexp= "(\\(\\d{2}\\)\\s)(\\d{4,5}\\-\\d{4})",
            message = "Verificar formato de telefone. (DD) 9XXX-XXXX ou (DD) XXXX-XXXX")
    private String telefone;

    @NotBlank(message = "Campo endereço deve ser informado.")
    private String endereco;






}
