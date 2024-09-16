package com.wave.cofrinho.service;

import java.util.List;
import java.util.Optional;

import com.wave.cofrinho.model.Conta;

public interface ContaService {
    Conta criarConta(Conta conta);
    Optional<Conta> buscarContaPorId(Long id);
    List<Conta> listarContas();
    void deletarConta(Long id);
    List<Conta> findContasByUsuarioId(Long usuarioId);
}
