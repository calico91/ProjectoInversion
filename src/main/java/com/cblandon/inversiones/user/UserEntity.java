package com.cblandon.inversiones.user;

import java.util.Set;

import com.cblandon.inversiones.roles.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, unique = true, length = 25)
    String username;
    @Column(nullable = false, length = 30)
    String lastname;
    @Column(nullable = false, length = 30)
    String firstname;
    @Column(length = 25)
    String country;
    String password;
    @Column(nullable = false, unique = true)
    @Email(message = "correo invalido")
    String email;
    @Enumerated(EnumType.STRING)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Roles> roles;
    @Column(length = 25)
    String idMovil;

}
