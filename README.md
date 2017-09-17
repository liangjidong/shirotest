# shirotest
## 基于ssm+shiro
## 1，实现基于shiro+(redis或ecache)的session共享

## 2，实现基于RSA加密的密码传输（Http下）
java端的加密解密和js端的加密解密方法应该匹配。   
js端使用jsencrypt.js包的方法公钥加密
java端使用如下代码私钥解密

```java
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
```

## 3，实现用户注册，基于邮箱的账户激活

## 4，实现基于邮箱的密码找回
3,4功能点参考http://blog.csdn.net/xyang81/article/details/7727141
注意点：使用Base64将字节数组编码成字符串，拼接到url的时候，应该使用url safe的方式，对+ = 分别使用- _ 代替，使用commons-codec中提供的base64 SAFE方法就能实现
