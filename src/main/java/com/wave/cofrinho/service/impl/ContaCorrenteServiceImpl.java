package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.wave.cofrinho.model.ContaCorrente;
import com.wave.cofrinho.repository.ContaCorrenteRepository;
import com.wave.cofrinho.service.ContaCorrenteService;

public class ContaCorrenteServiceImpl implements ContaCorrenteService {
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Override
    public ContaCorrente criarContaCorrente(ContaCorrente contaCorrente) {
        return contaCorrenteRepository.save(contaCorrente);
    }

    @Override
    public Optional<ContaCorrente> buscarContaCorrentePorId(Long id) {
        return contaCorrenteRepository.findById(id);
    }

    @Override
    public List<ContaCorrente> listarContasCorrente() {
        return contaCorrenteRepository.findAll();
    }

    @Override
    public void deletarContaCorrente(Long id) {
        contaCorrenteRepository.deleteById(id);
    }

}
