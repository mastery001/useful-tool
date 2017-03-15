package tool.database.sql.xml.support;

public class ColumnAttribute {

	/**
	 * 列名
	 */
	private String column;
	/**
	 * 类型
	 */
	private String type;

	/**
	 * 长度
	 */
	private Integer length;
	/**
	 * 是否不能为空
	 */
	private boolean not_null;
	/**
	 * 默认值
	 */
	private String defaultValue;
	/**
	 * 是否为主键
	 */
	private boolean primary_key;
	/**
	 * 将外键信息保存此当中，对应是表名
	 */
	private String foreign_key;
	
	/**
	 * 是否自动增长
	 */
	private boolean auto_increment;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNot_null() {
		return not_null;
	}

	public void setNot_null(boolean notNull) {
		not_null = notNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(boolean primaryKey) {
		primary_key = primaryKey;
	}

	public String getForeign_key() {
		return foreign_key;
	}

	public void setForeign_key(String foreignKey) {
		foreign_key = foreignKey;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public boolean isAuto_increment() {
		return auto_increment;
	}

	public void setAuto_increment(boolean auto_increment) {
		this.auto_increment = auto_increment;
	}

}
