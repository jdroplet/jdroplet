package jdroplet.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.schooner.MemCached.MemcachedItem;
import jdroplet.core.SystemConfig;
import jdroplet.util.Encoding;


import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemCached implements ICache {

	private MemCachedClient mClient = null;

	public MemCached() {
		String[] servers = null;
		Integer[] weights = null;
		SockIOPool pool = null;

		mClient = new MemCachedClient();

		servers = SystemConfig.getProperty("app.memcached.servers").split(";");
		//servers = new String[]{"127.0.0.1:11211"};
		weights = new Integer[] { 3 };

		// 设置服务器信息
		pool = SockIOPool.getInstance();
		pool.setServers(servers);
		pool.setWeights(weights);

		// 设置初始连接数、最小和最大连接数以及最大处理时间
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaxIdle(1000 * 30 * 30);

		// 设置主线程的睡眠时间
		pool.setMaintSleep(30);

		// 设置TCP的参数，连接超时等
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(0);

		// 初始化连接池
		pool.initialize();
	}

	@Override
	public void add(String key, Object value) {
		add(key, value, ICache.HOUR_FACTOR);
	}

	@Override
	public void add(String key, Object value, int seconds) {
		mClient.add(key, value, new Date(seconds * 1000));
	}

	@Override
	public void add(String key, String group, Object value) {
		add(key, group, value, ICache.HOUR_FACTOR);
	}

	@Override
	public void add(String key, String group, Object value, int seconds) {
		add(key, new String[] { group }, value, seconds);
	}

	@Override
	public void add(String key, String[] groups, Object value) {
		add(key, groups, value, ICache.HOUR_FACTOR);
	}

	@Override
	public void add(String key, String[] groups, Object value, int seconds) {
		mClient.add(key, value, new Date(seconds * 1000));

		for (String groupKey : groups) {
			List<String> keys = null;
			MemcachedItem mi = null;

			mi = mClient.gets(groupKey);
			if (mi == null) {
				keys = new ArrayList<>();
			} else {
				keys = (List<String>) mi.getValue();
			}

			if (!keys.contains(key))
				keys.add(key);

			if (mi == null) {
				mClient.add(groupKey, keys, new Date(DAY_FACTOR * 3000));
			} else {
				boolean result = mClient.cas(groupKey, keys, new Date(DAY_FACTOR * 3000), mi.getCasUnique());
				if (result == false) { // 如果更新缓存失败，就清理掉所有内容
					keys = (List<String>) mClient.get(groupKey);
					if (keys != null) {
						for(String cacheKey:keys) {
							mClient.delete(cacheKey);
						}
						mClient.delete(groupKey);
					}
				}
			}
		}
	}

	@Override
	public Object get(String key) {
		return mClient.get(key);
	}

	@Override
	public Collection<Object> getValues() {
		return null;
	}

	@Override
	public Collection<String> getKeys() {
		//遍历statsItems 获取items:2:number=14
		List<String> keys = new ArrayList<String>();
		Map<String,Map<String,String>> statsItems=mClient.statsItems();
		Map<String,String> statsItems_sub=null;
		String statsItems_sub_key=null;
		int items_number=0;
		String server=null;
		//根据items:2:number=14，调用statsCacheDump，获取每个item中的key
		Map<String,Map<String,String>> statsCacheDump=null;
		Map<String,String> statsCacheDump_sub=null;
		String statsCacheDumpsub_key=null;
		// String statsCacheDumpsub_key_value=null;

		for (Iterator<String> iterator=statsItems.keySet().iterator();iterator.hasNext();) {
			server= iterator.next();
			statsItems_sub=statsItems.get(server);
			//System.out.println(server+"==="+statsItems_sub);
			for (Iterator<String> iterator_item=statsItems_sub.keySet().iterator();iterator_item.hasNext();) {
				statsItems_sub_key= iterator_item.next();
				//System.out.println(statsItems_sub_key+":=:"+bb);
				//items:2:number=14
				if (statsItems_sub_key.toUpperCase().startsWith("items:".toUpperCase()) && statsItems_sub_key.toUpperCase().endsWith(":number".toUpperCase())){
					items_number=Integer.parseInt(statsItems_sub.get(statsItems_sub_key).trim());
					//System.out.println(statsItems_sub_key+":=:"+items_number);
					statsCacheDump=mClient.statsCacheDump(new String[]{server},Integer.parseInt(statsItems_sub_key.split(":")[1].trim()), items_number);

					for (Iterator<String> statsCacheDump_iterator=statsCacheDump.keySet().iterator();statsCacheDump_iterator.hasNext();) {
						statsCacheDump_sub = statsCacheDump.get(statsCacheDump_iterator.next());
						//System.out.println(statsCacheDump_sub);
						for (Iterator<String> iterator_keys = statsCacheDump_sub.keySet().iterator(); iterator_keys.hasNext();) {
							statsCacheDumpsub_key = iterator_keys.next();
							//statsCacheDumpsub_key_value=statsCacheDump_sub.get(statsCacheDumpsub_key);
							//System.out.println(statsCacheDumpsub_key);//key是中文被编码了,是客户端在set之前编码的，服务端中文key存的是密文
							//System.out.println(statsCacheDumpsub_key_value);

							//keylist.put(URLDecoder.decode(statsCacheDumpsub_key, "UTF-8"), new KeysBean(server,Long.parseLong(statsCacheDumpsub_key_value.substring(1, statsCacheDumpsub_key_value.indexOf("b;")-1).trim()),Long.parseLong(statsCacheDumpsub_key_value.substring(statsCacheDumpsub_key_value.indexOf("b;")+2,statsCacheDumpsub_key_value.indexOf("s]")-1).trim())));
							keys.add(Encoding.urlDecode(statsCacheDumpsub_key, "UTF-8"));
						}
					}
				}

			}
		}
		return keys;
	}

	@Override
	public Collection<String> getKeys(String group) {
		List<String> keys = (List<String>) get(group);

		return keys;
	}

	@Override
	public void remove(String key) {
		mClient.delete(key);
	}

	@Override
	public void clear() {
		Collection<String> keys = getKeys();
		for(String key:keys) {
			this.mClient.delete(key);
		}
	}

	@Override
	public void clear(String group) {
		Collection<String> keys = getKeys(group);

		if (keys == null)
			return;

		for(String key:keys) {
			remove(key);
		}
	}
}
