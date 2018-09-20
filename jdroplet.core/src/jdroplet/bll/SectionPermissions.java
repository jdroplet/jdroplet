package jdroplet.bll;


import java.util.List;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IPermissionDataProvider;
import jdroplet.security.PermissionBase;


public class SectionPermissions {
    
	public static List<PermissionBase> getPermissions(int clusterId, int userId, int parentId, String type) {
		IPermissionDataProvider provider = DataAccess.instance().getPermissionDataProvider();
		return provider.getPermissions(clusterId, userId, parentId, type);
	}
	
	public static PermissionBase getPermission(int clusterId, int sectionId, int roleId) {
		IPermissionDataProvider provider = DataAccess.instance().getPermissionDataProvider();
		return provider.getPermission(clusterId, sectionId, roleId);
	}
	
	public static PermissionBase getPermission(int sectionId) {
		IPermissionDataProvider provider = DataAccess.instance().getPermissionDataProvider();
		return provider.getPermission(sectionId);
	}
		
	public static void add(List<PermissionBase> permissions) {
		IPermissionDataProvider provider = DataAccess.instance().getPermissionDataProvider();
		provider.add(permissions);
	}
	
	public static void update(List<PermissionBase> permissions) {
		IPermissionDataProvider provider = DataAccess.instance().getPermissionDataProvider();
		provider.update(permissions);
	}
	
	public static void delete(List<PermissionBase> permissions) {
		IPermissionDataProvider provider = DataAccess.instance().getPermissionDataProvider();
		provider.delete(permissions);
	}
}
