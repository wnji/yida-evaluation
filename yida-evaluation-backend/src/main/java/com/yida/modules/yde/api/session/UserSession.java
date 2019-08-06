package com.yida.modules.yde.api.session;

import com.yida.modules.yde.entity.YdeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 保存在session中的当前登录用户的信息
 */
@Component
public class UserSession implements Serializable {
    @Autowired
    private SessionService sessionService;

    /**
     * 从session取当前登录用户
     * @param token
     * @return
     */
    public YdeUser getUser(String token){
        return (YdeUser)sessionService.getAttributeByToken(token, "user");
    }

    /**
     * 保存当前登录用户到session
     * @param token
     * @param user
     */
    public void setUser(String token, YdeUser user){
        sessionService.setAttributeByToken(token, "user", user);
    }


}
