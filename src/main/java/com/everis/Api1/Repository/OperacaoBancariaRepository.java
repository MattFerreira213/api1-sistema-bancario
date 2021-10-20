package com.everis.Api1.Repository;

import com.everis.Api1.Model.OperacaoBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperacaoBancariaRepository extends JpaRepository<OperacaoBancaria, Long> {

    List<OperacaoBancaria> findExtrartoByNumeroDaConta(Long numeroDaConta);
}
