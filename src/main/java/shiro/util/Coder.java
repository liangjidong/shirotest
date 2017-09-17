package shiro.util;


import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by author on 2017/9/15.
 * 该类提供了基本的Base64加密解密，Md5、Sha加密算法
 */
public class Coder {
    public static void main(String[] args) {
    }

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * base64解密---使用commons-codec.jar
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static byte[] decryptBASE64(String key) throws IOException {
        byte[] decodeByte = Base64.decodeBase64(key.getBytes());
        return decodeByte;
    }

    /**
     * base64加密---使用commons-codec.jar，不能使用在url中，如果在url中使用，则需要使用encode safe
     *
     * @param key
     * @return
     */
    public static String encryptBASE64(byte[] key) {
        return Base64.encodeBase64String(key);
    }

    public static String encryptBASE64UrlSafe(byte[] key){
        return Base64.encodeBase64URLSafeString(key);
    }

    /**
     * md5加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encryptMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * sha加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encryptSHA(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }
}
