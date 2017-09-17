package shiro.session;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiro.util.RedisManager;
import shiro.util.SerializableUtils;
import shiro.util.SerializableUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by author on 2017/8/29.
 */
public class JedisShiroSessionRepository implements ShiroSessionRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String SESSION_PREFIX = "shiro-session";


    public void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        byte[] key = getByteKey(session.getId());
        byte[] value = SerializableUtils.serialize(session);

        logger.info("session序列化为:" + value);

        Long timeout = session.getTimeout() / 1000;
        RedisManager.set(key, value, timeout.intValue());
    }


    public void deleteSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.error("session id is null");
            return;
        }
        byte[] key = getByteKey(sessionId);
        RedisManager.del(key);
    }

    public Session getSession(Serializable sessionId, Class<? extends Session> cls) {
        if (sessionId == null) {
            logger.error("session id is null");
            return null;
        }
        Session session = null;
        byte[] key = getByteKey(sessionId);
        byte[] value = RedisManager.get(key);
        session = (Session) SerializableUtils.deserialize(value);
        return session;
    }

    public Collection<Session> getAllSessions(Class<? extends Session> cls) {
        Set<Session> sessions = new HashSet<Session>();
        Set<byte[]> byteKeys = RedisManager.keys(this.SESSION_PREFIX + "*");
        if (byteKeys != null && byteKeys.size() > 0) {
            for (byte[] byteKey : byteKeys) {
                Session s = (Session) SerializableUtils.deserialize(RedisManager.get(byteKey));
                sessions.add(s);
            }
        }
        return sessions;
    }

    private byte[] getByteKey(Serializable id) {
        String key = this.SESSION_PREFIX + id;
        return key.getBytes();
    }
}
