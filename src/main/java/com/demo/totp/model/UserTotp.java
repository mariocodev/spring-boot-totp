package com.demo.totp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la información TOTP de un usuario.
 * Almacena el nombre de usuario y su secreto TOTP asociado.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTotp {

    /**
     * Nombre de usuario único que identifica al usuario
     */
    @Id
    private String username;

    /**
     * Secreto TOTP en formato Base32 utilizado para generar y validar códigos
     */
    private String secret;
}
