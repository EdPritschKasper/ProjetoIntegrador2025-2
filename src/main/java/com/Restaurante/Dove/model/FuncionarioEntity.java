package com.Restaurante.Dove.model;


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

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<PedidoEntity> pedidos;
//    private List<PedidoEntity> pedidos = new ArrayList<>();

}
