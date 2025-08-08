package com.Restaurante.Dove.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoEntity> pedidos;
//    private List<PedidoEntity> pedidos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tb_cardapio_ingrediente",
            joinColumns = @JoinColumn(name = "cardapio_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<IngredienteEntity> ingredientes;
//    private List<IngredienteEntity> ingredientes = new ArrayList<>();
}
