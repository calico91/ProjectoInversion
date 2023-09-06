package com.cblandon.inversiones.Cliente;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nombres;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    @Email(message = "correo invalido")
    private String email;
    @Size(min = 10, max = 10, message = "el numero celular debe tener 10 caracteres")
    private String celular;
    private String pais;
    @Column(unique = true, nullable = false)
    private String cedula;
    private String usuariocreador;


}
