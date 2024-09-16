package com.wave.cofrinho.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();

        // Gerando a SECRET_KEY (32 bytes)
        byte[] secretKeyBytes = new byte[32];
        random.nextBytes(secretKeyBytes);
        String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);
        System.out.println("SECRET_KEY: " + secretKey);

        // Gerando o INIT_VECTOR (16 bytes)
        byte[] initVectorBytes = new byte[16];
        random.nextBytes(initVectorBytes);
        String initVector = Base64.getEncoder().encodeToString(initVectorBytes);
        System.out.println("INIT_VECTOR: " + initVector);
    }
}
