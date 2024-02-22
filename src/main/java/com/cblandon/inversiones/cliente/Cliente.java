package com.cblandon.inversiones.cliente;


import com.cblandon.inversiones.credito.Credito;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("id_cliente"))
    private Integer id;
    @Column(nullable = false, length = 50)
    private String nombres;
    @Column(nullable = false, length = 50)
    private String apellidos;
    @Column(length = 80)
    private String email;
    @Column(nullable = false, length = 20)
    private String celular;
    @Column(length = 30)
    private String pais;
    private String observaciones;
    @Column(nullable = false, length = 100)
    private String direccion;
    @Column(unique = true, nullable = false, length = 20)
    private String cedula;
    @Column(name = "usuario_creador", length = 25)
    private String usuariocreador;
    @Column(name = "usuario_modificador", length = 25)
    private String usuariomodificador;
    @Column(name = "fecha_creacion", length = 20)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechacreacion;

    @Column(name = "fecha_modificacion", length = 20)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechamodificacion;

    @OneToMany(targetEntity = Credito.class, fetch = FetchType.LAZY, mappedBy = "cliente")
    private List<Credito> listaCreditos;

    @PrePersist
    public void prePersit() {
        if (pais == null) {
            pais = "Colombia";
        }
        if (email == null) {
            email = "NN";
        }
        this.fechacreacion = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        fechamodificacion = new Date();
    }

}
