package com.Restaurante.Dove.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
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

    @JsonIgnoreProperties({"pedidos", "ingredientes"})
    @ManyToOne
    @JoinColumn(name = "cardapio_id", nullable = false)
    private CardapioEntity cardapio;

    @JsonIgnoreProperties({"cpf", "pedidos"})
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private FuncionarioEntity funcionario;

    @JsonIgnoreProperties({ "email", "senha", "pedidos"})
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @JsonIgnoreProperties({"descricao", "pedidos", "cardapios"})
    @ManyToMany
    @JoinTable(
            name = "tb_pedido_ingrediente",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<IngredienteEntity> ingredientes;
}
