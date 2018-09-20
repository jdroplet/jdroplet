package jdroplet.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import jdroplet.bll.Methods;
import jdroplet.core.SystemConfig;
import jdroplet.enums.SortOrder;
import jdroplet.util.SortUtil;

import org.apache.log4j.Logger;

import javax.lang.model.type.NullType;


public class PluginFactory {

	class ClassRef {
		Class clazz = null;
		String method = null;
		int priority = 0;
	}
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Map<String, List<ClassRef>> mActions = null;
	private Map<String, List<ClassRef>> mFilters = null;
	private Map<String, String> mCalls = null;
	private ClassLoader parent_loader = null;
	
	private static PluginFactory inst = null;
	public static PluginFactory getInstance() {
		if (inst == null) 
			inst = new PluginFactory();
		
		return inst;
	}
	
	private PluginFactory() {
		mActions = new HashMap<String, List<ClassRef>>();
		mFilters = new HashMap<String, List<ClassRef>>();
		mCalls = new HashMap<String, String>();
	}
	
	public ClassLoader getClassLoader() {
		return parent_loader;
	}
	
	@SuppressWarnings("unchecked")
	public void launch() {
		URL url = null;
		URLClassLoader loader = null;
		Class<IPlugin> clazz = null;
		JarFile jar = null;
		Manifest mf = null;
		String clazz_name = null;
		IPlugin plugin = null;
		File file = null;
		File[] files = null;
		String plugin_path = null;
		
		// 加载服务器中的插件
		plugin_path = SystemConfig.getServerPath() + SystemConfig.getPluginPath();
		file = new File(plugin_path);
		files = file.listFiles();
		parent_loader = Thread.currentThread().getContextClassLoader();
		
		for (File f : files) {
			try {
				jar = new JarFile(f);
				mf = jar.getManifest();
				clazz_name = mf.getMainAttributes().getValue("Bundle-Activator");
	
				url = f.toURI().toURL();
				loader = (URLClassLoader) new URLClassLoader(new URL[] { url },
						parent_loader);				
				
				clazz = (Class<IPlugin>) loader.loadClass(clazz_name);
				plugin = (IPlugin) clazz.newInstance();
				plugin.star();
				
				parent_loader = loader;
				
				logger.info(jar.getName() + " load complete");
			} catch (Exception ex) {
				logger.error("plugin launch error", ex);
			} finally {
				try { jar.close(); } catch (IOException e) {}
			}
		}
	}
	
	public List<ClassRef> getFilters(String tag) {
		List<ClassRef> filters = null;
		
		filters = mFilters.get(tag);
		if (filters == null)
			return null;
		
		SortUtil.bubbleSort(filters.toArray(), "priority", SortOrder.ASC);
		return filters;
	}
	
	public List<ClassRef> getActions(String tag) {
		List<ClassRef> actions = null;
		
		actions = mActions.get(tag);
		if (actions == null)
			return null;
		
		SortUtil.bubbleSort(actions.toArray(), "priority", SortOrder.ASC);
		return actions;
	}
	
	@SuppressWarnings("rawtypes")
	public void addAction(String tag, Class clazz, String method) {
		addAction(tag, clazz, method, 10);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addAction(String tag, Class clazz, String method, int priority) {
		List<ClassRef> refs = null;
		ClassRef mref = null;
		String key = null;

		key = tag + "_" + clazz.toString() + '@' + method;
		if (mCalls.containsKey(key))
			return;

		refs = this.mActions.get(tag);
		if (refs == null) {
			refs = new ArrayList<ClassRef>();
			this.mActions.put(tag, refs);
		}
		mref = new ClassRef();
		mref.clazz = clazz;
		mref.method = method;
		mref.priority = priority;

		refs.add(mref);
		mCalls.put(key, null);
	}
	
	@SuppressWarnings("rawtypes")
	public void addFilter(String tag, Class clazz, String method) {
		addFilter(tag, clazz, method, 10);
	}
	
	public boolean hasActions(String tag) {
		return this.mActions.containsKey(tag) || Methods.exists(tag);
	}
	
	public boolean hasFilters(String tag) {
		return this.mFilters.containsKey(tag) || Methods.exists(tag);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addFilter(String tag, Class clazz, String method, int priority) {
		List<ClassRef> refs = null;
		ClassRef mref = null;
		String key = null;

		key = tag + "_" + clazz.toString() + '@' + method;
		if (mCalls.containsKey(key))
			return;

		refs = this.mFilters.get(tag);
		if (refs == null) {
			refs = new ArrayList<ClassRef>();
			this.mFilters.put(tag, refs);
		}
		mref = new ClassRef();
		mref.clazz = clazz;
		mref.method = method;
		mref.priority = priority;

		refs.add(mref);
		mCalls.put(key, null);
	}
	
	public void doAction(String tag, Object...args) {
		List<ClassRef> clzzes = null;
		Class clazz = null;
		java.lang.reflect.Method m = null;
		Class<?> types[] = null;

		clzzes = getActions(tag);
		if (clzzes != null) {
			for (ClassRef ref : clzzes) {
				try {
					types = new Class[args.length];
					for (int i = 0; i < args.length; i++) {
						types[i] = args[i].getClass();
					}
					m = ref.clazz.getMethod(ref.method, types);
					m.invoke(null, args);

					//logger.info("doAction:" + ref.clazz.toString());
				} catch (Exception ex) {
					logger.error("doAction error", ex);
				}
			}
		}
	}
	
	public Object applyFilters(String tag, Object result, Object...args) {
		List<ClassRef> clzzes = null;
		java.lang.reflect.Method m = null;
		Class<?> types[] = null;
		Object objs[] = null;

		clzzes = getFilters(tag);
		if (clzzes != null) {
			for (ClassRef ref : clzzes) {
				try {
					types = new Class[args.length + 1];
					if (result==null)
						types[0] = NullType.class;
					else
						types[0] = result.getClass();

					for (int i = 1; i < args.length+1; i++) {
						types[i] = args[i-1].getClass();
					}

					objs = new Object[args.length+1];
					objs[0] = result;
					for (int i = 1; i < args.length+1; i++) {
						objs[i] = args[i-1];
					}

					m = ref.clazz.getMethod(ref.method, types);
					result = m.invoke(null, objs);

					//logger.info("applyFilters:" + ref.clazz.toString());
				} catch (Exception ex) {
					logger.error("applyFilters error", ex);
				}
			}
		}
		
		return result;
	}
}
