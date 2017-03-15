package tool.database.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tool.database.sql.annotation.AttributeAssemblyByAnnotation;
import tool.mastery.core.ClassUtil;
import tool.mastery.file.ClassFileHelper;

import com.JavaPersistence.util.AnnotationUtil;

public class SqlUtil {

	public static final int VARCHAR_MAX_SIZE = 200;

	public static String createSqlStatementByAnnotation(Class<?> entityClass) {
		AnnotationUtil aUtil = new AnnotationUtil();
		// 初始化StringBuilder后添加创建sql语句的开端，create table xxx
		String tableName = aUtil.getAnnotationTableName(entityClass);
		StringBuilder sBuilder = new StringBuilder(
				SqlStatementConstant.CREATE_TABLE);

		if (tableName == null) {
			throw new NullPointerException("此类尚未配置TableAnnotation注解！请配置后再使用！");
		}
		// 填充表名
		sBuilder.append(" " + tableName + " ( " + "\r\n");
		// 获取此类的所有字段
		Field[] fields = entityClass.getDeclaredFields();
		// 进行添加field后的组装好的StringBuilder
		sBuilder = AttributeAssemblyByAnnotation.assemblyAttribute(fields, sBuilder);
		sBuilder.append(");");
		return sBuilder.toString();
	}

	public static String createSqlStatement(String poSrcPath) {
		StringBuilder sBuilder = new StringBuilder();
		//得到该包对应的所有class文件
		Set<String> entitys = ClassFileHelper.getClassFileInPackage(poSrcPath);
		//保存多表的生成sql
		List<String> MutilTableSqlStatement = new ArrayList<String>();
		// 遍历所有的class类
		for (Iterator<String> it = entitys.iterator(); it.hasNext();) {
			String className = it.next();
			String sql = "";
			try {
				//创建对应po类生成的sql语句
				sql = createSqlStatementByAnnotation(ClassUtil
						.getClassByName(className));
			} catch (ClassNotFoundException e) {
				// 此异常永远不会产生
				e.printStackTrace();
			}
			// 如果存在需要创建外键时先不加入到sBuilder中
			if (sql.indexOf(SqlStatementConstant.FOREIGN_KEY) != -1) {
				MutilTableSqlStatement.add(sql);
				continue;
			}
			sBuilder.append(sql + "\r\n\r\n");
		}
		sortSql(sBuilder, MutilTableSqlStatement);
		return sBuilder.toString();
	}

	/**
	 * 使用递归来完成sql语句的排序
	 * 
	 * @param sBuilder
	 * @param MutilTableSqlStatement
	 */
	public static void sortSql(StringBuilder sBuilder,
			List<String> MutilTableSqlStatement) {
		List<String> surplusTable = new ArrayList<String>();
		for (int i = 0; i < MutilTableSqlStatement.size(); i++) {
			boolean flag = false;
			String sql = MutilTableSqlStatement.get(i);
			String[] splitSql = sql.split(",");
			for (int j = 0; j < splitSql.length; j++) {
				if (splitSql[j].indexOf(SqlStatementConstant.FOREIGN_KEY) != -1) {
					String table = splitSql[j].split(" ")[5];
					if (!table.startsWith("t_")) {
						table = splitSql[j].split(" ")[6];
					}
					// 获取外键的表名
					table = table.substring(0, table.indexOf("("));
					// 如果已经生成的sql语句不存在此张表则跳过
					if (sBuilder.indexOf(table) == -1) {
						boolean exist = false;
						// 再在多表中寻找是否存在此张表，如果不存在则抛出异常
						for (int k = 0; k < MutilTableSqlStatement.size(); k++) {
							String sql2 = MutilTableSqlStatement.get(k);
							// 如果存在这张表则继续，否则抛出异常
							if(sql != sql2 && sql2.indexOf(table) != -1) {
								exist = true;
								break;
							}
						}
						if(!exist) {
							throw new AssemblyException("创建失败，尚未配置表" + table + ",sql语句生成失败！" );
						}
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				surplusTable.add(sql);
				continue;
			}
			sBuilder.append(sql + "\r\n\r\n");
		}
		if (surplusTable.size() != 0) {
			sortSql(sBuilder, surplusTable);
		}
	}

}
