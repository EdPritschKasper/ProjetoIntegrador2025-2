package com.Restaurante.Dove.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_funcionario")
public class FuncionarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "cpf", unique = true, length = 50)
    private String cpf;

    @JsonIgnoreProperties({"marmita", "status", "hora_inicio", "hora_fim", "cardapio", "funcionario", "cliente", "ingredientes"})
    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<PedidoEntity> pedidos;

}
