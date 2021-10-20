package com.everis.Api1.Controller;

import com.everis.Api1.Dto.ClienteDto;
import com.everis.Api1.Model.Cliente;
import com.everis.Api1.Service.ClienteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastrar-cliente")
    public ResponseEntity<?> cadastrarClienet(@RequestBody @Valid ClienteDto clienteDto) {
        Cliente dadosCliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, dadosCliente);
        clienteService.cadastrarCliente(dadosCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(dadosCliente);
    }

    @GetMapping("/listar-cliente")
    public ResponseEntity<?> listarCLiente(@RequestParam(name = "cpf") String cpf) {
        var cliente = clienteService.listarCliente(cpf);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @PutMapping("/atualizar-cliente")
    public ResponseEntity<?> atualizarCliente(@RequestBody @Valid ClienteDto clienteDto, @RequestParam(name = "cpf") String cpf) {
        var cliente = clienteService.listarCliente(cpf);
        BeanUtils.copyProperties(clienteDto, cliente);
        clienteService.atualizarCliente(cliente, cpf);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @DeleteMapping("/deletar-cliente")
    public ResponseEntity<?> deletarCliente(@RequestParam(name = "cpf") String cpf) {
        clienteService.deletarCliente(cpf);
        Map.Entry<String, String> mensagem = Map.entry("mensagem", "Cliente deletado com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(mensagem);

    }

}
