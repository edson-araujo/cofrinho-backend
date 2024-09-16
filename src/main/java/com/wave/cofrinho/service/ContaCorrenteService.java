package com.wave.cofrinho.service;

import java.util.List;
import java.util.Optional;

import com.wave.cofrinho.model.ContaCorrente;

public interface ContaCorrenteService {
    ContaCorrente criarContaCorrente(ContaCorrente contaCorrente);
    Optional<ContaCorrente> buscarContaCorrentePorId(Long id);
    List<ContaCorrente> listarContasCorrente();
    void deletarContaCorrente(Long id);
}
