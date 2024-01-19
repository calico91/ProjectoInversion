package com.cblandon.inversiones.roles.dto;

import com.cblandon.inversiones.roles.Role;
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
