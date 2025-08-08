package com.Restaurante.Dove.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_pedido")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "marmita", length = 20)
    private String marmita;
    @Column(name = "status", length = 10)
    private String status;
    @Column(name = "hora_inicio")
    private LocalTime hora_inicio;
    @Column(name = "hora_fim")
    private LocalTime hora_fim;
    @ManyToOne
    @JoinColumn(name = "cardapio_id", nullable = false)
    private CardapioEntity cardapio;
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private FuncionarioEntity funcionario;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;
    @ManyToMany
    @JoinTable(
            name = "tb_pedido_ingrediente",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<IngredienteEntity> ingredientes;
//    private List<IngredienteEntity> ingredientes = new ArrayList<>();
}
