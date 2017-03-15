package tool.database.sql.util;

import tool.database.sql.SqlStatementConstant;

public class TypeUtil {

	/**
	 * 通过属性的类型判断数据库对应的类型
	 * 
	 * @param typeName
	 * @return
	 */
	public static String getSqlType(String typeName) {
		String sqlType = "";
		typeName = typeName.toLowerCase();
		if (typeName.indexOf("int") != -1 || typeName.indexOf("integer") != -1
				|| typeName.indexOf("long") != -1) {
			sqlType = SqlStatementConstant.INT;
		} else if (typeName.indexOf("float") != -1
				|| typeName.indexOf("double") != -1) {
			sqlType = SqlStatementConstant.DECIMAL + "(6,2)";
		} else if (typeName.indexOf("date") != -1) {
			sqlType = SqlStatementConstant.DATE;
		} else {
			sqlType = SqlStatementConstant.VARCHAR;
		}
		return sqlType;
	}

	/**
	 * 判断数据库的类型是否需要长度
	 * 
	 * @return
	 */
	public static boolean isNeedLength(String sqlType) {
		if (SqlStatementConstant.DATE.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.DECIMAL.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.TIMESTAMP.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.DATETIME.equalsIgnoreCase(sqlType)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否是sql中的数据类型
	 * @param sqlType
	 * @return
	 */
	public static boolean isSqlType(String sqlType) {
		if (SqlStatementConstant.DATE.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.DECIMAL.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.TIMESTAMP.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.DATETIME.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.INT.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.BINARY.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.BIT.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.TINYINT.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.TEXT.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.NUMERIC.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.BYTE.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.CHAR.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.VARBINARY.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.VARCHAR.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.REAL.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.FLOAT.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.DOUBLE.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.IMAGE.equalsIgnoreCase(sqlType)
				|| SqlStatementConstant.MONEY.equalsIgnoreCase(sqlType)) {
			return true;
		}
		return false;
	}
}
