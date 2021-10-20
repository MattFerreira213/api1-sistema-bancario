package com.everis.Api1.Repository;

import com.everis.Api1.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findClienteByCpf(String cpf);

}
