package com.yida.modules.yde.api.v1.cache;

import com.jeesite.common.cache.CacheUtils;
import org.springframework.stereotype.Component;

@Component
public class CacheService {

    public CacheService(){
    }

    public void put(String key, Object value){
        CacheUtils.put(key, value);
    }

    public Object get(String key){
        return CacheUtils.get(key);
    }

    public void remove(String key){
        CacheUtils.remove(key);
    }
}
