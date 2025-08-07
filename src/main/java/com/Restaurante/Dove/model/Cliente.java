package com.Restaurante.Dove.model;


import lombok.*;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    private Long id;
    private String nome;
    private String email;
    private String senha;

}
