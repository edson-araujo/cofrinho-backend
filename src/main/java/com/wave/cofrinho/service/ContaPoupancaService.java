package com.wave.cofrinho.service;

import java.util.List;
import java.util.Optional;

import com.wave.cofrinho.model.ContaPoupanca;

public interface ContaPoupancaService {
  ContaPoupanca criarContaPoupanca(ContaPoupanca contaPoupanca);
    Optional<ContaPoupanca> buscarContaPoupancaPorId(Long id);
    List<ContaPoupanca> listarContasPoupanca();
    void deletarContaPoupanca(Long id);
}
