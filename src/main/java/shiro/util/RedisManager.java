package shiro.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by author on 2017/8/29.
 */
public class RedisManager {

    private static final Logger log = LoggerFactory.getLogger(RedisManager.class);

    private static JedisPool jedisPool;
    private static int expireTime = 1800;
    private static int countExpireTime = 2 * 24 * 3600;
    private static String password = "admin";
    private static String redisIp = "192.168.200.133";
    private static int redisPort = 6379;
    private static int maxActive = 200;
    private static int maxIdle = 200;
    private static long maxWait = 5000;

    static {
        initPool();
    }

    // 初始化连接池
    public static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config, redisIp, redisPort, 10000, password);
    }

    public static byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    public static byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            jedis.expire(key, expireTime);
        } finally {
            jedis.close();
        }
        return value;
    }

    public static byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
        return value;
    }


    public static void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public static Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
        } finally {
            jedis.close();
        }
        return keys;
    }

    // 从连接池获取redis连接
    public static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
//            jedis.auth(password);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return jedis;
    }

    // 回收redis连接
    public static void recycleJedis(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

}
