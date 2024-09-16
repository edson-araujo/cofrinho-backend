package com.wave.cofrinho.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.wave.cofrinho.model.Conta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {
    private Long id;
    private String nome;
    private String tipo;
    
    // Construtor que recebe uma entidade Conta e converte para DTO
    public ContaDTO(Conta conta) {
        this.id = conta.getId();
        this.nome = conta.getNome(); // Ajuste conforme o campo da sua entidade
        this.tipo = conta.getTipo().name(); // Ajuste conforme o campo da sua entidade
    }
}
