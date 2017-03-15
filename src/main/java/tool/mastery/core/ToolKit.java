package tool.mastery.core;

import java.io.InputStream;
import java.net.URL;

public class ToolKit {
	private static final ClassLoader CLASS_LOADER = ToolKit.class.getClassLoader();
	
	public static InputStream getResourceAsStream(String name) {
		return CLASS_LOADER.getResourceAsStream(name);
	}
	
	 public static URL getResource(String name) {
		 return CLASS_LOADER.getResource(name);
	 }
}
