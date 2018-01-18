package com.mdni.scm.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public final class AESEncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String CHAR_ENCODE = "utf-8";

    private static final String IBM_SECURE_RANDOM = "IBMSecureRandom";
    private static final int LENGTH = 128;


    public static String encrypt(String encryptKey, String content) throws Exception {
        if (content == null) {
            return null;
        }
        return parseByte2HexStr(doCipher(encryptKey, Cipher.ENCRYPT_MODE, content.getBytes(CHAR_ENCODE)));
    }

    public static String decrypt(String decryptKey, String content) throws Exception {
        if (content == null) {
            return null;
        }
        return new String(doCipher(decryptKey, Cipher.DECRYPT_MODE, parseHexStr2Byte(content)));
    }

    private static byte[] doCipher(String encryptKey, int mode, byte[] content) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom sr = SecureRandom.getInstance(IBM_SECURE_RANDOM);
        sr.setSeed(encryptKey.getBytes());
        kgen.init(LENGTH, sr);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key);
        return cipher.doFinal(content);
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}