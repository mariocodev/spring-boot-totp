package com.demo.totp.service;

import com.demo.totp.dto.TotpSetupResponse;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.demo.totp.model.UserTotp;
import com.demo.totp.repository.UserTotpRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con TOTP.
 * Incluye generación de secretos, códigos QR y validación de tokens.
 */
@Service
@RequiredArgsConstructor
public class TotpService {

    private final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
    private final Base32 base32 = new Base32();
    private final UserTotpRepository userTotpRepository;

    /**
     * Genera un nuevo secreto TOTP para un usuario y lo almacena en la base de datos.
     *
     * @param username Nombre de usuario para el que se genera el secreto
     * @return Respuesta con el secreto y URI para el QR
     * @throws NoSuchAlgorithmException Si no se puede generar la clave
     */
    public TotpSetupResponse generateNewTotp(String username) throws NoSuchAlgorithmException {
        // Generar clave secreta
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
        keyGenerator.init(160); // Tamaño recomendado para TOTP

        final Key key = keyGenerator.generateKey();
        final String secretKey = base32.encodeToString(key.getEncoded());

        // Guardar o actualizar en base de datos
        UserTotp userTotp = userTotpRepository.findByUsername(username)
                .orElse(new UserTotp());

        userTotp.setUsername(username);
        userTotp.setSecret(secretKey);
        userTotpRepository.save(userTotp);

        // Generar URI para QR
        String otpAuthUri = String.format("otpauth://totp/%s?secret=%s&issuer=SpringBootTOTP",
                username, secretKey);

        return new TotpSetupResponse(secretKey, otpAuthUri);
    }

    /**
     * Valida un código TOTP para un usuario específico.
     *
     * @param username Nombre de usuario a validar
     * @param code Código de 6 dígitos a validar
     * @return true si el código es válido, false en caso contrario
     * @throws InvalidKeyException Si la clave es inválida
     * @throws NoSuchAlgorithmException Si el algoritmo no está disponible
     */
    public boolean validateCode(String username, String code) throws InvalidKeyException, NoSuchAlgorithmException {
        Optional<UserTotp> userTotpOpt = userTotpRepository.findByUsername(username);
        if (userTotpOpt.isEmpty()) {
            return false;
        }

        String secretKey = userTotpOpt.get().getSecret();
        final Key key = new javax.crypto.spec.SecretKeySpec(
                base32.decode(secretKey),
                totp.getAlgorithm());

        final int expectedCode = totp.generateOneTimePassword(key, Instant.now());
        return String.valueOf(expectedCode).equals(code);
    }

    /**
     * Obtiene el secreto TOTP de un usuario.
     *
     * @param username Nombre de usuario
     * @return Secreto en formato Base32
     * @throws IllegalArgumentException Si el usuario no existe
     */
    public String getSecretForUser(String username) {
        return userTotpRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getSecret();
    }

    /**
     * Verifica si un usuario ya tiene configurado TOTP.
     *
     * @param username Nombre de usuario a verificar
     * @return true si el usuario tiene TOTP configurado
     */
    public boolean isTotpEnabled(String username) {
        return userTotpRepository.existsByUsername(username);
    }
}