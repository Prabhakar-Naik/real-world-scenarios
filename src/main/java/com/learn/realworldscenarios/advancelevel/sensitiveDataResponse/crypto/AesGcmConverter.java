package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * @author prabhakar, @Date 11-08-2025
 */
@Converter
public class AesGcmConverter implements AttributeConverter<String, String> {

    // AES-GCM parameters  --> this is one type of "AES" = block cipher algorithm.
    // "GCM" = Galois/Counter Mode (stream-like, with built-in authentication).
    //@Value("${AES_KEY_BASE64}")
    private static final String value = "ESvOQ3BMB7BOMEtASbtOM8/m4V7ouV0EtLUAPlljbdE=";
    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12; // 96 bits recommended for GCM
    private static final int TAG_LENGTH_BITS = 128;

    private static final SecretKeySpec keySpec;
    private static final SecureRandom secureRandom = new SecureRandom();

    static {
        //String base64Key = System.getenv("AES_KEY_BASE64"); // or
        String base64Key = value;
        if (base64Key == null) {
            throw new IllegalArgumentException("AES_KEY_BASE64 env var not set. Provide a 32-byte key (base64).");
        }
        byte[] key = Base64.getDecoder().decode(base64Key);
        if (key.length != 32){ // enforce 256-bit key
            throw new IllegalArgumentException("AES_KEY_BASE64 must decode to 32 bytes (256-bit).");
        }
        keySpec = new SecretKeySpec(key, AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if (attribute == null || attribute.isEmpty()) return null;

            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS,iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] cipherText = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            ByteBuffer bb = ByteBuffer.allocate(iv.length + cipherText.length);
            bb.put(iv);
            bb.put(cipherText);
            return Base64.getEncoder().encodeToString(bb.array());

        }catch (Exception e) {
            throw new RuntimeException("Error encrypting attribute", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) return null;
            byte[] all = Base64.getDecoder().decode(dbData);
            ByteBuffer bb = ByteBuffer.wrap(all);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            bb.get(iv);
            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS,iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, StandardCharsets.UTF_8);

        }catch (Exception e) {
            throw new RuntimeException("Error decrypting dbData", e);
        }

    }

}
