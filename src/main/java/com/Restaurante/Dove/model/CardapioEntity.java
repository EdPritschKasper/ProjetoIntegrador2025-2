package com.Restaurante.Dove.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_cardapio")
public class CardapioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @JsonIgnoreProperties({"marmita", "status", "hora_inicio", "hora_fim", "cardapio", "funcionario", "cliente", "ingredientes"})
//    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL)
    private List<PedidoEntity> pedidos;

    @JsonIgnoreProperties({"descricao", "pedidos", "cardapios"})
    @ManyToMany
    @JoinTable(
            name = "tb_cardapio_ingrediente",
            joinColumns = @JoinColumn(name = "cardapio_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<IngredienteEntity> ingredientes;
}
