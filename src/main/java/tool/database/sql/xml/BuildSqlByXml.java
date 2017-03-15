package tool.database.sql.xml;

import java.util.List;

import org.dom4j.Element;

import tool.database.sql.AssemblyException;
import tool.database.sql.AttributeAssembly;
import tool.database.sql.SqlBuilder;
import tool.mastery.core.ClassUtil;

public class BuildSqlByXml extends SqlBuilder {

	// 获取对应实体的元素
	private Element entityElement;

	public BuildSqlByXml() {
	}

	public BuildSqlByXml(Element entityElement) {
		super(entityElement);
	}

	public Element getEntityElement() {
		return entityElement;
	}

	public void setEntityElement(Element entityElement) {
		this.setEntity(entityElement);
	}

	@Override
	protected String buildAttribute() throws Exception {
		// TODO Auto-generated method stub
		List<Element> columnElements = (List<Element>) entityElement.elements();
		Class<?> entityClass = null;
		try {
			entityClass = ClassUtil.getClassByName(this.entityElement
					.attributeValue("name"));
		} catch (ClassNotFoundException e) {
			throw new AssemblyException("此类不存在，请检查配置的"
					+ this.entityElement.attributeValue("name") + "是否正确");
		}
		String sql = "";
		AttributeAssembly attributeAssembly = new AttributeAssemblyByXml(
				entityClass, columnElements);
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
		// 获取表名
		String tableName = this.entityElement.attributeValue("table");
		if (tableName == null || "".equals(tableName)) {
			// 获取类名
			String className = this.entityElement.attributeValue("name");
			throw new NullPointerException("xml配置中的" + className
					+ "该类的table未配置或者配置为‘’");
		}
		return tableName;
	}

	@Override
	public String construct(Object entity) throws AssemblyException {
		// TODO Auto-generated method stub
		return super.construct(entity);
	}

	@Override
	protected void convertEntityToNeedTypeClass() {
		if (entity instanceof Element) {
			this.entityElement = (Element) entity;
		}
		
	}
}
