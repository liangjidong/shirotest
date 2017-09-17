package shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by author on 2017/8/29.
 */
public class CustomShiroSessionDao extends AbstractSessionDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ShiroSessionRepository shiroSessionRepository;
    //反序列化需要使用--默认使用SimpleSession
    private Class<? extends Session> sessionType;

    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        getShiroSessionRepository().saveSession(session);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        //写死，SimpleSession。因此要求客户端
        return getShiroSessionRepository().getSession(sessionId, sessionType);
    }

    public void update(Session session) throws UnknownSessionException {
        getShiroSessionRepository().saveSession(session);
    }

    public void delete(Session session) {
        if (session == null) {
            logger.error("session is null");
        }
        Serializable sessionId = session.getId();
        if (sessionId != null)
            getShiroSessionRepository().deleteSession(sessionId);
    }

    public Collection<Session> getActiveSessions() {
        return getShiroSessionRepository().getAllSessions(sessionType);
    }

    public ShiroSessionRepository getShiroSessionRepository() {
        return shiroSessionRepository;
    }

    public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
        this.shiroSessionRepository = shiroSessionRepository;
    }

    public Class<? extends Session> getSessionType() {
        if (this.sessionType == null)
            this.sessionType = SimpleSession.class;
        return sessionType;
    }

    public void setSessionType(Class<? extends Session> sessionType) {
        this.sessionType = sessionType;
    }
}
