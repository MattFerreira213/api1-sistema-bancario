package com.everis.Api1;

import com.everis.Api1.Model.Cliente;
import com.everis.Api1.Repository.ClienteRepository;
import com.everis.Api1.Service.ClienteService;
import com.everis.Api1.Utils.Exceptions.ClienteExistenteException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;

public class ClienteServiceTest {

    @Autowired
    ClienteService clienteService;

    @Mock
    ClienteRepository clienteMocked;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        clienteService = new ClienteService(clienteMocked);
    }

    @Test(expected = ClienteExistenteException.class)
    public void should_deny_creation_of_client_that_exists(){
        Cliente dadosDoClienteSalvos =new Cliente();
        dadosDoClienteSalvos.setId(1L);
        dadosDoClienteSalvos.setNome("Mateus");
        dadosDoClienteSalvos.setCpf("20967493013");
        dadosDoClienteSalvos.setEndereco("Av. Paulista, nº 150");
        dadosDoClienteSalvos.setTelefone("(11) 4002-8922");

        when(clienteMocked.findClienteByCpf("20967493013")).thenReturn(dadosDoClienteSalvos);

        Cliente novoCliente = new Cliente();
        novoCliente.setNome("Mateus");
        novoCliente.setCpf("20967493013");
        novoCliente.setEndereco("Av. Paulista, nº 150");
        novoCliente.setTelefone("(11) 4002-8922");

        clienteService.cadastrarCliente(novoCliente);
    }
}
