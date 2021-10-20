package com.everis.Api1.Utils.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String mensagem;
    private final String campo;
}
