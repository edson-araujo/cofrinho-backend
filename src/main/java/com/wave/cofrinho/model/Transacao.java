package com.wave.cofrinho.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.wave.cofrinho.Enum.TipoTransacaoEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacoes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    private LocalDateTime dataTransacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoTransacaoEnum tipo;

    // Outros campos e métodos, se necessário
}
