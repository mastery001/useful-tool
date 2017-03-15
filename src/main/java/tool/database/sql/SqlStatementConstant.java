package tool.database.sql;

public class SqlStatementConstant {

	public static final String CREATE_DATABASE = "create database";

	public static final String CREATE_TABLE = "create table";

	public static final String NOT_NULL = "not null";

	public static final String DEFAULT = "default";

	public static final String ADD = "add";

	public static final String ALTER_TABLE = "alter table";

	public static final String PRIMARY = "primary";

	public static final String PRIMARY_KEY = "primary key";

	public static final String AUTO_INCREMENT = "auto_increment";

	public static final String CONSTRAINT = "constraint";

	public static final String FOREIGN_KEY = "foreign key";

	public static final String REFERENCES = "references";

	public static final String FOREIGN_CONSTAINT = "ON DELETE CASCADE ON UPDATE CASCADE";

	public static final Integer DEFAULT_LENGTH = 20;

	/**
	 * 以下是sql的数据类型
	 */
	public static final String BIT = "bit";

	public static final String BINARY = "binary";

	public static final String VARBINARY = "varbinary ";

	public static final String INT = "int";

	public static final String TINYINT = "tinyint";

	public static final String MONEY = "money";

	public static final String TEXT = "text";

	public static final String IMAGE = "image";

	public static final String BYTE = "byte";

	public static final String DOUBLE = "double";
	/**
	 * 表示的数字可以达到38位,存储数据时所用的字节数目会随着使用权用位数的多少变化.
	 */
	public static final String NUMERIC = "numeric";
	/**
	 * 用8个字节来存储数据.最多可为53位.范围为:-1.79E+308至1.79E+308.
	 */
	public static final String FLOAT = "float";
	/**
	 * 位数为24,用4个字节,数字范围:-3.04E+38至3.04E+38
	 */
	public static final String REAL = "real";

	public static final String DECIMAL = "decimal";

	public static final String DATE = "date";

	public static final String TIMESTAMP = "timestamp";
	/**
	 * 表示时间范围可以表示从1753/1/1至9999/12/31,时间可以表示到3.33/1000秒.使用8个字节.
	 */
	public static final String DATETIME = "datetime";

	public static final String CHAR = "char";

	public static final String VARCHAR = "varchar";

}
