package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.wave.cofrinho.model.Transacao;
import com.wave.cofrinho.model.Transferencia;
import com.wave.cofrinho.repository.TransferenciaRepository;
import com.wave.cofrinho.service.TransferenciaService;

public class TransferenciaServiceImpl implements TransferenciaService{

     private final TransferenciaRepository transferenciaRepository;

    @Autowired
    public TransferenciaServiceImpl(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    @Override
    public Transacao save(Transacao transacao) {
        if (transacao instanceof Transferencia) {
            return transferenciaRepository.save((Transferencia) transacao);
        }
        throw new IllegalArgumentException("Transacao must be of type Despesa");
    }

    @Override
    public Optional<Transacao> findById(Long id) {
        return transferenciaRepository.findById(id).map(Transacao.class::cast);
    }

    @Override
    public List<Transacao> findAll() {
        return transferenciaRepository.findAll().stream()
                .map(Transacao.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        transferenciaRepository.deleteById(id);
    }

}
