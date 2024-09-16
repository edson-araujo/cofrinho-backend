package com.wave.cofrinho.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Outro extends Conta {

    @Column(nullable = false)
    private String descricao;

}