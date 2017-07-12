package lc.nsu.edu.cn.mysafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 刘畅 on 2017/7/5.
 */

public class MD5Util {
    public static String encoder(String psd){
        try {
            psd = psd+"mysafe";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bs = digest.digest(psd.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bs){
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length()<2){
                    hexString = "0"+hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
