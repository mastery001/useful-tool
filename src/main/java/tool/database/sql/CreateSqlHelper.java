package tool.database.sql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import tool.database.sql.annotation.BuildSqlByAnnotation;
import tool.database.sql.xml.BuildSqlByXml;
import tool.mastery.core.ClassUtil;
import tool.mastery.core.ReadXmlUtils;
import tool.mastery.core.ToolKit;
import tool.mastery.file.ClassFileHelper;

public class CreateSqlHelper {

	public String createSqlByXml() {
		return createSqlByXml("entitys.xml");
	}

	public String createSqlByXml(String configLocation) {
		return createSqlByXml(ToolKit.getResourceAsStream(configLocation));
	}

	@SuppressWarnings("unchecked")
	public String createSqlByXml(InputStream is) {
		StringBuilder sBuilder = new StringBuilder();
		Element rootElement = ReadXmlUtils.getRootElementByPath(is);
		List<Element> entityElements = rootElement.elements();
		// 保存多表的生成sql
		List<String> MutilTableSqlStatement = new ArrayList<String>();
		// 创建构建sql语句的对象，通过xml创建
		SqlBuilder sqlBuilder = new BuildSqlByXml();
		for (int i = 0; i < entityElements.size(); i++) {
			Element entityElement = entityElements.get(i);
			String sql = sqlBuilder.construct(entityElement);
			System.out.println(sql);
			// 如果存在需要创建外键时先不加入到sBuilder中
			if (sql.indexOf(SqlStatementConstant.FOREIGN_KEY) != -1) {
				MutilTableSqlStatement.add(sql);
				continue;
			}
			sBuilder.append(sql + "\r\n\r\n");
		}
		SqlUtil.sortSql(sBuilder, MutilTableSqlStatement);
		return sBuilder.toString();

	}

	public String createSqlByPackageName(String packageName) {
		StringBuilder sBuilder = new StringBuilder();
		// 得到该包对应的所有class文件
		Set<String> entitys = ClassFileHelper
				.getClassFileInPackage(packageName);
		// 保存多表的生成sql
		List<String> MutilTableSqlStatement = new ArrayList<String>();
		// 创建构建sql语句的对象，通过注解创建
		SqlBuilder sqlBuilder = new BuildSqlByAnnotation();
		// 遍历所有的class类
		for (Iterator<String> it = entitys.iterator(); it.hasNext();) {
			String className = it.next();
			String sql = "";
			try {
				// 创建对应po类生成的sql语句
				sql = sqlBuilder.construct(ClassUtil.getClassByName(className));
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
		SqlUtil.sortSql(sBuilder, MutilTableSqlStatement);
		return sBuilder.toString();
	}

}
