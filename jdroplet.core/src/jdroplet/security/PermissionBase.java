package jdroplet.security;

import java.io.Serializable;

import jdroplet.enums.AccessControlEntry;
import jdroplet.enums.Permission;


public abstract class PermissionBase implements Serializable {
	private static final long serialVersionUID = -7077070868774296340L;
	private int sectionId;
	private int roleId;
	private long allowMask;
	private long denyMask;

	public PermissionBase() {
		allowMask = 0;
        denyMask = 0;
	}
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int value) {
		this.sectionId = value;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public long getAllowMask() {
		return allowMask;
	}
	public void setAllowMask(long allowMask) {
		this.allowMask = allowMask;
	}
	public long getDenyMask() {
		return denyMask;
	}
	public void setDenyMask(long denyMask) {
		this.denyMask = denyMask;
	}
	
	
	public boolean getBit(long p) {
		boolean bRet = false;
		
		if ((denyMask & p) == p) 
			bRet = false;
		
		if ((allowMask & p) == p) 
			bRet = true;
		return bRet;
	}
	
	public void setBit(long p, AccessControlEntry ac) {
		switch(ac.getValue()) {
		case 1:// Allow
			allowMask = allowMask | (p & -1);
			denyMask = denyMask & ~(p & -1);
			break;
		case 2: // NotSet
			allowMask = allowMask & ~(p & -1);
			denyMask = denyMask & ~(p & -1);
			break;
		default:// Deny
			allowMask = allowMask & ~(p & -1);
			denyMask = denyMask | ~(p & -1);
			break;
		}
	}
	
	public void merge(PermissionBase pb) {
		this.allowMask	|= pb.allowMask;
        this.denyMask	|= pb.denyMask;
	}
	
	public boolean view() {
		return getBit(Permission.View);
	}
	public boolean post() {
		return getBit(Permission.Post);
	}
	public boolean edit() {
		return getBit(Permission.Edit);
	}
	public boolean delete() {
		return getBit(Permission.Delete);
	}
	public boolean vote() {
		return getBit(Permission.Vote);
	}
	public boolean createPoll() {
		return getBit(Permission.CreatePoll);
	}
}
