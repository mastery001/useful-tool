package tool.database.sql.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import tool.database.sql.AttributeAssembly;
import tool.database.sql.ConstraintBuilder;
import tool.database.sql.util.TypeUtil;
import tool.database.sql.xml.support.ColumnAttribute;
import tool.database.sql.xml.support.ObjectMapping;

public class AttributeAssemblyByXml implements AttributeAssembly {

	private List<Element> columnElements;
	private Class<?> entityClass;

	public AttributeAssemblyByXml(Class<?> entityClass,
			List<Element> columnElements) {
		this.entityClass = entityClass;
		this.columnElements = columnElements;
	}

	public List<Element> getColumnElements() {
		return columnElements;
	}

	public void setColumnElements(List<Element> columnElements) {
		this.columnElements = columnElements;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public String assemblyAttribute() throws SecurityException,
			NoSuchFieldException {
		// 用于拼装sql语句
		StringBuilder sqlBuilder = new StringBuilder();
		// 存储主键
		List<String> primaryKey = new ArrayList<String>();
		// 存储外键
		Map<String, String> foreignKeyConstraint = new HashMap<String, String>();
		List<ColumnAttribute> objMapping = null;
		try {
			objMapping = ObjectMapping.buildObjectMapping(entityClass,
					columnElements);
		} catch (SecurityException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			throw e;
		}
		for (int i = 0; i < objMapping.size(); i++) {
			ColumnAttribute columnAttribute = objMapping.get(i);
			sqlBuilder.append(columnAttribute.getColumn() + " "
					+ columnAttribute.getType());
			if (TypeUtil.isNeedLength(columnAttribute.getType())) {
				sqlBuilder.append("(" + columnAttribute.getLength() + ")");
			}
			sqlBuilder.append(" ");
			// 如果设置了默认为空，则添加
			if (columnAttribute.isNot_null()) {
				sqlBuilder.append("not null ");
			}
			// 如果设置了默认值，则构建
			if (columnAttribute.getDefaultValue() != null) {
				sqlBuilder.append("default('"
						+ columnAttribute.getDefaultValue() + "') ");
			}
			// 判断是否是主键
			if (columnAttribute.isPrimary_key()) {
				primaryKey.add(columnAttribute.getColumn());
				sqlBuilder.append("primary key");
				if (columnAttribute.isAuto_increment()) {
					sqlBuilder.append(" auto_increment,");
				}
			}
			sqlBuilder.append(",\r\n");

			// 如果是外键，此字段应该有值
			if (columnAttribute.getForeign_key() != null) {
				foreignKeyConstraint.put(columnAttribute.getColumn(),
						columnAttribute.getForeign_key());
			}
		}
		// 构建主键约束
		ConstraintBuilder.buildPrimaryConstraint(sqlBuilder, primaryKey);
		// 构建外键约束
		sqlBuilder.append(ConstraintBuilder
				.buildForeignConstraint(foreignKeyConstraint));
		return sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).toString();
	}
}
