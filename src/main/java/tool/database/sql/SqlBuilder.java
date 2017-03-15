package tool.database.sql;

public abstract class SqlBuilder {

	/**
	 * 负责组装sql语句
	 */
	protected StringBuilder sqlBuilder;

	protected Object entity;

	public SqlBuilder() {
		this(null);
	}
	
	public SqlBuilder(Object entity) {
		setEntity(entity);
	}

	/**
	 * 设置构建时的实体
	 * @param entity
	 */
	protected void setEntity(Object entity) {
		this.entity = entity;
		convertEntityToNeedTypeClass();
	}
	
	/**
	 * 转换实体至需要的类型
	 */
	protected abstract void convertEntityToNeedTypeClass();
	
	/**
	 * 判断实体是否存在
	 * @return
	 */
	private boolean isExistEntity() {
		if(entity == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 只负责返回需要的表名
	 */
	protected abstract String buildTableName() throws NullPointerException;

	/**
	 * 只负责实例化attributeAssembly的对象，用于构建添加上括号内部的属性（字段）
	 */
	protected abstract String buildAttribute() throws Exception;

	/**
	 * 添加表后的约束，需要使用alter table ....
	 */
	protected abstract String buildConstraint();

	public String construct(Object entity) throws AssemblyException{
		//设置实体
		setEntity(entity);
		// 初始化StringBuilder后添加创建sql语句的开端，create table xxx
		sqlBuilder = new StringBuilder(SqlStatementConstant.CREATE_TABLE + " ");
		
		// 创建表
		String tableName = this.buildTableName();
		if(tableName == null) {
			throw new AssemblyException("尚未生成数据库中字段的创建表的语句，请检查生成sql语句的类！");
		}
		sqlBuilder.append(tableName + " ( " + "\r\n");
		
		// 创建属性
		String attributeSql = null;
		try {
			attributeSql = this.buildAttribute();
		} catch (Exception e) {
			AssemblyException ae = new AssemblyException(e.getMessage());
			ae.initCause(e);
			throw ae;
		}
		if(attributeSql == null) {
			throw new AssemblyException("尚未生成数据库中字段的创建语句，请检查生成sql语句的类！");
		}
		sqlBuilder.append(attributeSql + ");");
		
		// 创建约束
		String constraintSql = this.buildConstraint();
		if(constraintSql != null) {
			sqlBuilder.append(constraintSql);
		}
		return sqlBuilder.toString();
	}
	
	public String construct() throws AssemblyException{
		if(!isExistEntity()) {
			throw new AssemblyException("尚未配置需要构建sql的实体类，请重新配置！");
		}
		return construct(entity);
	}
}
