package com.glory.blink.server.utils.schema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

import com.glory.blink.server.utils.JsonUtil;

public class ConfigUtil {

	public static String convertStreamToString(InputStream inputStream){
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            inputStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    return sb.toString();
	}
	
	public static SchameJsonBean configToBean(String config){
		return JsonUtil.deserialize(config , SchameJsonBean.class);
	}
	
	public static String beanToConfig(SchameJsonBean bean){
		return JsonUtil.serialize(bean);
	}
	
	public static LinkedHashMap<String, String> getFieldMap(SchameJsonBean bean) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		for(Schemas schema : bean.getSchemas()) {
			map.put(schema.getKey(), schema.getType());
		}
		return map;
	}
	
	public static String getFieldStr(SchameJsonBean bean) {
		String result = "";
		for(Schemas schema : bean.getSchemas()) {
			result += schema.getKey() + ",";
		}
		
		if(result.equals("")) {
			return result;
		}
		
		return result.substring(0, result.length() - 1);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String path = "G:\\git\\blink-server\\src\\main\\resources\\schema.json";
		String jsonStr = convertStreamToString(new FileInputStream(new File(path)));
		SchameJsonBean bean = JsonUtil.deserialize(jsonStr , SchameJsonBean.class);
		
		System.out.println(bean.getTableName());
		
		
	}

}
