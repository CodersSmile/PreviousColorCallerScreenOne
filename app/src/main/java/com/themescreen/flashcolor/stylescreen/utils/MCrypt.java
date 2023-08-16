package com.themescreen.flashcolor.stylescreen.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {
    static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private String iv = "";
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    Context mcontext;

    private String SecretKey = "";

    static {
        System.loadLibrary("keys");
    }



    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static String getResponseString(byte[] encryption, String secretKey, String ivCode) {

        Log.e("Check", "getResponseString: "+secretKey+" "+ivCode );
        try {
            /* Base64 base64 = new Base64();*/
            Cipher ciper = null;
            ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new
                    SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec
                    (ivCode.getBytes("UTF-8"), 0, ciper.getBlockSize());


            //Decrypt
            ciper.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] text = ciper.doFinal(Base64.decode(encryption, Base64.DEFAULT));


            return new String(text);

        } catch (Exception e) {
            Log.e("Check", "getStaticNew: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
