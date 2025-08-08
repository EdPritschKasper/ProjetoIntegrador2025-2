package com.Restaurante.Dove.model;

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
    @ManyToMany(mappedBy = "ingredientes")
    private List<PedidoEntity> pedidos;
//    private List<PedidoEntity> pedidos = new ArrayList<>();
    @ManyToMany(mappedBy = "ingredientes")
    private List<CardapioEntity> cardapios;
//    private List<CardapiosEntity> cardapios = new ArrayList<>();
}
