package tool.mastery.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CreateBeanUtil {

	public static boolean createBeanFile(String packageName, String className,
			List<String> params) {

		File dirs = new File("src/" + packageName.replace(".", "/"));
		// 如果目录不存在，则创建
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		File file = new File(dirs.getAbsolutePath() + "/" + className + ".java");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			List<String> newParams = new ArrayList<String>();
			for (int i = 0; i < params.size(); i++) {
				String[] splitParam = params.get(i).split(" ");
				// 如果是对象
				if (isType(splitParam[0])) {
					//如果类型是日期的话，则默认为java.util.date
					if(splitParam[0].equalsIgnoreCase("date")) {
						splitParam[0] = "java.util.Date";
					}
					newParams.add(splitParam[0] + " " + splitParam[1]);
				}else {
					//如果类型是日期的话，则默认为java.util.date
					if(splitParam[1].equalsIgnoreCase("date")) {
						splitParam[1] = "java.util.Date";
					}
					newParams.add(splitParam[1] + " " + splitParam[0]);
				}
			}
			String body = buildClassBody(packageName, className, newParams);
			// System.out.println(body);
			bw.write(body);
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return true;

	}

	private static String buildClassBody(String packageName, String className,
			List<String> params) {
		String body = null;
		StringBuilder sBuilder = new StringBuilder("package ");
		sBuilder.append(packageName + ";" + "\n\n");
		sBuilder.append("public class " + className + "{" + "\n");
		for (int i = 0; i < params.size(); i++) {
			sBuilder.append("\tprivate " +  params.get(i) + ";\n");
		}
		sBuilder.append("\n");
		for (int i = 0; i < params.size(); i++) {
			String[] splitParam = params.get(i).split(" ");
			String paramType = splitParam[0];
			String paramName = splitParam[1];

			// 生成get方法
			sBuilder.append("\tpublic " + paramType + " get"
					+ paramName.substring(0, 1).toUpperCase()
					+ paramName.substring(1, paramName.length()) + "() {\n");

			sBuilder.append("\t\t return " + "this." + paramName + ";\n\t}\n");
			sBuilder.append("\n");
			// 生成set方法
			sBuilder.append("\tpublic void set"
					+ paramName.substring(0, 1).toUpperCase()
					+ paramName.substring(1, paramName.length()) + "("
					+ params.get(i) + ") {\n");
			sBuilder.append("\t\tthis." + paramName + " = " + paramName
					+ ";\n\t}\n");
			sBuilder.append("\n");
		}
		sBuilder.append("}");
		body = sBuilder.toString();
		return body;
	}

	/**
	 * 根据流创建类
	 * 
	 * @param packageName
	 * @param br
	 * @throws IOException
	 */
	private static void createByReader(String packageName, BufferedReader br)
			throws IOException {
		String line = null;
		String className = null;
		boolean flag = false;
		List<String> params = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			//如果是空行则跳到下一行
			//if(!line.matches("\\w+")) continue;
			if (line.split(" ").length == 1) {
				// 创建类
				if (className != null && params.size() != 0) {
					flag = createBeanFile(packageName, className, params);
				}
				className = line;
				flag = false;
				params = new ArrayList<String>();
			} else {
				params.add(line);
			}
		}
		if (!flag) {
			flag = createBeanFile(packageName, className, params);
		}
	}

	/**
	 * 创建类
	 * 
	 * @param packageName
	 * @param fileName
	 */
	public static void create(String packageName, String fileName) {
		BufferedReader br = null;
		InputStream is = null;
		try {
			is = new FileInputStream(new File(fileName));
			br = new BufferedReader(new InputStreamReader(is));
			createByReader(packageName, br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否是类型
	 * @param typeName
	 * @return
	 */
	private static boolean isType(String typeName) {
		if (typeName.matches("[A-Z]{1}.+")
				|| typeName.toLowerCase().equals("int")
				|| typeName.toLowerCase().equals("float")
				|| typeName.toLowerCase().equals("double")) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		/*
		 * List<String> params = new ArrayList<String>();
		 * params.add("String name"); params.add("int age");
		 * createBeanFile("po", "User", params);
		 */
		create("cn.zlpm.po", "vo.txt");
	}

}
