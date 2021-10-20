package com.everis.Api1.Controller;

import com.everis.Api1.Dto.ContaDto;
import com.everis.Api1.Model.Conta;
import com.everis.Api1.Service.ContaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/criar-conta")
    public ResponseEntity<?> criarConta(@RequestBody @Valid ContaDto contaDto){
        Conta dadosConta = new Conta();
        BeanUtils.copyProperties(contaDto, dadosConta);
        contaService.cadastrarConta(dadosConta);
        return ResponseEntity.status(HttpStatus.CREATED).body(dadosConta);
    }

    @GetMapping("/listar-conta")
    public ResponseEntity<?> listarConta(@RequestParam(name = "cpf") String cpf){
        var contas = contaService.listarConta(cpf);
        return ResponseEntity.ok(contas);
    }

    @DeleteMapping("/deletar-conta")
    public ResponseEntity<?> deletarConta(@RequestParam(name = "cpf") String cpf){
        contaService.deletarConta(cpf);
        Map.Entry<String, String> mensagem = Map.entry("mensagem", "Conta deletada com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(mensagem);
    }
}
