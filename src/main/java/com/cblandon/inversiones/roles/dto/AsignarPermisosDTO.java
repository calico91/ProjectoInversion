package com.cblandon.inversiones.roles.dto;

import java.util.Set;

public record AsignarPermisosDTO(String rol, Set<Integer> permisos) {
}
