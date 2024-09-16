package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wave.cofrinho.model.Conta;
import com.wave.cofrinho.repository.ContaRepository;
import com.wave.cofrinho.service.ContaService;

@Service
public class ContaServiceImpl implements ContaService {
    @Autowired
    private ContaRepository contaRepository;

    @Override
    public Conta criarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    @Override
    public Optional<Conta> buscarContaPorId(Long id) {
        return contaRepository.findById(id);
    }

    @Override
    public List<Conta> listarContas() {
        return contaRepository.findAll();
    }

    @Override
    public void deletarConta(Long id) {
        contaRepository.deleteById(id);
    }

    @Override
    public List<Conta> findContasByUsuarioId(Long usuarioId) {
        return contaRepository.findByUsuarioId(usuarioId);
    }

}
