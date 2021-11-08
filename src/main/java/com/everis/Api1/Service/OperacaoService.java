package com.everis.Api1.Service;

import com.everis.Api1.Dto.GetQuantidadeSaque;
import com.everis.Api1.Enum.EOperacao;
import com.everis.Api1.Enum.ETipoDeConta;
import com.everis.Api1.Enum.ETipoEvento;
import com.everis.Api1.Evento.Evento;
import com.everis.Api1.Interface.BuscarSaque;
import com.everis.Api1.Model.Conta;
import com.everis.Api1.Model.OperacaoBancaria;
import com.everis.Api1.Repository.ContaRepository;
import com.everis.Api1.Repository.OperacaoBancariaRepository;
import com.everis.Api1.Utils.Exceptions.ContaNaoEncontradaException;
import com.everis.Api1.Utils.Exceptions.CpfNaoEncontradoException;
import com.everis.Api1.Utils.Exceptions.SaldoInsuficienteException;
import com.everis.Api1.Utils.Exceptions.TransacaoNaoAutorizadaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class OperacaoService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private OperacaoBancariaRepository operacaoBancariaRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BuscarSaque buscarSaque;

    private Optional<Conta> verificarConta;

    public List<Map.Entry<String,String>> sacar(OperacaoBancaria dadosOperacaoBancaria){

        dadosOperacaoBancaria.setNumeroDaConta(dadosOperacaoBancaria.getNumeroDaConta());
        dadosOperacaoBancaria.setNumeroDaContaDestino(dadosOperacaoBancaria.getNumeroDaConta());
        dadosOperacaoBancaria.setValorDeTransacao(dadosOperacaoBancaria.getValorDeTransacao());
        dadosOperacaoBancaria.setTipoOperacao(EOperacao.SAQUE);
        dadosOperacaoBancaria.setTaxa(0.0);

        var conta = contaRepository.findContaByNumeroDaConta(dadosOperacaoBancaria.getNumeroDaConta());

        verificarConta = Optional.ofNullable(conta);

        if (verificarConta.isPresent()) {

            var valorDeSaque = dadosOperacaoBancaria.getValorDeTransacao().doubleValue();
            var valorSaldo = conta.getSaldo().doubleValue();

            String alerta = "";

            if (valorDeSaque + 10 <= valorSaldo && valorDeSaque > 0) {

                Evento evento = modelMapper.map(conta, Evento.class);
                evento.setTipoEvento(ETipoEvento.SAQUE_EFETUADO);
                try {
                kafkaTemplate.send("BANK_NEW_SAQUE", conta.getNumeroDaConta().toString(), objectMapper.writeValueAsString(
                        new Evento(evento.getNumeroDaConta(), evento.getTipoDeConta(), evento.getTipoEvento(), evento.getQuantidadeSaque(), LocalDateTime.now().toString())));
                }catch (JsonProcessingException ex){
                    ex.printStackTrace();
                }

                var tipoConta = conta.getTipoDaConta();
                var numConta = conta.getNumeroDaConta();

                var buscarQuantidadeSaque = retornarQuantiSaques(numConta);
                var quantidadeSaque = buscarQuantidadeSaque.getQuantidadeSaque();

                if (tipoConta == ETipoDeConta.PESSOA_FISICA || tipoConta == ETipoDeConta.PESSOA_JURIDICA){

                    if(quantidadeSaque > 0){
                        var novoValorSaldo = valorSaldo - valorDeSaque ;
                        conta.setSaldo(BigDecimal.valueOf(novoValorSaldo));

                        alerta = String.format("Você ainda possui %d saques gratuitos", quantidadeSaque);

                    } else {
                        var taxa = 10.0;
                        var novoValorSaldo = valorSaldo - (valorDeSaque + taxa);
                        conta.setSaldo(BigDecimal.valueOf(novoValorSaldo));
                        dadosOperacaoBancaria.setTaxa(taxa);

                        alerta = "Atingido limite de saques gratuitos, será cobrado uma taxa de" + String.format("R$%.2f", taxa);                    }
                } else if (tipoConta == ETipoDeConta.GOVERNAMENTAL){
                    if(quantidadeSaque > 0){
                        var novoValorSaldo = valorSaldo - valorDeSaque ;
                        conta.setSaldo(BigDecimal.valueOf(novoValorSaldo));

                        alerta = String.format("Você ainda possui %d saques gratuitos", quantidadeSaque);
                    } else {
                        var taxa = 20.0;
                        var novoValorSaldo = valorSaldo - (valorDeSaque + taxa);
                        conta.setSaldo(BigDecimal.valueOf(novoValorSaldo));
                        dadosOperacaoBancaria.setTaxa(taxa);

                        alerta = "Atingido limite de saques gratuitos, será cobrado uma taxa de" + String.format("R$%.2f", taxa);
                    }
                }

                contaRepository.save(conta);
                operacaoBancariaRepository.save(dadosOperacaoBancaria);

                var mensagem = Map.entry("mensagem", "Saque efetuado com sucesso");
                var aviso = Map.entry("aviso", alerta);

                List<Map.Entry<String,String>> msg = new ArrayList<>();
                msg.add(mensagem);
                msg.add(aviso);

                return msg;
            } else {
                throw new SaldoInsuficienteException("Transação não autorizado, saldo insuficiente!");
            }
        } else {
            throw new ContaNaoEncontradaException("Conta não encontrada, verifique o numero da conta");
        }
    }

    public void depositar(OperacaoBancaria dadosOperacaoBancaria) {

        dadosOperacaoBancaria.setNumeroDaConta(dadosOperacaoBancaria.getNumeroDaConta());
        dadosOperacaoBancaria.setNumeroDaContaDestino(dadosOperacaoBancaria.getNumeroDaConta());
        dadosOperacaoBancaria.setValorDeTransacao(dadosOperacaoBancaria.getValorDeTransacao());
        dadosOperacaoBancaria.setTipoOperacao(EOperacao.DEPOSITO);
        dadosOperacaoBancaria.setTaxa(0.0);

        var conta = contaRepository.findContaByNumeroDaConta(dadosOperacaoBancaria.getNumeroDaConta());

        verificarConta = Optional.ofNullable(conta);
        if (verificarConta.isPresent()) {
            var valorSaldo = conta.getSaldo().doubleValue();
            var valorDeDeposito = dadosOperacaoBancaria.getValorDeTransacao().doubleValue();

            if (valorDeDeposito > 0) {
                var novoValorSaldo = valorDeDeposito + valorSaldo;
                conta.setSaldo(BigDecimal.valueOf(novoValorSaldo));
                contaRepository.save(conta);
                operacaoBancariaRepository.save(dadosOperacaoBancaria);
            } else {
                throw new TransacaoNaoAutorizadaException("Transação não autorizada, valor de peposito inválido!");
            }
        } else {
            throw new ContaNaoEncontradaException("Conta não encontrada, verifique o numero da conta");
        }

    }

    public Map.Entry<String, BigDecimal> consultarSaldo(Long numeroDaConta) {
        var conta = contaRepository.findContaByNumeroDaConta(numeroDaConta);
        verificarConta = Optional.ofNullable(conta);
        if (verificarConta.isPresent()){
            Map.Entry<String, BigDecimal> saldo = Map.entry("Saldo", conta.getSaldo());;
            return saldo;
        } else {
            throw new ContaNaoEncontradaException("Conta não encontrada");
        }
    }

    public void tranferir(OperacaoBancaria dadosOperacaoBancaria) {

        dadosOperacaoBancaria.setNumeroDaConta(dadosOperacaoBancaria.getNumeroDaConta());
        dadosOperacaoBancaria.setNumeroDaContaDestino(dadosOperacaoBancaria.getNumeroDaContaDestino());
        dadosOperacaoBancaria.setValorDeTransacao(dadosOperacaoBancaria.getValorDeTransacao());
        dadosOperacaoBancaria.setTipoOperacao(EOperacao.TRANFERENCIA);
        dadosOperacaoBancaria.setTaxa(0.0);

        var contaOrigem = contaRepository.findContaByNumeroDaConta(dadosOperacaoBancaria.getNumeroDaConta());
        var contaDestino = contaRepository.findContaByNumeroDaConta(dadosOperacaoBancaria.getNumeroDaContaDestino());

        Optional<Conta> verificarContaOrigem = Optional.ofNullable(contaOrigem);
        Optional<Conta> verificarContaDestino = Optional.ofNullable(contaDestino);

        if (verificarContaOrigem.isPresent() && verificarContaDestino.isPresent()) {

            if (contaOrigem != contaDestino) {

                var valorSaldoConatOrigem = contaOrigem.getSaldo().doubleValue();
                var valorSaldoConatDestino = contaDestino.getSaldo().doubleValue();
                var valorDeTransferencia = dadosOperacaoBancaria.getValorDeTransacao().doubleValue();

                if (valorDeTransferencia < valorSaldoConatOrigem && valorDeTransferencia > 0) {
                    var novoSaldoContaOrigem = valorSaldoConatOrigem -= valorDeTransferencia;
                    var novoSaldoContaDestino = valorSaldoConatDestino += valorDeTransferencia;

                    contaOrigem.setSaldo(BigDecimal.valueOf(novoSaldoContaOrigem));
                    contaDestino.setSaldo(BigDecimal.valueOf(novoSaldoContaDestino));

                    contaRepository.save(contaOrigem);
                    contaRepository.save(contaDestino);
                    operacaoBancariaRepository.save(dadosOperacaoBancaria);

                } else {
                    throw new SaldoInsuficienteException("Transação não autorizado verifique dados da operação");
                }
            } else {
                throw new TransacaoNaoAutorizadaException("Transação não autorizado verifique o número das contas");
            }
        } else {
            throw new ContaNaoEncontradaException("Conta não encontrada verifique o número das contas");

        }
    }

    public List<OperacaoBancaria> extrato(Long numeroDaConta){
        var operacoes = operacaoBancariaRepository.findExtrartoByNumeroDaConta(numeroDaConta);

        if(operacoes.size() > 0){
            for (OperacaoBancaria ob : operacoes) {
                ob.getNumeroDaConta();
            }
        } else {
            throw new CpfNaoEncontradoException(
                    String.format("Conta não encontrada, verifique o numero da conta"));
        }
        return operacoes;
    }

    @GetMapping("/saque/{numeroDaConta}")
    private GetQuantidadeSaque retornarQuantiSaques(@PathVariable Long numeroDaConta) {
        return buscarSaque.getQauntidadeDeSaque(numeroDaConta);

    }
}

