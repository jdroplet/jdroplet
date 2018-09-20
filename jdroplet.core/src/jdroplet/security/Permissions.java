package jdroplet.security;

import jdroplet.data.model.Post;
import jdroplet.data.model.Section;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;

public class Permissions {
    
    public static void remove (PermissionBase p) {
    }

    public static void add (PermissionBase p) {
    	
    }
    
//    private static void validate(PermissionBase p) {
//        if(p.getApplicationType() == ApplicationType.Unknown)
//            throw new ApplicationException(ApplicationExceptionType.PermissionApplicationUnknown,"Permission has an Unknown ApplicationType");
//    }
    
    public static boolean validatePermissions(Section section, long permission, User user) {
        return validatePermissions(section, permission, user, null);
    }
    
    public static boolean validatePermissions(Section section, long permission, User user, Post p) {
        return section.validate(permission, user, p);
    }
    
    public static void accessCheck (Section section, long permission, User user, Post post) {
    	if(section == null)
            throw new ApplicationException("Unknown Section Requested");

        section.accessCheck(permission, user, post);
    } 
}