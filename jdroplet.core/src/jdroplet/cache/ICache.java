package jdroplet.cache;

import java.util.Collection;

public interface ICache {
	static final String DUMMY_FQN = "";
	static final String NOTIFICATION = "notification";
	
	static final int MAX_FACTOR = Integer.MAX_VALUE;
	static final int MONTH_FACTOR = 259200; //30天
	static final int DAY_FACTOR = 86400;//1天
    static final int HOUR_FACTOR = 3600;//1小时
    static final int MINUTE_FACTOR = 60;//1分钟
    static final int SECOND_FACTOR = 10; //10s

    void add(String key, Object value);
	void add(String key, Object value, int seconds);
	
	void add(String key, String group, Object value);
	void add(String key, String group, Object value, int seconds);
	void add(String key, String[] groups, Object value);
	void add(String key, String[] groups, Object value, int seconds);

	Object get(String key);
	
	Collection<Object> getValues();
	
	Collection<String> getKeys();
	Collection<String> getKeys(String group);
	
	void remove(String key);
	void clear();
	void clear(String group);
}
