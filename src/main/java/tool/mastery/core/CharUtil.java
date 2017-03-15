package tool.mastery.core;

/**
 * 字符工具类
 * @author Administrator
 *
 */
public class CharUtil {

	/**
	 * 替代字符串方法,将字符串中所有的'.'替换成'/'
	 * 
	 * @param str
	 * @return str.replace('.','/');
	 */
	public static String replacePointToSlash(String str) {
		return str.replace('.', '/');
	}
}
