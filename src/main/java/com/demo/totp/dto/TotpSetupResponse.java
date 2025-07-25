package com.demo.totp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de configuración TOTP.
 * Contiene el secreto y la URI para generar el código QR.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotpSetupResponse {
    private String secret;
    private String otpAuthUri;
}
