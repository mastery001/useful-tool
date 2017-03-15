package tool.mastery.file;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import tool.mastery.core.CharUtil;
import tool.mastery.core.ToolKit;

public class ClassFileHelper {

	/**
	 * 根据包名获取包下所有的.class文件,包名的规则如下
	 * @param packageName
	 * @return
	 */
	public static Set<String> getClassFileInPackage(String packageName) {
		Set<String> entitys = null;
		URL url = ToolKit.getResource("");
		System.out.println(url);

		String filePath = url.toString();

		if (packageName == null || "".equals(packageName)) {
			throw new NullPointerException("packageName路径不能为空！");
		} else {
			filePath += CharUtil.replacePointToSlash(packageName);
			// TODO System.out.println("文件路径(字符串)： " + filePath);
			File file = null;
			try {
				file = new File(new URI(filePath));
				// TODO System.out.println("文件路径是(文件类型)：" + file);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			if (file.exists()) {
				ClassScanHelper classHelper = new ClassScanHelper();
				entitys = classHelper.getAllEntity(file, packageName);
			}
		}
		return entitys;
	}
}
