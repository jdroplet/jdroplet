package jdroplet.data.idal;

import java.util.List;

import jdroplet.security.PermissionBase;


public interface IPermissionDataProvider {
	List<PermissionBase> getPermissions(int clusterId, int userId, int parentId, String type);
	PermissionBase getPermission(int clusterId, int sectionId, int roleId);
	PermissionBase getPermission(int sectionId);
	
	void add(List<PermissionBase> permissions);
	void update(List<PermissionBase> permissions);
	void delete(List<PermissionBase> permissions);
}
