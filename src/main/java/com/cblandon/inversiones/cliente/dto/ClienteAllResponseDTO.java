package com.cblandon.inversiones.cliente.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClienteAllResponseDTO {

     Integer id;
     String nombres;
     String apellidos;
     String email;
     String celular;
     String pais;
     String cedula;
     String direccion;
     String observaciones;

}
