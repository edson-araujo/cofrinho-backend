package com.wave.cofrinho.service;

import java.util.List;
import java.util.Optional;

import com.wave.cofrinho.model.Transacao;

public interface TransacaoService {
    Transacao save(Transacao transacao);
    Optional<Transacao> findById(Long id);
    List<Transacao> findAll();
    void deleteById(Long id);
}