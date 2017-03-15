package tool.mastery.ioc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import tool.mastery.core.ReadXmlUtils;


public class ClassPathXmlApplicationContext implements BeanFactory {

	private Map<String, Object> beans = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public ClassPathXmlApplicationContext() throws Exception {
		// 获得对应xml的根节点
		Element rootElement = ReadXmlUtils
				.getRootElementByPath(SpringConstantStore.CLASS_PATH_XML);
		List list = rootElement.elements();
		for (int i = 0; i < list.size(); i++) {
			Element element = (Element) list.get(i);
			String id = element.attributeValue("id");
			String clazz = element.attributeValue("class");
			System.out.println(id + " and " + clazz);
			Object obj = Class.forName(clazz).newInstance();
			beans.put(id, obj);

			List<Element> propertyElements = (List<Element>) element.elements();
			for (Element propertyElement : propertyElements) {
				String name = propertyElement.attributeValue("name");
				String value = propertyElement.attributeValue("value");
				// 获得对应的对象
				Object object = beans.get(value);
				Method setMethod = obj.getClass().getMethod(
						"set" + name.substring(0, 1).toUpperCase()
								+ name.substring(1, name.length()) , object.getClass().getInterfaces()[0]);
				setMethod.invoke(obj, object);
			}
		}
	}

	@Override
	public Object getBean(String name) {
		// TODO Auto-generated method stub
		return beans.get(name);
	}

}
