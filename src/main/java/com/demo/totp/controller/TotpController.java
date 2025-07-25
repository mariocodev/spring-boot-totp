package com.demo.totp.controller;

import com.demo.totp.dto.TotpSetupResponse;
import com.demo.totp.dto.ValidationRequest;
import com.demo.totp.dto.ValidationResponse;
import com.demo.totp.service.TotpService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

/**
 * Controlador que maneja las peticiones HTTP relacionadas con TOTP.
 * Expone endpoints para configurar, generar QR y validar códigos.
 */
@RestController
@RequestMapping("/api/totp")
@RequiredArgsConstructor
public class TotpController {

    private final TotpService totpService;

    /**
     * Configura TOTP para un usuario generando un nuevo secreto.
     *
     * @param username Nombre de usuario para configurar
     * @return Respuesta con el secreto y URI para QR
     */
    @GetMapping("/setup/{username}")
    public TotpSetupResponse setupTotp(@PathVariable String username) throws Exception {
        if (totpService.isTotpEnabled(username)) {
            throw new IllegalArgumentException("TOTP ya está configurado para este usuario");
        }
        return totpService.generateNewTotp(username);
    }

    /**
     * Genera un código QR escaneable por aplicaciones autenticadoras.
     *
     * @param username Nombre de usuario
     * @return Imagen PNG del código QR
     */
    @GetMapping(value = "/qr/{username}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQrCode(@PathVariable String username) throws Exception {
        String secret = totpService.getSecretForUser(username);
        String otpAuthUri = String.format("otpauth://totp/%s?secret=%s&issuer=SpringBootTOTP",
                username, secret);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUri, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    /**
     * Valida un código TOTP proporcionado por el usuario.
     *
     * @param request Solicitud con username y código
     * @return Respuesta indicando si el código es válido
     */
    @PostMapping("/validate")
    public ValidationResponse validateCode(@RequestBody ValidationRequest request) {
        try {
            boolean isValid = totpService.validateCode(request.getUsername(), request.getCode());
            return new ValidationResponse(isValid);
        } catch (Exception e) {
            return new ValidationResponse(false);
        }
    }
}