package com.wave.cofrinho.configuration;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import org.apache.tomcat.util.codec.binary.Base64;

public class EncryptionUtil {
    // Gera uma chave secreta válida de 32 bytes para AES-256 (em Base64)
    private static final String SECRET_KEY = "fxBwwdjcUJq5aZL1mpWdKL0TE+TsjePsDS+pfK0SSvQ="; // Decodificado: 32 bytes
    private static final String INIT_VECTOR = "BPl5ERgVB04PYdFohWGPlw=="; // Base64 de 16 bytes

    public static String encrypt(String value) {
        try {
            // Decodifica o IV e a chave secreta
            byte[] ivBytes = Base64.decodeBase64(INIT_VECTOR);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            byte[] keyBytes = Base64.decodeBase64(SECRET_KEY);
            // Garante que a chave tem exatamente 32 bytes para AES-256
            if (keyBytes.length != 32) {
                throw new IllegalArgumentException("A chave secreta deve ter 32 bytes (256 bits) para AES-256.");
            }
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");

            // Configura o Cipher para criptografia
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao criptografar o token", ex);
        }
    }

    public static String decrypt(String encrypted) {
        try {
            // Decodifica o IV e a chave secreta
            byte[] ivBytes = Base64.decodeBase64(INIT_VECTOR);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            byte[] keyBytes = Base64.decodeBase64(SECRET_KEY);
            // Verifica o tamanho da chave
            if (keyBytes.length != 32) {
                throw new IllegalArgumentException("A chave secreta deve ter 32 bytes (256 bits) para AES-256.");
            }
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");

            // Configura o Cipher para descriptografia
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao descriptografar o token", ex);
        }
    }
}
