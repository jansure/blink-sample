package com.glory.blink.server.utils.flink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class DynamicBeanUtils {

	// 本项目Jar，用于编译文件中有Flink包的情况,在配置文件中配置blink.compiler.jar
	public static String jarName = "";
	
	public static String serverHost = "";
	
	public static String serverPort = "";

	// 动态生成Bean的包名
	private String beanPackage = "com.pkpm.flink.dynamic";

	// pojo类模板
	private static String pojoBeanTemplate = "package ${beanPackage}; import java.sql.Timestamp; public class ${className}{${classBody}}";

	
	public void setBeanPackage(String beanPackage) {
		this.beanPackage = beanPackage;
	}
	
	public String getBeanPackage() {
		return beanPackage;
	}

	/**
	 * 创建POJO类
	 * 
	 * @param className
	 *            类名
	 * @param fieldMap
	 *            字段配置。 id->int, name->String这种形式
	 * @return
	 */
	public String createPojoBean(String className, LinkedHashMap<String, String> fieldMap) {

		return pojoBeanTemplate.replace("${beanPackage}", beanPackage).replace("${className}", className)
				.replace("${classBody}", createPojoBody(fieldMap));
	}

	public static String createPojoBody(LinkedHashMap<String, String> fieldMap) {

		StringBuilder result = new StringBuilder();
		String fieldNames = "";
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			String fieldStr = createField(entry.getKey(), entry.getValue());
			result.append(fieldStr);
			String getterStr = createGetter(entry.getKey(), entry.getValue());
			result.append(getterStr);
			String setterStr = createSetter(entry.getKey(), entry.getValue());
			result.append(setterStr);

			if ("".equals(fieldNames)) {
				fieldNames += "\"";
			} else {
				fieldNames += "\",";
			}

			fieldNames += entry.getKey() + "=\" + " + entry.getKey() + "+";

		}

		String toString = createToString(fieldNames);
		result.append(toString);

		return result.toString();

	}

	private static String createToString(String str) {
		String template = "public String toString() {return ${string};}";
		return template.replace("${string}", str.substring(0, str.length() - 1));
	}

	private static String createSetter(String fieldName, String fieldType) {
		String template = "public void set${fieldNameUpOne}(${fieldType} ${fieldName}){this.${fieldName} = ${fieldName};}";
		String fieldNameUpOne = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

		return template.replace("${fieldName}", fieldName).replace("${fieldType}", fieldType)
				.replace("${fieldNameUpOne}", fieldNameUpOne);
	}

	private static String createGetter(String fieldName, String fieldType) {
		String template = "public ${fieldType} get${fieldNameUpOne}(){return this.${fieldName};}";
		String fieldNameUpOne = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

		return template.replace("${fieldName}", fieldName).replace("${fieldType}", fieldType)
				.replace("${fieldNameUpOne}", fieldNameUpOne);
	}

	private static String createField(String fieldName, String fieldType) {
		return "private " + fieldType + " " + fieldName + ";";
	}

	public String getSourcePath(String className) {
		return beanPackage.replace(".", "/") + "/" + className + ".java";
	}

	public void createBean(String className, String sourceContent, String classPath) throws Exception {
		String sourcefileName = getSourcePath(className);
		String srcPath = classPath + "/src/";
		File file = new File(srcPath + sourcefileName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(sourceContent);
		fileWriter.flush();
		fileWriter.close();

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(file.getAbsolutePath());
		File dest = new File(classPath);
		if (!dest.exists()) {
			dest.mkdirs();
		}

		// options就是指定编译输入目录，与我们命令行写javac -d C://是一样的
		List<String> options = new ArrayList<String>();
		options.add("-cp");
		options.add(dest.getAbsolutePath() + ";" + jarName);
		options.add("-d");
		options.add(dest.getAbsolutePath());
		JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, options, null, javaFileObjects);
		task.call();
		manager.close();

		 delete(new File(srcPath));

	}

	public static String readTemplate(String templateFile) throws Exception {
		StringBuffer result = new StringBuffer();
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateFile);

		byte[] b = new byte[4096];
		for (int n; (n = input.read(b)) != -1;) {
			result.append(new String(b, 0, n));
		}

		input.close();
		return result.toString();
	}
	
	public static String readFile(String fileName) throws Exception {
		
		//获取本机编码
		String charsetName = System.getProperty("sun.jnu.encoding");
		
		StringBuffer result = new StringBuffer();
		InputStream input = new FileInputStream(fileName);

		
		byte[] b = new byte[4096];
		for (int n; (n = input.read(b)) != -1;) {
			result.append(new String(b, 0, n, charsetName));
		}

		input.close();
		return result.toString();
	}

	public static void delete(File file) {
		if (!file.exists())
			return;

		if (file.isFile() || file.list() == null) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File a : files) {
				delete(a);
			}
			file.delete();
		}

	}
}
