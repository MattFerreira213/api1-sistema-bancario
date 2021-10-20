package com.everis.Api1.Service;

import com.everis.Api1.Model.Cliente;
import com.everis.Api1.Repository.ClienteRepository;
import com.everis.Api1.Utils.Exceptions.ClienteExistenteException;
import com.everis.Api1.Utils.Exceptions.CpfNaoEncontradoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private static Optional<Cliente> verificarExistenciaDoCliente;

    public void cadastrarCliente(Cliente dadosCliente) {

        dadosCliente.setNome(dadosCliente.getNome());
        dadosCliente.setCpf(dadosCliente.getCpf());
        dadosCliente.setTelefone(dadosCliente.getTelefone());
        dadosCliente.setEndereco(dadosCliente.getEndereco());

        verificarExistenciaDoCliente = Optional.ofNullable(clienteRepository.findClienteByCpf(dadosCliente.getCpf()));

        if (!verificarExistenciaDoCliente.isPresent()) {
            clienteRepository.save(dadosCliente);
        } else {
            throw new ClienteExistenteException(
                    String.format("O cpf %s já possui cadastrado",  dadosCliente.getCpf()));
        }
    }

    public Cliente listarCliente(String cpf) {
        var cliente = clienteRepository.findClienteByCpf(cpf);

        verificarExistenciaDoCliente = Optional.ofNullable(cliente);

        if (verificarExistenciaDoCliente.isPresent()) {
            return cliente;
        } else {
            throw new CpfNaoEncontradoException(
                    String.format("Cliente de cpf %s não encontrado.", cpf));
        }
    }

    public Cliente atualizarCliente(Cliente dadosCliente, String cpf) {
        var dadosAtualcliente = clienteRepository.findClienteByCpf(cpf);

        BeanUtils.copyProperties(dadosCliente, dadosAtualcliente);

        verificarExistenciaDoCliente = Optional.of(dadosAtualcliente);

        if (verificarExistenciaDoCliente.isPresent()) {
            return clienteRepository.save(dadosAtualcliente);
        } else {
            throw new CpfNaoEncontradoException(
                    String.format("Cliente de cpf %s não encontrado.", cpf));
        }
    }

    public void deletarCliente(String cpf) {
        var cliente = clienteRepository.findClienteByCpf(cpf);

        verificarExistenciaDoCliente = Optional.ofNullable(cliente);

        if (verificarExistenciaDoCliente.isPresent()) {
            clienteRepository.delete(cliente);
        } else {
            throw new CpfNaoEncontradoException(
                    String.format("Cliente de cpf %s não encontrado.", cpf));
        }
    }
}
