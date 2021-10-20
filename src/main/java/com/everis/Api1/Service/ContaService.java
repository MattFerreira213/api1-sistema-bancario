package com.everis.Api1.Service;

import com.everis.Api1.Enum.ETipoDeConta;
import com.everis.Api1.Enum.ETipoEvento;
import com.everis.Api1.Evento.Evento;
import com.everis.Api1.Model.Cliente;
import com.everis.Api1.Model.Conta;
import com.everis.Api1.Repository.ClienteRepository;
import com.everis.Api1.Repository.ContaRepository;
import com.everis.Api1.Utils.Exceptions.ContaExistenteException;
import com.everis.Api1.Utils.Exceptions.CpfNaoEncontradoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    public void cadastrarConta(Conta dadosDaConta) {

        dadosDaConta.setAgencia(dadosDaConta.getAgencia());
        dadosDaConta.setCpf(dadosDaConta.getCpf());
        dadosDaConta.setNumeroDaConta(dadosDaConta.getNumeroDaConta());
        dadosDaConta.setDigitoVerificador(dadosDaConta.getDigitoVerificador());
        dadosDaConta.setTipoDaConta(dadosDaConta.getTipoDaConta());
        dadosDaConta.setSaldo(dadosDaConta.getSaldo());


        Optional<Cliente> verificarCpfDaConta = Optional.ofNullable(
                clienteRepository.findClienteByCpf(dadosDaConta.getCpf()));

        Optional<Conta> verificarExistenciaDeConta = Optional.ofNullable(
                contaRepository.findContaByNumeroDaConta(dadosDaConta.getNumeroDaConta()));

        if (verificarCpfDaConta.isPresent() && !verificarExistenciaDeConta.isPresent()){
            var tipoConta = dadosDaConta.getTipoDaConta();
            if (tipoConta == ETipoDeConta.PESSOA_FISICA){
                dadosDaConta.setQuantidadeDeSaqueSemTaxa(5);

                dadosDaConta.setTipoDaConta(ETipoDeConta.PESSOA_FISICA);
            }else if (tipoConta == ETipoDeConta.PESSOA_JURIDICA){
                dadosDaConta.setQuantidadeDeSaqueSemTaxa(50);

                dadosDaConta.setTipoDaConta(ETipoDeConta.PESSOA_JURIDICA);
            } else if (tipoConta == ETipoDeConta.GOVERNAMENTAL){
                dadosDaConta.setQuantidadeDeSaqueSemTaxa(250);

                dadosDaConta.setTipoDaConta(ETipoDeConta.GOVERNAMENTAL);
            }
            contaRepository.save(dadosDaConta);
            Evento evento = modelMapper.map(dadosDaConta, Evento.class);
            evento.setTipoEvento(ETipoEvento.CONTA_CRIADA);
            try {
            kafkaTemplate.send("BANK_NEW_SAQUE", dadosDaConta.getNumeroDaConta().toString(),
                    objectMapper.writeValueAsString(new Evento(evento.getNumeroDaConta(), evento.getTipoDeConta(),
                            evento.getTipoEvento(), evento.getQuantidadeSaque(), LocalDateTime.now().toString())));
            } catch (JsonProcessingException ex){
                ex.printStackTrace();
            }
        } else {
            throw new ContaExistenteException("Número da conta em uso ou cpf não encontrado.");
        }
    }

    public List<Conta> listarConta(String cpf) {
        var contas = contaRepository.findContaByCpf(cpf);
        if (contas.size() > 0){
            for (Conta conta : contas) {
                conta.getCpf();
            }
        } else {
            throw new CpfNaoEncontradoException(
                    String.format("Conta de cpf %s não encontrado ou não existe.", cpf));
        }
        return contas;
    }

    public void deletarConta(String cpf) {
        var contas = contaRepository.findContaByCpf(cpf);

        if (contas.size() > 0){
            for (Conta conta : contas) {
                contaRepository.delete(conta);
            }
        } else {
            throw new CpfNaoEncontradoException(
                    String.format("Conta de cpf %s não encontrado ou não existe.", cpf));
        }
    }
}
