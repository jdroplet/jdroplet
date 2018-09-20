package jdroplet.factorys;

public class SkinFactory {
//	private Properties props;
//	private static SkinFactory fatory;
//	
//	private SkinFactory() {
//		FileInputStream input = null;
//		Configuration config = Configuration.config();
//		this.props = new Properties();
//		try {
//			input = new FileInputStream(new StringBuilder(64)
//										.append(config.getServerPath())
//										.append("/")
//										.append(config.getPagesSkinFile())
//										.toString());
//			this.props.load(input);
//		} catch (IOException e) {
//		} finally {
//			try { input.close(); } catch (Exception ex) {}
//		}
//	}
//	
//	public String getSkin(String name) {
//		return this.props.getProperty(name);
//	}
//	
//	public static SkinFactory getFatory() {
//		fatory = (SkinFactory)HttpRuntime.cache().get("irock_skin_factory");
//		
//		if (fatory == null) {
//			fatory = new SkinFactory();
//			HttpRuntime.cache().add("irock_skin_factory", fatory);
//		}
//		return fatory;
//	}
}
