package com.example.springwebserver.service;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
    public boolean expire(String key, long time);

    public long getExpire(String key);

    public boolean hasKey(String key);

    public void del(String... key);

    public Object get(String key);

    public boolean set(String key, Object value);

    public boolean set(String key, Object value, long time);

    public long incr(String key, long delta);

    public long decr(String key, long delta);

    public Object hget(String key, String item);

    public Map<Object, Object> hmget(String key);

    public boolean hmset(String key, Map<String, Object> map);

    public boolean hmset(String key, Map<String, Object> map, long time);

    public boolean hset(String key, String item, Object value);

    public boolean hset(String key, String item, Object value, long time);

    public void hdel(String key, Object... item);

    public boolean hHasKey(String key, String item);

    public double hincr(String key, String item, double by);

    public double hdecr(String key, String item, double by);

    public Set<Object> sGet(String key);

    public boolean sHasKey(String key, Object value);

    public long sSet(String key, Object... values);

    public long sSetAndTime(String key, long time, Object... values);

    public long sGetSetSize(String key);

    public long setRemove(String key, Object... values);

    public List<Object> lGet(String key, long start, long end);

    public long lGetListSize(String key);

    public Object lGetIndex(String key, long index);

    public boolean lSet(String key, Object value);

    public boolean lSet(String key, Object value, long time);

    public boolean lSet(String key, List<Object> value);

    public boolean lSet(String key, List<Object> value, long time);

    public boolean lUpdateIndex(String key, long index, Object value);

    public long lRemove(String key, long count, Object value);
}
