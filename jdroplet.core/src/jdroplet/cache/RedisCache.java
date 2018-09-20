package jdroplet.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jdroplet.core.SystemConfig;
import jdroplet.util.ByteUtil;

import org.apache.log4j.Logger;

import redis.clients.jedis.*;

public class RedisCache {

	private static Logger log = Logger.getLogger(RedisCache.class);

	private static JedisPool sPool = null;
	public static Logger logger = Logger.getLogger(RedisCache.class);

	static {
		String server = null;
		JedisPoolConfig pool_config = null;

		server = SystemConfig.getProperty("app.redis.server");
//		server = "192.168.33.55";
		
		pool_config = new JedisPoolConfig();
		pool_config.setMaxTotal(500);// 设置最大连接数
		pool_config.setMaxIdle(80); // 设置最大空闲数
		pool_config.setMaxWaitMillis(60);// 设置超时时间
		pool_config.setTestOnBorrow(true);

		sPool = new JedisPool(pool_config, server);
	}

	public static void remove(String key) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	public static Object get(String key) {
		Object obj = null;
		byte[] bytes = null;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			bytes = jedis.get(key.getBytes());
			if (bytes != null)
				obj = ByteUtil.b2o(bytes);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return obj;
		}
	}

	public static String getString(String key) {
		Jedis jedis = null;
		String val = null;

		try {
			jedis = sPool.getResource();
			val = jedis.get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return val;
		}
	}

	public static void set(String key, String value) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	public static void set(String key, Object obj) {
		byte[] bytes = null;
		Jedis jedis = null;

		try {
			bytes = ByteUtil.o2b(obj);
			jedis = sPool.getResource();
			jedis.set(key.getBytes(), bytes);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static void setMapValue(String key, String field, String val) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.hset(key, field, val);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static void removeMapField(String key, String... fields) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.hdel(key, fields);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static String getMapStringValue(String key, String field) {
		Jedis jedis = null;
		String val = null;

		try {
			jedis = sPool.getResource();
			val = jedis.hget(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return val;
		}
	}

	public static void setMapValue(String key, String field, Object val) {
		Jedis jedis = null;
		byte[] bytes = null;

		try {
			bytes = ByteUtil.o2b(val);
			jedis = sPool.getResource();
			jedis.hset(key.getBytes(), field.getBytes(), bytes);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static Object getMapValue(String key, String field) {
		Jedis jedis = null;
		byte[] bytes = null;
		Object obj = null;

		try {
			jedis = sPool.getResource();
			bytes = jedis.hget(key.getBytes(), field.getBytes());
			obj = ByteUtil.b2o(bytes);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return obj;
		}
	}

	public static void setMap(String key, Map<byte[], byte[]> map) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.hmset(key.getBytes(), map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	public static List<String> getMapStringValues(String key, String... fields) {
		Jedis jedis = null;
		List<String> vals = null;

		try {
			jedis = sPool.getResource();
			vals = jedis.hmget(key, fields);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return vals;
		}
	}

	public static List<Object> getMapValues(String key, String... fields) {
		Jedis jedis = null;
		List<Object> vals = null;
		List<byte[]> bytes = null;
		byte[][] bFields = null;
		Object obj = null;

		bFields = new byte[fields.length][];
		for (int i = 0; i < fields.length; i++) {
			bFields[i] = fields[i].getBytes();
		}

		try {
			jedis = sPool.getResource();
			bytes = jedis.hmget(key.getBytes(), bFields);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		vals = new ArrayList<Object>(fields.length);
		for (int i = 0; i < bytes.size(); i++) {
			try {
				obj = ByteUtil.b2o(bytes.get(i));
			} catch (Exception e) {
			}
			vals.add(obj);
		}

		return vals;
	}

	public static Set<String> getMapFields(String key) {
		Jedis jedis = null;
		Set<String> fields = null;

		try {
			jedis = sPool.getResource();
			fields = jedis.hkeys(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return fields;
		}
	}

	/**
	 * 获取队列长度
	 * 
	 * @param key
	 * @return
	 */
	public static long getListLength(String key) {
		Jedis jedis = null;
		long length = 0;

		try {
			jedis = sPool.getResource();
			length = jedis.llen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return length;
		}
	}

	/**
	 * 将values压入一个队列
	 * 
	 * @param key
	 *            队列的键值
	 * @param values
	 *            需要压入队列的值
	 */
	public static void push(String key, String... values) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.rpush(key, values);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 将values压入一个队列
	 * 
	 * @param key
	 *            队列的键值
	 * @param values
	 *            需要压入队列的值
	 */
	public static void push(String key, Object... values) {
		Jedis jedis = null;
		byte[][] bValues = null;

		bValues = new byte[values.length][];
		for (int i = 0; i < values.length; i++) {
			try {
				bValues[i] = ByteUtil.o2b(values[i]);
			} catch (IOException e) {
			}
		}

		try {
			jedis = sPool.getResource();
			jedis.rpush(key.getBytes(), bValues);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 将队列尾删除
	 * 
	 * @param key
	 *            队列的键值
	 * @return 返回删除的内容
	 */
	public static String popString(String key) {
		Jedis jedis = null;
		String value = null;

		try {
			jedis = sPool.getResource();
			value = jedis.rpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return value;
		}
	}

	/**
	 * 将队列尾删除
	 * 
	 * @param key
	 *            队列的键值
	 * @return 返回删除的内容
	 */
	public static Object pop(String key) {
		Jedis jedis = null;
		Object obj = null;
		byte[] bytes = null;

		try {
			jedis = sPool.getResource();
			bytes = jedis.rpop(key.getBytes());
			obj = ByteUtil.b2o(bytes);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return obj;
		}
	}

	/**
	 * 将values插入到队列头
	 * 
	 * @param key
	 *            队列的键值
	 * @param values
	 *            需要压入队列的值
	 */
	public static void insert2Head(String key, String... values) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.lpush(key, values);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 将values插入到队列头
	 * 
	 * @param key
	 *            队列的键值
	 * @param values
	 *            需要压入队列的值
	 */
	public static void insert2Head(String key, Object... values) {
		Jedis jedis = null;
		byte[][] bValues = null;

		bValues = new byte[values.length][];
		for (int i = 0; i < values.length; i++) {
			try {
				bValues[i] = ByteUtil.o2b(values[i]);
			} catch (IOException e) {
			}
		}

		try {
			jedis = sPool.getResource();
			jedis.lpush(key.getBytes(), bValues);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 将队列头删除
	 * 
	 * @param key
	 *            队列的键值
	 * @return 返回删除的内容
	 */
	public static String deleHeadString(String key) {
		Jedis jedis = null;
		String value = null;

		try {
			jedis = sPool.getResource();
			value = jedis.lpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return value;
		}
	}

	/**
	 * 将队列头删除
	 * 
	 * @param key
	 *            队列的键值
	 * @return 返回删除的内容
	 */
	public static Object deleHead(String key) {
		Jedis jedis = null;
		Object obj = null;
		byte[] bytes = null;

		try {
			jedis = sPool.getResource();
			bytes = jedis.lpop(key.getBytes());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		try {
			obj = ByteUtil.b2o(bytes);
		} catch (Exception e) {
		}
		return obj;
	}

	/**
	 * 获取一个队列
	 * 
	 * @param key
	 *            队列的键值
	 * @return 队列
	 */
	public static List<Object> getList(String key) {
		return getList(key, 0, -1);
	}

	/**
	 * 获取一个范围的队列
	 * 
	 * @param key
	 *            队列的键值
	 * @return 队列
	 */
	public static List<Object> getList(String key, int start, int end) {
		List<Object> list = null;
		List<byte[]> bytes = null;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			bytes = jedis.lrange(key.getBytes(), start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		list = new ArrayList<Object>();
		for (int i = 0; i < bytes.size(); i++) {
			try {
				list.add(ByteUtil.b2o(bytes.get(i)));
			} catch (Exception ex) {
				logger.error("getStringList error", ex);
			}
		}
		return list;
	}

	public static List<String> getListString(String key, int start, int end) {
		List<String> list = null;
		List<byte[]> bytes = null;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			bytes = jedis.lrange(key.getBytes(), start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		list = new ArrayList<String>();
		for (int i = 0; i < bytes.size(); i++) {
			try {
				list.add(new String(bytes.get(i)));
			} catch (Exception ex) {
				logger.error("getStringList error", ex);
			}
		}
		return list;
	}

	public static long getRankListLength(String key) {
		Jedis jedis = null;
		long size = 0;

		try {
			jedis = sPool.getResource();
			size = jedis.zcard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return size;
	}

	public static void addScore(String key, String member, int score) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.zincrby(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static Set<Tuple> getRankList(String key) {
		return getRankList(key, 0, -1);
	}

	public static Set<Tuple> getRankList(String key, int start, int end) {
		Set<Tuple> elements = null;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			elements = jedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		return elements;
	}

	public static long getRankListSize(String key) {
		Jedis jedis = null;
		long size = 0;

		try {
			jedis = sPool.getResource();
			size = jedis.zcard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return size;
	}

	public static long getRank(String key, String member) {
		Jedis jedis = null;
		long rank = 0;

		try {
			jedis = sPool.getResource();
			rank = jedis.zrevrank(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		return rank;
	}

	public static boolean isExistUpdate(String key, int expire) {
		Jedis jedis = null;
		long status = -1;

		try {
			jedis = sPool.getResource();
			status = jedis.setnx(key, "true");
			if (status > 0)
				jedis.expire(key, expire);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return status <= 0 ? true : false;
	}

	public static double zscore(String key, String member) {
		double score = 0.0;

		Jedis jedis = null;
		try {
			jedis = sPool.getResource();

			Double temp = jedis.zscore(key, member);
			if (temp != null) {
				score = temp.doubleValue();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return score;
	}

	public static long incr(String key) {
		long score = 0;

		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			score = jedis.incr(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return score;
	}

	public static Long expireAt(String key, long unixTime) {
		long score = 0;

		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			score = jedis.expireAt(key, unixTime);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return score;
	}

	public static Long expire(String key, int time) {
		long score = 0;

		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			score = jedis.expire(key, time);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return score;
	}

	public static Long hincrBy(String key, String field, long value) {

		long score = 0;

		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			score = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return score;
	}

	public static Map<String, String> hgetAll(String key) {

		Map<String, String> values = null;

		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			values = jedis.hgetAll(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return values;
		}
	}

	public static Long zrevrank(String key, String member) {

		long score = 0;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			score = jedis.zrevrank(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			return score;
		}

	}

	public static long zadd(String key, double score, String member) {

		long result = 0;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			result = jedis.zadd(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	public static Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			result = jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	public static Set<String> zcount(String key, double min, double max) {
		Set<String> result = null;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			result = jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	public static int generateUserId(String key, int maxNumer) {
		int result = -1;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			result = jedis.incrBy(key, 1).intValue();

			// 如果大于最大值就删除掉之，然后重新获取
			if (result >= maxNumer) {
				jedis.del(key);
				result = jedis.incrBy(key, 1).intValue();
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	public static boolean isKeyExists(String key) {
		boolean result = false;
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			result = jedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	public static void lrem(String key, long count, String value) {
		Jedis jedis = null;

		try {
			jedis = sPool.getResource();
			jedis.lrem(key, count, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	public static boolean hexists(String key, String field) {
		Jedis jedis = null;
		boolean result = false;

		try {
			jedis = sPool.getResource();
			result = jedis.hexists(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}
	
	public static long hlen(String key) {
		Jedis jedis = null;
		long result = 0;

		try {
			jedis = sPool.getResource();
			result = jedis.hlen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	public static boolean setX(String key, String value) {
		Jedis jedis = null;
		Transaction transaction = null;
		boolean result = false;

		jedis = sPool.getResource();
		jedis.watch(key);
		transaction = jedis.multi();
		transaction.set(key, value);
		List<Object> obj = transaction.exec();
		if (obj != null && obj.size() > 0) {
			String data = (String) obj.get(0);
			result = "OK".equals(data);
		}
		jedis.unwatch();
		jedis.close();

		return result;
	}

}
