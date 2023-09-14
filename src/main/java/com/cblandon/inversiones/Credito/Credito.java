package com.cblandon.inversiones.Credito;

import java.time.LocalDate;
import java.util.Date;

import com.cblandon.inversiones.Cliente.Cliente;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credito")
public class Credito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcredito")
    private Integer id;
    @Column(nullable = false, name = "cantidadprestada")
    private Double cantidadPrestada;

    @Column(nullable = false, name = "interesporcentaje")
    private Double interesPorcentaje;

    @Column(nullable = false, name = "valorcuota")
    private Double valorCuota;

    @Column(nullable = false, name = "cantidadcuotas")
    private Integer cantidadCuotas;

    @Column(nullable = false)
    private Double saldo;

    @Column(nullable = false, name = "cuotacapital")
    private Double cuotaCapital;

    @Column(nullable = false, name = "idtitularcredito")
    private long idTitularCredito;

    @Column(nullable = false, name = "interescredito")
    private Double interesCredito;

    @Column(nullable = false, name = "usuariocreador")
    private String usuarioCreador;

    @Column(nullable = false, name = "cuotascanceladas")
    private Integer cuotasCanceladas;
    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "diapago")
    private LocalDate diaPago;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "idcliente")
    private Cliente cliente;

}
