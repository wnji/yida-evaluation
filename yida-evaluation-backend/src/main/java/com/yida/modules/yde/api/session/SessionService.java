package com.yida.modules.yde.api.session;

import com.yida.modules.yde.api.v1.cache.CacheService;
import com.yida.modules.yde.api.security.TokenService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Component
public class SessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TokenService tokenService;

    public String createSesseionByToken(String token) {
        Claims claims = tokenService.getClaims(token);
        String sessionId = "session-" +claims.getSubject();

        Hashtable<String, Object> sessionData = new Hashtable<String, Object>();
        Long sessionIssuedAt = Long.valueOf(claims.getIssuedAt().getTime());
        sessionData.put("sessionIssuedAt", sessionIssuedAt);

        LOGGER.debug("createSesseionByToken, sessionId:"+sessionId + ", sessionIssuedAt:"+ sessionIssuedAt);

        cacheService.put(sessionId,sessionData );

        return sessionId;
    }

    public void invalidateSesseion(String token) {
        Claims claims = tokenService.getClaims(token);
        String sessionId = "session-" +claims.getSubject();

        cacheService.remove(sessionId );
    }

    public  String getSessionId(String token){
        Claims claims = tokenService.getClaims(token);

        String sessionId = "session-" +claims.getSubject();

        return sessionId;
    }

    public boolean sessionExists(String token){
        if(StringUtils.isBlank(token)){
          return false;
        }

        boolean exists = false;

        try {
            Claims claims = tokenService.getClaims(token);
            String sessionId = "session-" + claims.getSubject();
            long issuedAt = claims.getIssuedAt().getTime();

            Hashtable<String, Object> sessionData = (Hashtable<String, Object>)cacheService.get(sessionId);
            if(sessionData!=null){
                Long sessionIssuedAt = (Long)sessionData.get("sessionIssuedAt");

                LOGGER.debug("sessionExists, issuedAt:"+issuedAt + ", sessionIssuedAt:"+sessionIssuedAt);
                if(sessionIssuedAt.longValue() == issuedAt){
                    exists = true;
                }
            }
            else{
                LOGGER.debug("sessionId:"+sessionId + ", sessionData is null");
            }
        } catch (Exception e) {
            LOGGER.error("SessionService.sessionExists", e);
        }

        return exists;
    }

    public Object getAttribute(String sessionId, String key) {
        Hashtable<String, Object> sessionData = (Hashtable<String, Object>)cacheService.get(sessionId);

        if(sessionData!=null){
            return sessionData.get(key);
        }

        return null;
    }

    public Object getAttributeByToken(String token, String key){
        String sessionId = this.getSessionId(token);
        return this.getAttribute(sessionId, key);
    }

    public void setAttribute(String sessionId, String key, Object value) {
        Hashtable<String, Object> sessionData = (Hashtable<String, Object>)cacheService.get(sessionId);

        if(sessionData!=null){
            if(value!=null) {
                sessionData.put(key, value);
            }
            else{
                sessionData.remove(key);
            }
            cacheService.put(sessionId,sessionData );
        }
    }

    public void setAttributeByToken(String token, String key, Object value) {
        String sessionId = this.getSessionId(token);
        this.setAttribute(sessionId, key, value);
    }
}
