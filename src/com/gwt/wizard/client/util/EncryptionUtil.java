package com.gwt.wizard.client.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptionUtil
{
    public static String KEY = "XMzDdG4D03CKm2IxIWQw7g==";

    public static void main(String[] args)
    {
        try
        {

            String text = "55184205518";

            System.out.println("Sen-crypted value:" + encrypt(text, KEY));
            System.out.println("Sdecrypted value:" + decrypt(encrypt(text, KEY), KEY));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String encrypt(String text, String secretKey)
    {
        byte[] raw;
        String encryptedString;
        SecretKeySpec skeySpec;
        byte[] encryptText = text.getBytes();
        Cipher cipher;
        try
        {
            raw = Base64.decodeBase64(secretKey);
            skeySpec = new SecretKeySpec(raw, "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
        return encryptedString;
    }

    public static String decrypt(String text, String secretKey)
    {
        Cipher cipher;
        String encryptedString;
        byte[] encryptText = null;
        byte[] raw;
        SecretKeySpec skeySpec;
        try
        {
            raw = Base64.decodeBase64(secretKey);
            skeySpec = new SecretKeySpec(raw, "AES");
            encryptText = Base64.decodeBase64(text);
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            encryptedString = new String(cipher.doFinal(encryptText));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
        return encryptedString;
    }

}