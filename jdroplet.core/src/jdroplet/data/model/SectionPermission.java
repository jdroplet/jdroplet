package jdroplet.data.model;

import jdroplet.bll.Moderate;
import jdroplet.core.HttpContext;
import jdroplet.enums.Permission;
import jdroplet.exceptions.ApplicationException;
import jdroplet.security.PermissionBase;




public class SectionPermission extends PermissionBase {
	
	public boolean view() {
		return getBit( Permission.View );
	}

	public boolean post() {
		return getBit( Permission.Post );
	}
    
	public boolean edit() {
		return getBit( Permission.Edit );
	}
    
	public boolean delete() {
		return getBit( Permission.Delete );
	}
    
	public boolean vote() {
		return getBit( Permission.Vote );
	}

	public boolean createPoll() {
		return getBit( Permission.CreatePoll );
	}
    
	public boolean moderate() {
		return getBit( Permission.Moderate );
	}

	public boolean administer() {
		return getBit( Permission.Administer );
	}
	
	public static void accessCheck(Section section, long permission, User user, Post post) {
		HttpContext context = HttpContext.current();


		Integer forumId;
		if (post != null)
			forumId = Integer.valueOf(1);
		else
			forumId = section.getId();

		if (Moderate.IsForumModerator(user, null, forumId))
			return;
		
		SectionPermission fp = (SectionPermission)section.getResolvePermission(user);
		
		long lValue = permission;
		
		if (lValue == 0) {
			if (!context.request().isAuthenticated())
				throw new ApplicationException("AccessDeny");
	
			if( !fp.administer() )
				throw new ApplicationException("AccessDeny");
		} else {
			
		}
	}
	
	public static boolean validate(Section section, long permission, User user, Post p) {
		if(section == null || section.getPermissionSet() == null || user == null )
			return false;

        if(user.isAdministrator())
            return true;

		boolean bReturn = true;
		SectionPermission fpFinal = (SectionPermission)section.getResolvePermission(user);

        //if(fpFinal.moderate())
        //    return true;

        if((permission == (permission | Permission.Administer)) && bReturn )
			bReturn &= fpFinal.administer();

		if((permission == (permission | Permission.CreatePoll)) && bReturn )
			bReturn &= fpFinal.createPoll();
        
		if((permission == (permission | Permission.Delete)) && bReturn ) 
			bReturn &= fpFinal.delete();

		if((permission == (permission | Permission.Edit)) && bReturn )
			bReturn &= fpFinal.edit();

		if((permission == (permission | Permission.Moderate)) && bReturn )
			bReturn &= fpFinal.moderate();

		if((permission == (permission | Permission.Post)) && bReturn )
			bReturn &= fpFinal.post();

		if((permission == (permission | Permission.View)) && bReturn )
			bReturn &= fpFinal.view();

		if((permission == (permission | Permission.Vote)) && bReturn )
			bReturn &= fpFinal.vote();

		return bReturn;
	}
}
