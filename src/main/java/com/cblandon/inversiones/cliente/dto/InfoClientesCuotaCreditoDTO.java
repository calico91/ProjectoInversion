package com.cblandon.inversiones.cliente.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoClientesCuotaCreditoDTO {

     Integer idCliente;
     String nombres;
     String apellidos;
     String cedula;
     String fechaCredito;
     Double valorCredito;
     String fechaAbono;
     String fechaCuota;
     Double valorCuota;
     Double valorInteres;
     Integer idCredito;

}
