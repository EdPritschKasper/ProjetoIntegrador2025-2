package com.Restaurante.Dove.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_ingrediente")
public class IngredienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;

    @JsonIgnoreProperties({"marmita", "status", "hora_inicio", "hora_fim", "cardapio", "funcionario", "cliente", "ingredientes"})
    @ManyToMany(mappedBy = "ingredientes")
    private List<PedidoEntity> pedidos;

    @JsonIgnoreProperties({"data", "pedidos", "ingredientes"})
    @ManyToMany(mappedBy = "ingredientes")
    private List<CardapioEntity> cardapios;
}
