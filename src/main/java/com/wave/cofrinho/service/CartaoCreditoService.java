package com.wave.cofrinho.service;

import java.util.List;
import java.util.Optional;

import com.wave.cofrinho.model.CartaoCredito;

public interface CartaoCreditoService {
    CartaoCredito criarContaCartaoCredito(CartaoCredito cartaoCredito);
    Optional<CartaoCredito> buscarCartaoCreditoPorId(Long id);
    List<CartaoCredito> listarContasCartaoCredito();
    void deletarCartaoCredito(Long id);
}
