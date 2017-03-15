package tool.mastery.core;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXmlUtils {

	// dom4j中读取xml文件的类
	private static SAXReader saxReader;

	static {
		saxReader = new SAXReader();
	}

	private ReadXmlUtils() {
	}

	/**
	 * 通过路径来获取根节点
	 * 
	 * @param path
	 * @return
	 */
	public static Element getRootElementByPath(String path) {
		Element rootElement = null;
		Document document = getDocument(path);
		rootElement = document.getRootElement();
		return rootElement;
	}

	/**
	 * 通过InputStream流来获取根节点
	 * 
	 * @param path
	 * @return
	 */
	public static Element getRootElementByPath(InputStream is) {
		Element rootElement = null;
		Document document = getDocument(is);
		rootElement = document.getRootElement();
		return rootElement;
	}

	/**
	 * 根据路径来返回xml文档的文档对象(document)
	 * 
	 * @param path
	 * @return
	 */
	private static Document getDocument(String path) {
		InputStream is = ReadXmlUtils.class.getClassLoader()
				.getResourceAsStream(path);
		return getDocument(is);
	}

	/**
	 * 根据路径来返回xml文档的文档对象(document)
	 * 
	 * @param path
	 * @return
	 */
	private static Document getDocument(InputStream is) {
		Document document = null;

		try {
			document = saxReader.read(is);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return document;
	}
}
