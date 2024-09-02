package com.cblandon.inversiones.user.repository;

import java.util.Optional;
import java.util.Set;

import com.cblandon.inversiones.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    /**
     * buscar los usuarios admin y el usuario que se paso por parametro
     */
    @Query(value = "SELECT * FROM user u" +
            "   INNER JOIN user_roles ur ON ur.user_id = u.id" +
            "   WHERE u.username = :username OR ur.role_id = 1", nativeQuery = true)
    Set<UserEntity> buscarUsuariosAdmin(@Param("username") String username);

    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT u FROM UserEntity u JOIN u.roles r WHERE r.id != 1")
    Set<UserEntity> consultarUsuarios();

    @Query(value = "SELECT u.isActive FROM UserEntity u WHERE u.username = :username")
    boolean validarEstadoUsuario(@Param("username") String username);

}
