package shiro.session;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by author on 2017/9/15.
 */
public class EcacheShiroSessionRepository implements ShiroSessionRepository {
    Cache<Serializable, Session> sessionCache;

    public EcacheShiroSessionRepository(CacheManager cacheManager) {
        this.sessionCache = cacheManager.getCache("shiro-activeSessionCache");
    }

    @Override
    public void saveSession(Session session) {
        this.sessionCache.put(session.getId(), session);
    }

    @Override
    public void deleteSession(Serializable sessionId) {
        this.sessionCache.remove(sessionId);
    }

    @Override
    public Session getSession(Serializable sessionId, Class<? extends Session> cls) {
        return this.sessionCache.get(sessionId);
    }

    @Override
    public Collection<Session> getAllSessions(Class<? extends Session> cls) {
        Set<Serializable> keys = this.sessionCache.keys();
        List<Session> sessions = new ArrayList<>(keys.size());
        for (Serializable key : keys) {
            sessions.add(this.sessionCache.get(key));
        }
        return sessions;
    }
}
