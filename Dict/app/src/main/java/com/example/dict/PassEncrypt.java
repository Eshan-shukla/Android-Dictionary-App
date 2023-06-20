package com.example.dict;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PassEncrypt {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "mysecretkey12345";
    private static final String INITIALIZATION_VECTOR = "eshanshukla12345";

    public static String encryptPassword(String password) throws Exception {
        byte[] secretKeyBytes = SECRET_KEY.getBytes("UTF-8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");

        byte[] initializationVectorBytes = INITIALIZATION_VECTOR.getBytes("UTF-8");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVectorBytes);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }
}
