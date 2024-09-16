package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wave.cofrinho.model.Transacao;
import com.wave.cofrinho.repository.TransacaoRepository;
import com.wave.cofrinho.service.TransacaoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;

    @Autowired
    public TransacaoServiceImpl(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @Override
    public Transacao save(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    @Override
    public Optional<Transacao> findById(Long id) {
        return transacaoRepository.findById(id);
    }

    @Override
    public List<Transacao> findAll() {
        return transacaoRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        transacaoRepository.deleteById(id);
    }
}
