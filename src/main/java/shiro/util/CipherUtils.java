package shiro.util;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by author on 2017/9/15.
 * RSA 加密解密工具类,其中加密数据字节不能超过117
 */
public class CipherUtils {
    public static void main(String[] args) throws Exception {
        Map<String, Object> map = initKey();
        String pub = getPublicKey(map);
        String priv = getPrivateKey(map);

        String s = "111111";

        byte[] encryptByPublicKey = encryptByPublicKey(s.getBytes(), pub);
        System.out.println(Coder.encryptBASE64(encryptByPublicKey));

        byte[] decryptByPrivateKey = decryptByPrivateKey(encryptByPublicKey, priv);
        System.out.println(new String(decryptByPrivateKey));

        byte[] encryptByPrivateKey = encryptByPrivateKey(s.getBytes(), priv);
        byte[] decryptByPublicKey = decryptByPublicKey(encryptByPrivateKey, pub);
        System.out.println(new String(decryptByPublicKey));

    }

    public static final String KEY_ALGORTHM = "RSA";
    public static final String SIGNATURE_ALGORTHM = "MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 每次产生新的rsa密钥需要调用这个方法
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        publicKey.getPublicExponent();
//        publicKey.getModulus();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> map = new HashMap<>(2);
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }

    public static String getPublicKey(Map<String, Object> map) throws Exception {
        Object key = map.get(PUBLIC_KEY);
        return Coder.encryptBASE64(((Key) key).getEncoded());
    }

    public static String getPrivateKey(Map<String, Object> map) throws Exception {
        Object key = map.get(PRIVATE_KEY);
        return Coder.encryptBASE64(((Key) key).getEncoded());
    }


    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        //由于密钥使用base64进行加密了，所以第一步需要用base64解密
        byte[] keyBytes = Coder.decryptBASE64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //私钥加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Coder.decryptBASE64(key);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Coder.decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Coder.decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }
}
