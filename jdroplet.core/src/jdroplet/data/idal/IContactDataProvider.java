package jdroplet.data.idal;

import jdroplet.data.model.Contact;

public interface IContactDataProvider extends IDataProvider {

    boolean exists(Integer shopId, Integer activityId, Integer userId);

    Contact getUserDefaultContact(Integer userId);
}
