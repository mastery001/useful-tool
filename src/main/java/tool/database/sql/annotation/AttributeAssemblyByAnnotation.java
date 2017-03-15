package tool.database.sql.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.JavaPersistence.util.AnnotationUtil;

import tool.database.sql.AssemblyException;
import tool.database.sql.AttributeAssembly;
import tool.database.sql.ConstraintBuilder;
import tool.database.sql.SqlStatementConstant;
import tool.database.sql.annotation.support.AnnotationAttribute;
import tool.database.sql.annotation.support.FieldAnnotationHelper;
import tool.database.sql.util.TypeUtil;
import tool.mastery.core.RandomChar;

/**
 * 属性的组装类
 * 
 * @author Administrator
 * 
 */
public class AttributeAssemblyByAnnotation implements AttributeAssembly {

	private Field[] fields;

	public AttributeAssemblyByAnnotation(Field[] fields) {
		this.fields = fields;
	}

	@Override
	public String assemblyAttribute() throws AssemblyException {
		StringBuilder sBuilder = new StringBuilder();
		// 存储主键
		List<String> primaryKey = new ArrayList<String>();
		// 存储外键
		Map<String, String> foreignKeyConstraint = new HashMap<String, String>();
		for (Field field : fields) {
			String fieldName = field.getName();
			// 如果是serialVersionUID这个属性则不进行拼装
			if ("serialVersionUID".equalsIgnoreCase(fieldName)) {
				continue;
			}
			// 获取该字段上的所有注解属性
			AnnotationAttribute annotationAttribute = FieldAnnotationHelper
					.getAnnotationAtttibute(field);
			String column = annotationAttribute.getColumn();
			if (column.equals("")) {
				column = fieldName;
			}
			// 获取类型名
			String typeName = field.getType().getName();
			// 如果此字段对应的键是外键
			if (annotationAttribute.getType() != null
					&& !annotationAttribute.getType().equals("")) {
				typeName = annotationAttribute.getType();
				// 获取对应属性字段的表名
				String tableName = new AnnotationUtil()
						.getAnnotationTableName(field.getType());
				foreignKeyConstraint.put(column, tableName);
			}
			sBuilder.append(column + " ");
			// 获取对应数据库的类型
			String sqlType = TypeUtil.getSqlType(typeName);
			sBuilder.append(sqlType);
			// 判断类型是否需要长度
			if (TypeUtil.isNeedLength(sqlType)) {
				sBuilder.append("(" + annotationAttribute.getLength() + ")");
			}
			// 如果存在主键,则拼接创建主键的格式
			if (annotationAttribute.getKey() != null
					&& annotationAttribute.getKey().equals(
							SqlStatementConstant.PRIMARY)) {
				primaryKey.add(column);
				sBuilder.append(" " + SqlStatementConstant.PRIMARY_KEY);
				if (annotationAttribute.isAuto_increment()) {
					sBuilder.append(" " + SqlStatementConstant.AUTO_INCREMENT);
				}
			}
			sBuilder.append(", " + "\r\n");
		}
		// 构建主键约束
		ConstraintBuilder.buildPrimaryConstraint(sBuilder,primaryKey);

		// 构建外键约束
		sBuilder.append(ConstraintBuilder
				.buildForeignConstraint(foreignKeyConstraint));

		return sBuilder.deleteCharAt(sBuilder.lastIndexOf(",")).toString();
	}

	/**
	 * 拼装属性的sql语句
	 * 
	 * @param entityClass
	 * @param sBuilder
	 * @return
	 */
	public static StringBuilder assemblyAttribute(Field[] fields,
			StringBuilder sBuilder) {
		// 存储主键
		List<String> primaryKey = new ArrayList<String>();
		// 存储外键
		Map<String, String> foreignKeyConstraint = new HashMap<String, String>();
		for (Field field : fields) {
			String fieldName = field.getName();
			// 如果是serialVersionUID这个属性则不进行拼装
			if ("serialVersionUID".equalsIgnoreCase(fieldName)) {
				continue;
			}
			// 获取该字段上的所有注解属性
			AnnotationAttribute annotationAttribute = FieldAnnotationHelper
					.getAnnotationAtttibute(field);
			String column = annotationAttribute.getColumn();
			if (column.equals("")) {
				column = fieldName;
			}
			// 获取类型名
			String typeName = field.getType().getName();
			// 如果此字段对应的键是外键
			if (annotationAttribute.getType() != null
					&& !annotationAttribute.getType().equals("")) {
				typeName = annotationAttribute.getType();
				// 获取对应属性字段的表名
				String tableName = new AnnotationUtil()
						.getAnnotationTableName(field.getType());
				foreignKeyConstraint.put(column, tableName);
			}
			sBuilder.append(column + " ");
			// 获取对应数据库的类型
			String sqlType = TypeUtil.getSqlType(typeName);
			sBuilder.append(sqlType);
			// 判断类型是否需要长度
			if (TypeUtil.isNeedLength(sqlType)) {
				sBuilder.append("(" + annotationAttribute.getLength() + ")");
			}
			// 如果存在主键,则拼接创建主键的格式
			if (annotationAttribute.getKey() != null
					&& annotationAttribute.getKey().equals(
							SqlStatementConstant.PRIMARY)) {
				primaryKey.add(column);
				sBuilder.append(" " + SqlStatementConstant.PRIMARY_KEY);
				if (annotationAttribute.isAuto_increment()) {
					sBuilder.append(" " + SqlStatementConstant.AUTO_INCREMENT);
				}
			}
			sBuilder.append(", " + "\r\n");
		}
		// 添加主键约束
		if (primaryKey.size() > 1) {
			sBuilder.append(SqlStatementConstant.CONSTRAINT + " pk_"
					+ primaryKey.get(0) + RandomChar.getRandomString(3) + " "
					+ SqlStatementConstant.PRIMARY_KEY + "(");
			// 如果是两个主键且配置了自动增长则抛出异常
			if (sBuilder.indexOf(SqlStatementConstant.AUTO_INCREMENT) != -1) {
				throw new AssemblyException(
						"两个主键或两个主键以上的不能配置自动增长！请重新配置后生成sql语句");
			}
			// 另一种处理方式删除掉自动增长
			/*
			 * sBuilder.delete(sBuilder.indexOf("auto_increment"),
			 * sBuilder.indexOf("auto_increment") + 14);
			 */
			for (int i = 0; i < primaryKey.size(); i++) {
				sBuilder.delete(
						sBuilder.indexOf(" " + SqlStatementConstant.PRIMARY),
						sBuilder.indexOf(" " + SqlStatementConstant.PRIMARY) + 12);
				sBuilder.append(primaryKey.get(i) + ",");
			}
			sBuilder.deleteCharAt(sBuilder.lastIndexOf(","));
			sBuilder.append(")," + "\r\n");
		}

		Set<String> set = foreignKeyConstraint.keySet();
		// 添加外键约束
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String attribute = it.next();
			sBuilder.append(SqlStatementConstant.CONSTRAINT + " fk_"
					+ RandomChar.getRandomString(6)
					+ RandomChar.getRandomString(2) + " "
					+ SqlStatementConstant.FOREIGN_KEY + "(" + attribute + ") "
					+ SqlStatementConstant.REFERENCES + " "
					+ foreignKeyConstraint.get(attribute) + "(" + attribute
					+ ") " + SqlStatementConstant.FOREIGN_CONSTAINT + ","
					+ "\r\n");
		}
		return sBuilder.deleteCharAt(sBuilder.lastIndexOf(","));
	}

}
