package jdroplet.data.idal;


public interface IMethodDataProvider {
	
	void delete(String tag);
	boolean exists(String tag);
	
//	List<Method> getMethods(String group);
//
//	void add(String tag, List<Method> methods);
}
