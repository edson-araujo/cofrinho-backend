package com.wave.cofrinho.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wave.cofrinho.model.Transacao;

public interface TransacaoRepository  extends JpaRepository<Transacao, Long>{

}
