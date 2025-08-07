package com.Restaurante.Dove.model;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    private Long id;
    private String cpf;
    private String senha;
    private String nome;



}
