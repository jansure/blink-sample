package com.glory.blink.server.utils.flink;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.glory.blink.server.dao.JobsDao;
import com.glory.blink.server.domain.Jobs;
import com.glory.blink.server.utils.JsonUtil;
import com.glory.blink.server.utils.schema.ConfigUtil;
import com.glory.blink.server.utils.schema.SchameJsonBean;

@Component
public class JobUtils {
	
	public static String taskFolder = "task";
	
	@Resource
	private JobsDao jobsDao;
	
	public void doJob(String uuid, String sql, SchameJsonBean config) {
		
		Jobs jobs = insertJob(uuid);
				
		DynamicBeanUtils dynamicBeanUtils = new DynamicBeanUtils();
		dynamicBeanUtils.setBeanPackage("com.pkpm.flink.dynamic.uu" + uuid.replace("-", ""));
		
		String filePath = new File(taskFolder + "/" + uuid).getAbsolutePath().replace("\\", "/");
		String targetPath = filePath + "/target/";
		String classPath = targetPath + "class/";
		String jarPath = targetPath + uuid + "_app.jar";
		
		String className = "MyBean";
		
		LinkedHashMap<String, String> fieldMap = ConfigUtil.getFieldMap(config);
		
		String splitter = config.getSplitter();
		
		String inputFile = config.getPath();
		String tableName = config.getTableName();
		String fields = ConfigUtil.getFieldStr(config);
		String outFile = "file:///" + filePath + "/result.out";
//		outFile = "hdfs:///localhost:9000/" + filePath + "/result.out";
		
		String sourceContent = dynamicBeanUtils.createPojoBean(className, fieldMap);
		try {
			
			// 创建POJO
			dynamicBeanUtils.createBean(className, sourceContent, classPath);
		
			// 创建Splitter
			sourceContent = DynamicBeanUtils.readTemplate("splitter.template");
			
			sourceContent = sourceContent.replace("${beanPackage}", dynamicBeanUtils.getBeanPackage())
					.replace("${className}", className).replace("${splitter}", splitter );
			
			dynamicBeanUtils.createBean("Splitter", sourceContent, classPath);
			
			// 创建Sink
			sourceContent = DynamicBeanUtils.readTemplate("sink.template");
			
			sourceContent = sourceContent.replace("${beanPackage}", dynamicBeanUtils.getBeanPackage());
			
			dynamicBeanUtils.createBean("WriterSinkFactory", sourceContent, classPath);

			// 创建Flink
			sourceContent = DynamicBeanUtils.readTemplate("flink.template");
		
			sourceContent = sourceContent.replace("${beanPackage}", dynamicBeanUtils.getBeanPackage())
					.replace("${jarFiles}", jarPath).replace("${inputFile}", inputFile)
					.replace("${tableName}", tableName).replace("${fields}", fields)
					.replace("${sql}", sql).replace("${outFile}", outFile)
					.replace("${serverHost}", DynamicBeanUtils.serverHost)
					.replace("${serverPort}", DynamicBeanUtils.serverPort)
					.replace("${className}", className);
			dynamicBeanUtils.createBean("Flink", sourceContent, classPath);
			
			//打Jar包
			JarUtils.jar(classPath, jarPath);
			
			// 将Jar包加载到系统中
		    Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
	        method.setAccessible(true);// 设置方法的访问权限
		    
	        // 获取系统类加载器
	        URLClassLoader classLoader = (URLClassLoader)JobUtils.class.getClassLoader();
	        if(classLoader == null) {
	        	classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	        }
	        
	        URL  appjarUrl = new URL("jar:file:/" + jarPath + "!/");
			method.invoke(classLoader, appjarUrl); 
			
			URL flinkjarUrl = new URL("jar:file:/" + DynamicBeanUtils.jarName + "!/");
			method.invoke(classLoader, flinkjarUrl); 
			
			Object obj = classLoader.loadClass(dynamicBeanUtils.getBeanPackage() + ".Flink").newInstance();
			Method apply = obj.getClass().getMethod("apply");
			Object invoke = apply.invoke(obj);
			
			//将任务写入数据库
			updateJobs(jobs, 1, null);

			//卸载Jar包
			close(appjarUrl);
			close(flinkjarUrl);

			//删除所有文件
			DynamicBeanUtils.delete(new File(targetPath));
		} catch (Exception e) {
			e.printStackTrace();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();;
			PrintStream ps = new PrintStream(baos);
		    e.printStackTrace(ps);
		    String s = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		    updateJobs(jobs, -1, s);
			
		}
		
	}

	private void updateJobs(Jobs jobs, int status, String error) {
		jobs.setJobStatus(status);
		jobs.setEndTime(LocalDateTime.now());
		jobs.setError(error);
		jobsDao.update(jobs);
		
	}

	private Jobs insertJob(String uuid) {
		Jobs jobs =	new Jobs();
		
		jobs.setJobStatus(0);//创建中
		jobs.setUuid(uuid);
		jobs.setCreateTime(LocalDateTime.now());
		jobsDao.insert(jobs);
		return jobs;
	}

	private static void close(URL jarUrl) {
		
		try {
			JarURLConnection jarURLConnection = (JarURLConnection) jarUrl.openConnection();
			jarURLConnection.getJarFile().close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws Exception {

		String uuid = UUID.randomUUID().toString();
		
		String sql = "SELECT * FROM test WHERE salary > 5.9";
		String path = "G:\\git\\blink\\blink-server\\src\\main\\resources\\schema2.json";
		String jsonStr = ConfigUtil.convertStreamToString(new FileInputStream(new File(path)));
		SchameJsonBean bean = JsonUtil.deserialize(jsonStr , SchameJsonBean.class);;
		new JobUtils().doJob(uuid, sql, bean);
		
		
	}

}
