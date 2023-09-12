package com.cblandon.inversiones.Roles.dto;

import com.cblandon.inversiones.Roles.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesDTO {

    private Long id;
    private Role name;
}
