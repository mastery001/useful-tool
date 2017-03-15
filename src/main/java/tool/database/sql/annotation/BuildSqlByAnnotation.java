package tool.database.sql.annotation;

import java.lang.reflect.Field;

import com.JavaPersistence.util.AnnotationUtil;

import tool.database.sql.AssemblyException;
import tool.database.sql.AttributeAssembly;
import tool.database.sql.SqlBuilder;

public class BuildSqlByAnnotation extends SqlBuilder {

	// 必须需要配置了相应注解的实体类才能创建sql语句
	private Class<?> entityClass;

	public BuildSqlByAnnotation() {
		super();
	}

	public BuildSqlByAnnotation(Class<?> entityClass) {
		super(entityClass);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.setEntity(entityClass);
	}

	@Override
	protected String buildAttribute() throws Exception {
		// TODO Auto-generated method stub
		// 获取此类的所有字段
		Field[] fields = entityClass.getDeclaredFields();
		String sql = "";
		AttributeAssembly attributeAssembly = new AttributeAssemblyByAnnotation(
				fields);
		sql = attributeAssembly.assemblyAttribute();
		return sql;
	}

	@Override
	protected String buildConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildTableName() throws NullPointerException {
		// TODO Auto-generated method stub
		AnnotationUtil aUtil = new AnnotationUtil();
		// 初始化StringBuilder后添加创建sql语句的开端，create table xxx
		String tableName = aUtil.getAnnotationTableName(entityClass);
		if (tableName == null) {
			throw new NullPointerException("此类尚未配置TableAnnotation注解！请配置后再使用！");
		}
		return tableName;
	}

	@Override
	public String construct(Object entity) throws AssemblyException {
		return super.construct(entity);
	}


	@Override
	protected void convertEntityToNeedTypeClass() {
		if (entity instanceof Class<?>) {
			this.entityClass = (Class<?>) entity;
		}
	}

}
