package com.wave.cofrinho.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.wave.cofrinho.model.CartaoCredito;
import com.wave.cofrinho.repository.CartaoCreditoRepository;
import com.wave.cofrinho.service.CartaoCreditoService;

public class CartaoCreditoServiceImpl implements CartaoCreditoService{

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    @Override
    public CartaoCredito criarContaCartaoCredito(CartaoCredito cartaoCredito) {
        return cartaoCreditoRepository.save(cartaoCredito);
    }

    @Override
    public Optional<CartaoCredito> buscarCartaoCreditoPorId(Long id) {
        return cartaoCreditoRepository.findById(id);
    }

    @Override
    public List<CartaoCredito> listarContasCartaoCredito() {
        return cartaoCreditoRepository.findAll();
    }

    @Override
    public void deletarCartaoCredito(Long id) {
        cartaoCreditoRepository.deleteById(id);
    }
}
