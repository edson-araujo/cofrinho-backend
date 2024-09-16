package com.wave.cofrinho.model;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class CartaoCredito extends Conta {

    @Column(nullable = false)
    private BigDecimal limite;

    @Column(nullable = false)
    private String tipoLimite;

    private BigDecimal faturaAtual;

}