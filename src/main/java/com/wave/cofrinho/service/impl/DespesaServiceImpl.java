package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wave.cofrinho.model.Despesa;
import com.wave.cofrinho.model.Transacao;
import com.wave.cofrinho.repository.DespesaRepository;
import com.wave.cofrinho.service.DespesaService;

@Service
public class DespesaServiceImpl implements DespesaService {

    private final DespesaRepository despesaRepository;

    @Autowired
    public DespesaServiceImpl(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    @Override
    public Transacao save(Transacao transacao) {
        if (transacao instanceof Despesa) {
            return despesaRepository.save((Despesa) transacao);
        }
        throw new IllegalArgumentException("Transacao must be of type Despesa");
    }

    @Override
    public Optional<Transacao> findById(Long id) {
        return despesaRepository.findById(id).map(Transacao.class::cast);
    }

    @Override
    public List<Transacao> findAll() {
        return despesaRepository.findAll().stream()
                .map(Transacao.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        despesaRepository.deleteById(id);
    }
}
