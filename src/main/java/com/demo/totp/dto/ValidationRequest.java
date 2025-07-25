package com.demo.totp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de validación de código TOTP.
 * Contiene el nombre de usuario y el código a validar.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequest {
    private String username;
    private String code;
}
