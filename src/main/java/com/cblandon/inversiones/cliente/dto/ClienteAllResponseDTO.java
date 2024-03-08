package com.cblandon.inversiones.cliente.dto;




public record ClienteAllResponseDTO(Integer id, String nombres, String apellidos, String email, String celular,
                                    String pais, String cedula, String direccion, String observaciones) {
}
