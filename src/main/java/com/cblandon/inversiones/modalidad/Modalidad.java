package com.cblandon.inversiones.modalidad;

import com.cblandon.inversiones.credito.Credito;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Modalidad {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_modalidad")
    private Integer id;
    private String description;
    @OneToMany(mappedBy = "modalidad", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Credito> listaCreditos;
}