package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wave.cofrinho.model.Despesa;
import com.wave.cofrinho.model.Receita;
import com.wave.cofrinho.model.Transacao;
import com.wave.cofrinho.repository.ReceitaRepository;
import com.wave.cofrinho.service.ReceitasService;

@Service
public class ReceitasServiceImpl implements ReceitasService{
private final ReceitaRepository receitaRepository;

    @Autowired
    public ReceitasServiceImpl(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @Override
    public Transacao save(Transacao transacao) {
        if (transacao instanceof Despesa) {
            return receitaRepository.save((Receita) transacao);
        }
        throw new IllegalArgumentException("Transacao must be of type Despesa");
    }

    @Override
    public Optional<Transacao> findById(Long id) {
        return receitaRepository.findById(id).map(Transacao.class::cast);
    }

    @Override
    public List<Transacao> findAll() {
        return receitaRepository.findAll().stream()
                .map(Transacao.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        receitaRepository.deleteById(id);
    }
}
