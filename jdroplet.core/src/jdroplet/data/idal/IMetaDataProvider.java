package jdroplet.data.idal;

import jdroplet.data.model.Meta;



public interface IMetaDataProvider {

	void remove(Class<?> clazz, Integer itemId, String key);

	int save(Meta meta);
	void save(Class<?> clazz, Integer itemId, String key, String value);
	String getValue(Class<?> clazz, Integer itemId, String key);
}
