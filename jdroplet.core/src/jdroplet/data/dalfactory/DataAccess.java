/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jdroplet.data.dalfactory;

import java.lang.reflect.Constructor;

import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.core.SystemConfig;
import jdroplet.core.HttpRuntime;
import jdroplet.data.idal.*;

import org.apache.log4j.Logger;


/**
 * @author Texel
 */
public abstract class DataAccess {
    protected static Logger logger = Logger.getLogger(DataAccess.class);

    protected DataAccess() {
    }

    public static DataAccess instance() {
        String clazz = SystemConfig.getDataAccessClass();
        DataAccess da = (DataAccess) HttpRuntime.cache().get(clazz);
        if (da == null) {
            try {
                Class<?> provider = Class.forName(clazz);
                Constructor<?> constructor = provider.getConstructor();

                da = (DataAccess) constructor.newInstance();
            } catch (Exception ex) {
                logger.error(ex);
            }
            HttpRuntime.cache().add(clazz, da, ICache.HOUR_FACTOR);
        }
        return da;
    }

    public Object createProvider(String name) {
        return createProvider(SystemConfig.getDataDal(), name);
    }

    public Object createProvider(String path, String name) {
        Object instance = null;
        String clazz = null;

        clazz = path + "." + name;
        instance = AppCache.get(clazz);
        if (instance == null) {
            try {
                Class<?> provider = Class.forName(clazz);
                Constructor<?> constructor = provider.getConstructor();

                instance = constructor.newInstance();
            } catch (Exception ex) {
                logger.error(ex);
            }
            AppCache.add(clazz, instance);
        }

        return instance;
    }

    public IOrderDataProvider getOrderDataProvider() {
        return (IOrderDataProvider) createProvider(SystemConfig.getDataDal(), "OrderDataProvider");
    }

    public IContactDataProvider getContactDataProvider() {
        return (IContactDataProvider) createProvider(SystemConfig.getDataDal(), "ContactDataProvider");
    }

    public ISectionDataProvider getSectionProvider() {
        return (ISectionDataProvider) createProvider(SystemConfig.getDataDal(), "SectionDataProvider");
    }

    public IUserDataProvider getUserDataProvider() {
        return (IUserDataProvider) createProvider(SystemConfig.getDataDal(), "UserDataProvider");
    }

    public IRoleDataProvider getRoleDataProvider() {
        return (IRoleDataProvider) createProvider(SystemConfig.getDataDal(), "RoleDataProvider");
    }

    public ISectionDataProvider getSectionDataProvider() {
        return (ISectionDataProvider) createProvider(SystemConfig.getDataDal(), "SectionDataProvider");
    }

    public ISiteManageDataProvider getSiteManageDataProvider() {
        return (ISiteManageDataProvider) createProvider(SystemConfig.getDataDal(), "SiteManageDataProvider");
    }

    public IGroupDataProvider getGroupDataProvider() {
        return (IGroupDataProvider) createProvider(SystemConfig.getDataDal(), "GroupDataProvider");
    }

    public IGroupMemberDataProvider getGroupMemberDataProvider() {
        return (IGroupMemberDataProvider) createProvider(SystemConfig.getDataDal(), "GroupMemberDataProvider");
    }

    public IPermissionDataProvider getPermissionDataProvider() {
        return (IPermissionDataProvider) createProvider(SystemConfig.getDataDal(), "SectionPermissionDataProvider");
    }

    public IPostDataProvider getPostDataProvider() {
        return (IPostDataProvider) createProvider(SystemConfig.getDataDal(), "PostDataProvider");
    }

    public ISearchDataProvider getSearchDataProvider() {
        return (ISearchDataProvider) createProvider(SystemConfig.getDataDal(), "SearchDataProvider");
    }

    public IAttachmentDataProvider getAttachmentDataProvider() {
        return (IAttachmentDataProvider) createProvider(SystemConfig.getDataDal(), "AttachmentDataProvider");
    }

    public IMetaDataProvider getMetaDataProvider() {
        return (IMetaDataProvider) createProvider(SystemConfig.getDataDal(), "MetaDataProvider");
    }

    public IMethodDataProvider getMethodDataProvider() {
        return (IMethodDataProvider) createProvider(SystemConfig.getDataDal(), "MethodDataProvider");
    }

    public IWorkDataProvider getWorkDataProvider() {
        return (IWorkDataProvider) createProvider(SystemConfig.getDataDal(), "WorkDataProvider");
    }

    public ISkuDataProvider getSkuDataProvider() {
        return (ISkuDataProvider) createProvider(SystemConfig.getDataDal(), "SkuDataProvider");
    }

    public IShopDataProvider getShopDataProvider() {
        return (IShopDataProvider) createProvider(SystemConfig.getDataDal(), "ShopDataProvider");
    }

    public ICronDataProvider getCronDataProvider() {
        return (ICronDataProvider) createProvider(SystemConfig.getDataDal(), "CronDataProvider");
    }

    public ICouponDataProvider getCouponDataProvider() {
        return (ICouponDataProvider) createProvider(SystemConfig.getDataDal(), "CouponDataProvider");
    }

    public IMessageDataProvider getMessageDataProvider() {
        return (IMessageDataProvider) createProvider(SystemConfig.getDataDal(), "MessageDataProvider");
    }

    public IMessagesRecipientDataProvider getMessagesRecipientDataProvider() {
        return (IMessagesRecipientDataProvider) createProvider(SystemConfig.getDataDal(), "MessagesRecipientDataProvider");
    }

    public IVoteDataProvider getVoteDataProvider() {
        return (IVoteDataProvider) createProvider(SystemConfig.getDataDal(), "VoteDataProvider");
    }

    public ILogDataProvider getLogDataProvider() {
        return (ILogDataProvider) createProvider(SystemConfig.getDataDal(), "LogDataProvider");
    }

    public IStatisticDataProvider getStatisticDataProvider() {
        return (IStatisticDataProvider) createProvider(SystemConfig.getDataDal(), "StatisticDataProvider");
    }

    public ILotteryDataProvider getLotteryDataProvider() {
        return (ILotteryDataProvider) createProvider(SystemConfig.getDataDal(), "LotteryDataProvider");
    }

    public ILotteryUserDataProvider getLotteryUserDataProvider() {
        return (ILotteryUserDataProvider) createProvider(SystemConfig.getDataDal(), "LotteryUserDataProvider");
    }

    public IActivityUserDataProvider getActivityUserDataProvider() {
        return (IActivityUserDataProvider) createProvider(SystemConfig.getDataDal(), "ActivityUserDataProvider");
    }

    public IGoodsDataProvider getGoodsDataProvider() {
        return (IGoodsDataProvider) createProvider(SystemConfig.getDataDal(), "GoodsDataProvider");
    }

    public IGoodsSpecDataProvider getGoodsSpecDataProvider() {
        return (IGoodsSpecDataProvider) createProvider(SystemConfig.getDataDal(), "GoodsSpecDataProvider");
    }

    public ICheckInDataProvider getCheckInDataProvider() {
        return (ICheckInDataProvider) createProvider(SystemConfig.getDataDal(), "CheckInDataProvider");
    }

    public IVisitDataProvider getVisitDataProvider() {
        return (IVisitDataProvider) createProvider(SystemConfig.getDataDal(), "VisitDataProvider");
    }

    public IAttributeDataProvider getAttributeDataProvider() {
        return (IAttributeDataProvider) createProvider(SystemConfig.getDataDal(), "AttributeDataProvider");
    }

    public IAttributeValueDataProvider getAttributeValueDataProvider() {
        return (IAttributeValueDataProvider) createProvider(SystemConfig.getDataDal(), "AttributeValueDataProvider");
    }
}
