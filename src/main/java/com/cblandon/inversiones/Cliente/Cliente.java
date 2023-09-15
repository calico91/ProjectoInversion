package com.cblandon.inversiones.Cliente;


import com.cblandon.inversiones.Credito.Credito;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("idcliente"))
    private Integer id;
    @Column(nullable = false)
    private String nombres;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    @Email(message = "correo invalido")
    private String email;
    @Column(nullable = false)
    @Size(min = 10, max = 10, message = "el numero celular debe tener 10 caracteres")
    private String celular;
    @Column(nullable = false)
    private String pais;
    @Column(unique = true, nullable = false)
    private String cedula;
    private String usuariocreador;

    private String usuariomodificador;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechacreacion;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechamodificacion;

    @OneToMany(targetEntity = Credito.class, fetch = FetchType.LAZY,mappedBy = "cliente")
    private List<Credito> listaCreditos;

    @PrePersist
    public void prePersit() {
        this.fechacreacion = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechamodificacion = new Date();
    }


}
