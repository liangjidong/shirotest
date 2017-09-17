package shiro.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by author on 2017/8/29.
 */
public interface ShiroSessionRepository {
    void saveSession(Session session);

    void deleteSession(Serializable sessionId);

    Session getSession(Serializable sessionId, Class<? extends Session> cls);

    Collection<Session> getAllSessions(Class<? extends Session> cls);
}
