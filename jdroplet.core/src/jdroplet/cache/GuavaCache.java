package jdroplet.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kuibo on 2017/10/10.
 */
public class GuavaCache implements ICache {

    private Cache<String, Object> cache;

    public GuavaCache() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(3600, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void add(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void add(String key, Object value, int seconds) {
        add(key, value);
    }

    @Override
    public void add(String key, String group, Object value) {
        cache.put(key, value);

        List<String> keys = null;

        keys = (List<String>) cache.getIfPresent(group);
        if (keys == null) {
            keys = new ArrayList<>();
        }
        keys.add(key);
        cache.put(group, keys);
    }

    @Override
    public void add(String key, String group, Object value, int seconds) {
        add(key, group, value);
    }

    @Override
    public void add(String key, String[] groups, Object value) {
        cache.put(key, value);

        List<String> keys = null;
        for(String group:groups) {
            keys = (List<String>) cache.getIfPresent(group);
            if (keys == null) {
                keys = new ArrayList<>();
            }
            keys.add(key);

            cache.invalidate(group);
            cache.put(group, keys);
        }
    }

    @Override
    public void add(String key, String[] groups, Object value, int seconds) {
        add(key, groups, value);
    }

    @Override
    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Collection<Object> getValues() {
        return cache.asMap().values();
    }

    @Override
    public Collection<String> getKeys() {
        return cache.asMap().keySet();
    }

    @Override
    public Collection<String> getKeys(String group) {
        return (List<String>) cache.getIfPresent(group);
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    @Override
    public void clear(String group) {
        List<String> keys = null;

        keys = (List<String>) cache.getIfPresent(group);
        if (keys != null)
            cache.invalidateAll(keys);
    }
}
