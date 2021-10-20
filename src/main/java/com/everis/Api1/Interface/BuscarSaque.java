package com.everis.Api1.Interface;

import com.everis.Api1.Dto.GetQuantidadeSaque;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "saque", url = "http://localhost:8081")
public interface BuscarSaque {

    @GetMapping("/saque/{numeroDaConta}")
    GetQuantidadeSaque getQauntidadeDeSaque(@PathVariable Long numeroDaConta);

}
