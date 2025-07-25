package com.demo.totp.repository;

import com.demo.totp.model.UserTotp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para acceder y gestionar los datos TOTP de los usuarios.
 * Proporciona operaciones CRUD básicas y métodos personalizados.
 */
public interface UserTotpRepository extends JpaRepository<UserTotp, String> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar
     * @return Optional conteniendo el UserTotp si existe
     */
    Optional<UserTotp> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el nombre de usuario dado.
     *
     * @param username Nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
}