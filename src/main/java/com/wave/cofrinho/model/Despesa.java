package com.wave.cofrinho.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "despesas")
@EqualsAndHashCode(callSuper = true)
public class Despesa extends Transacao {
    private String categoria;
}
