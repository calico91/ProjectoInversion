package com.cblandon.inversiones.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndIdMovil(String username, String idMovil);
}
