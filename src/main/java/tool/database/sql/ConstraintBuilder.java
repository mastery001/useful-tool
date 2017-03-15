package tool.database.sql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tool.mastery.core.RandomChar;

public class ConstraintBuilder {

	/**
	 * 构建主键约束
	 * 
	 * @param primaryKey
	 * @return
	 */
	public static void buildPrimaryConstraint(StringBuilder sBuilder,
			List<String> primaryKey) {
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
	}

	public static String buildForeignConstraint(
			Map<String, String> foreignKeyConstraint) {
		StringBuilder sBuilder = new StringBuilder();
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
		return sBuilder.toString();
	}
}
