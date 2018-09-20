package jdroplet.bll;

import java.awt.image.BufferedImage;
import java.io.File;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.*;

import jdroplet.cache.AppCache;
import jdroplet.core.SystemConfig;
import jdroplet.core.DateTime;
import jdroplet.core.fileupload.disk.HttpPostedFile;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IAttachmentDataProvider;
import jdroplet.data.idal.IAttachmentDataProvider;
import jdroplet.data.model.*;
import jdroplet.enums.SortOrder;
import jdroplet.exceptions.ApplicationException;
import jdroplet.plugin.PluginFactory;
import jdroplet.security.DigestUtil;
import jdroplet.util.*;

import org.apache.log4j.Logger;

public class Attachments {
	private static Logger logger = Logger.getLogger(Attachments.class);
	public static String GROUP_ATTAS = "ATTAS_GROUP_U-{0}";
	public static String KEY_ATTAS = "ATTAS_u-{0}_s-{1}_p-{2}";
	public static String KEY_ATTAS_TIME = "ATTAS_u-{0}_s-{1}_t-{2}";

	public static List<Attachment> addAttachment(Integer clusterId, Integer itemId, Integer userId, String folder, List<HttpPostedFile> files) {
		Cluster cluster = SiteManager.getCluster(clusterId);

		List<Attachment> attas = new ArrayList<Attachment>();
		if (!cluster.enableAttachments()) {
			return attas;
		}

		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(System.currentTimeMillis());
		c.get(Calendar.YEAR);

		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		String subPath = null;
		String sysPath = null;
		String filePath = null;

		subPath = new StringBuilder(32).append(year).append('/').append(month).append('/').toString();
		sysPath = SystemConfig.getAttachmentsStorePath();
		sysPath = (String) PluginFactory.getInstance().applyFilters("Attachments@get" + folder + "SavePath", sysPath);
		subPath = (String) PluginFactory.getInstance().applyFilters("Attachments@get" + folder + "SubPath", subPath);

		filePath = SystemConfig.getServerPath() + sysPath + subPath;
		new File(filePath).mkdirs();

		for (HttpPostedFile file : files) {
			if ((file.getSize() > cluster.getMaxAttachmentSize() * 1024) && cluster.getMaxAttachmentSize() > 0 ) {
				throw new ApplicationException("FileOutOfSize " +  file.getSize() + "b");
			} else if (!validateAttachmentType(file.getExtension())) {
				throw new ApplicationException("UnAllowFileType " + file.getExtension());
			} else {
				Attachment atta = new Attachment();
				String saveFileName = null;
				String filename = null;
				String rawName = null;
				Integer attaId = null;

				rawName = stripPath(file.getName());
				atta.setSize(BigInteger.valueOf(file.getSize()));
				atta.setContentType(file.getContentType());
				atta.setCreateTime(new Date());
				atta.setFilename((String) PluginFactory.getInstance().applyFilters("Attachments@get" + folder + "Filename", rawName));
				atta.setExtension(file.getExtension());
				atta.setFolder(folder);

				filename = (String) PluginFactory.getInstance().applyFilters("Attachments@get" + folder + "DiskFilename",
						getDiskFilename(atta), folder, atta);

				saveFileName = filePath + filename;
				file.saveAs(saveFileName);

				atta.setDiskFilename(subPath + filename);
				atta.setShopId(1);
				atta.setItemId(itemId);
				atta.setUserId(userId);
				attaId = add(atta);
				atta.setId(attaId);
				if (shouldCreateThumb(atta)) {
					if (PluginFactory.getInstance().hasActions("Attachments@create" + folder + "Thumb")) {
						PluginFactory.getInstance().doAction("Attachments@create" + folder + "Thumb", sysPath, subPath, filename);
					} else {
						createThumb(sysPath, subPath, filename);
					}

				}
				PluginFactory.getInstance().doAction("Attachments@" + folder + "Saved", atta, itemId);
				attas.add(atta);
			}
		}
		String group_key = MessageFormat.format(GROUP_ATTAS, userId);
		AppCache.clear(group_key);

		return attas;
	}

	public static Integer add(Attachment att) {
		IAttachmentDataProvider provider = DataAccess.instance().getAttachmentDataProvider();
		return provider.save(att);
	}

	public static List<Attachment> addSiteRes(Site site, List<HttpPostedFile> files) {
		return addSiteRes(site, files, false);
	}

	public static List<Attachment> addSiteRes(Site site, List<HttpPostedFile> files, boolean autoName) {
		String savePath = null;
		String name = null;
		List<Attachment> attas = new ArrayList<Attachment>();
		Attachment att =null;

		for (HttpPostedFile file : files) {
			if(validateAttachmentType(file.getExtension())) {
				name = Attachments.getSiteResDiskFilename(site, file, autoName);

				savePath = SystemConfig.getAppPath() + name;
				file.saveAs(savePath);

				att = new Attachment();
				att.setDiskFilename(name);
				att.setFilename(file.getName());
				attas.add(att);
			}
		}
		return attas;
	}

	private static String stripPath(String realName) {
		String separator = "/";
		int index = realName.lastIndexOf(separator);

		if (index == -1) {
			separator = "\\";
			index = realName.lastIndexOf(separator);
		}

		if (index > -1) {
			realName = realName.substring(index + 1);
		}

		return realName;
	}

	private static String getSiteResDiskFilename(Site site, HttpPostedFile file, boolean autoName) {
		StringBuffer dir = new StringBuffer(256);
		String name = null;
		boolean success = false;

		dir.append(SystemConfig.getTemplatePath()).append('/').append(site.getName()).append('/');
		File siteDir = new File(SystemConfig.getAppPath() + dir.toString());
		success = siteDir.exists();
		if (!success) {
			success = siteDir.mkdirs();
		}

		if (autoName) {
			String filename = DigestUtil.MD5(file.getName() + System.currentTimeMillis()) + "." + file.getExtension();
			name = dir.append(filename).toString();
		} else {
			name = dir.append(file.getName()).toString();
		}
		return name;
	}

	private static String getDiskFilename(Attachment att) {
		return getDiskFilename(att.getFilename(), att.getExtension());
	}

	private static String getDiskFilename(String filename, String extension) {
		return new StringBuilder(256)
				.append(DigestUtil.MD5(filename + System.currentTimeMillis()))
				.append('_')
				.append(Users.getCurrentUser().getId())
				.append('.')
				.append(extension)
				.toString();
	}

	private static boolean validateAttachmentType(String rawExtension) {
		String[] types = SiteManager.getCurrentSite().getCluster().getAllowedAttachmentTypes().split(";");

		rawExtension = rawExtension.toLowerCase();
		for(String type : types) {
			if(type.trim().toLowerCase().equals(rawExtension)) {
				return true;
			}
		}
		return false;
	}

	private void createMaskImage(Site site, String path) {
		BufferedImage srcImg = null;
		BufferedImage maskImg = null;
		Cluster cluster = null;
		String maskPath = null;
		StringBuilder dir = null;

		cluster = SiteManager.getCurrentSite().getCluster();
		dir = new StringBuilder(256);
		dir.append(SystemConfig.getAppPath()).append(SystemConfig.getTemplatePath()).append('/').append(site.getName()).append('/').append(cluster.getMaskImage());
		maskPath = dir.toString();
		try {
			srcImg = ImageUtils.getRAWImage(path);
			maskImg = ImageUtils.getRAWImage(maskPath);
			ImageUtils.createMaskImage(srcImg, maskImg);

			ImageUtils.saveImage(srcImg, ImageUtils.IMAGE_JPEG, path + "_mask");
		}
		catch (Exception ex) {
			logger.error("Exception", ex);
		}
	}

	private static boolean shouldCreateThumb(Attachment att) {
		String extension = att.getExtension().toLowerCase();

		return SystemConfig.enableCreateThumb()
				&& ("jpg".equals(extension) || "jpeg".equals(extension)
				|| "gif".equals(extension) || "png".equals(extension));
	}

	private static void createThumb(String path, String subPath, String diskFilename) {
		Cluster cluster = SiteManager.getCurrentSite().getCluster();
		String rawFileName = SystemConfig.getServerPath() + path + subPath + diskFilename;
		String thumbFilePath = SystemConfig.getServerPath() + path + "thumb/" + subPath;

		new File(thumbFilePath).mkdirs();
		try {
			BufferedImage image = ImageUtils.resizeImage(rawFileName, ImageUtils.IMAGE_JPEG,
					cluster.getImageThumbWidth(),
					cluster.getImageThumbHeight());
			ImageUtils.saveImage(image, ImageUtils.IMAGE_JPEG, thumbFilePath + diskFilename);
		} catch (Exception ex) {
			logger.error("createThumb Error:", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static DataSet<Attachment> getAttachments(Integer shopId, Integer userId, Integer itemId, Integer pageIndex, Integer pageSize) {
		DataSet<Attachment> datas = null;
		IAttachmentDataProvider provider = DataAccess.instance().getAttachmentDataProvider();
		SearchQuery query = null;
		SearchGroup group_root = null;

		group_root = new SearchGroup();
		group_root.setTerm(SearchGroup.AND);

		//========================
		group_root = new SearchGroup();
		group_root.setTerm(SearchGroup.AND);

		if (shopId != null) {
			query = new SearchQuery(Attachment.DataColumns.shopId, shopId, SearchQuery.EQ);
			group_root.addQuery(query);
		}

		if (userId != null) {
			query = new SearchQuery(Attachment.DataColumns.userId, userId, SearchQuery.EQ);
			group_root.addQuery(query);
		}

		if (itemId != null) {
			query = new SearchQuery(Attachment.DataColumns.itemId, itemId, SearchQuery.EQ);
			group_root.addQuery(query);
		}

		datas = (DataSet) provider.search(group_root, Attachment.DataColumns.id, SortOrder.DESC, pageIndex, pageSize);
		return datas;
	}

//	public static void delete(User owner, List<Integer> attids) {
//		List<Attachment> attas = null;
//		File file = null;
//		String path = null;
//		String group_key = null;
//		IAttachmentDataProvider provider = DataAccess.instance().getAttachmentDataProvider();
//
//		attas = getAttachments(attids);
//		for (Attachment atta:attas) {
//			path = SystemConfig.getAppPath() + atta.getDiskFilename();
//			file = new File(path);
//			file.delete();
//		}
//		provider.delete(attids);
//		group_key = MessageFormat.format(GROUP_ATTAS, owner.getId());
//		AppCache.clear(group_key);
//	}
}
