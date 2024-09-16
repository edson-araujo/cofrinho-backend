package com.wave.cofrinho.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wave.cofrinho.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long>{
    List<Conta> findByUsuarioId(Long usuarioId);
} 
