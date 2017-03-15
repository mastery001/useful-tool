package tool.database.sql.xml.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import tool.database.sql.AssemblyException;
import tool.database.sql.SqlStatementConstant;
import tool.database.sql.util.TypeUtil;

/**
 * @author mastery
 * @Time 2014-12-18 下午10:03:51
 * 
 */
public class ObjectMapping {

	/**
	 * 根据xml中的class中的配置构建对象与数据库的关系模型
	 * 
	 * @param entityClass
	 * @param columnElements
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static List<ColumnAttribute> buildObjectMapping(
			Class<?> entityClass, List<Element> columnElements)
			throws SecurityException, NoSuchFieldException {
		List<ColumnAttribute> objMapping = new ArrayList<ColumnAttribute>();
		for (int i = 0; i < columnElements.size(); i++) {
			Element columnElement = columnElements.get(i);
			ColumnAttribute columnAttribute = buildObjectMapping(entityClass,
					columnElement);
			objMapping.add(columnAttribute);
		}
		return objMapping;
	}

	/**
	 * 根据xml中的class中的配置构建对象与数据库的关系模型
	 * 
	 * @param entityClass
	 * @param columnElement
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static ColumnAttribute buildObjectMapping(Class<?> entityClass,
			Element columnElement) throws SecurityException,
			NoSuchFieldException, AssemblyException {
		// 获取配置中的name属性
		String name = columnElement.attributeValue("name");
		Field filed = null;
		try {
			// 判断是否有此属性
			filed = entityClass.getDeclaredField(name);
		} catch (SecurityException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			throw new NoSuchFieldException(e.getMessage() + "配置的name属性" + name
					+ "在类" + entityClass.getName() + "中不存在");
		}
		ColumnAttribute columnAttribute = new ColumnAttribute();
		// 获取元素名称
		String elementName = columnElement.getName();
		// 如果是id的话则认为此键为主键
		if ("id".equalsIgnoreCase(elementName)) {
			columnAttribute.setPrimary_key(true);
			// 是主键则存在是否有自动增长
			String auto_increment = columnElement.attributeValue("auto_increment");
			boolean booelanAuto_increment = false;
			if(auto_increment != null && auto_increment.equalsIgnoreCase("true")) {
				booelanAuto_increment = true;
			}
			columnAttribute.setAuto_increment(booelanAuto_increment);
		}
		// 处理列
		String column = columnElement.attributeValue("column");
		// 如果配置未配置列则默认为对象对应的name属性
		if (column == null || column.trim().equals("")) {
			column = name;
		}
		columnAttribute.setColumn(column);

		// 处理长度
		String length = columnElement.attributeValue("length");
		Integer intLength = 0;
		if (length == null) {
			intLength = SqlStatementConstant.DEFAULT_LENGTH;
		}
		try {
			intLength = Integer.parseInt(length);
		} catch (NumberFormatException e) {
		}
		columnAttribute.setLength(intLength);

		// 处理类型
		String type = columnElement.attributeValue("type");
		if (type == null || type.trim().equals("")) {
			type = TypeUtil.getSqlType(filed.getType().getName());
		}
		// 如果不是sql的数据类型
		if (!TypeUtil.isSqlType(type)) {
			throw new AssemblyException("属性" + name + "对应配置的type类型" + type + "不是数据库中的正确的数据类型，请重新配置！");
		}
		columnAttribute.setType(type);

		// 处理not-null
		String not_null = columnElement.attributeValue("not-null");
		boolean booleanValue = false;
		if (not_null != null && not_null.trim().equalsIgnoreCase("true")) {
			booleanValue = true;
		}
		// 默认为可以不为空
		columnAttribute.setNot_null(booleanValue);

		// 处理默认值
		String defaultValue = columnElement.attributeValue("default");
		columnAttribute.setDefaultValue(defaultValue);

		// 处理外键
		String foreign_key = columnElement.attributeValue("foreign-key");
		columnAttribute.setForeign_key(foreign_key);

		return columnAttribute;
	}
}
