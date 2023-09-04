package com.cblandon.inversiones.Cliente;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue
    private Integer id;
    @Basic
    private String nombres;
    private String apellidos;
    private String email;
    @Column(length = 10)
    private String celular;
    private String pais;


}
