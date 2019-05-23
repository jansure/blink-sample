package com.pkpm.flink.dynamic;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class Splitter implements FlatMapFunction<String, User> {

		@Override
		public void flatMap(String text, Collector<User> out) throws Exception {

			String[] textArray = text.split("\t");
			
			User obj = new User();
			
			Field[] fields = User.class.getDeclaredFields();
			for(int i = 0; i < fields.length; i ++) {
				Field field = fields[i];
				field.setAccessible(true);
				setValue(field, obj, textArray[i]);
			}
			
			out.collect(obj);
		}

		private void setValue(Field field, User obj, String value) throws Exception {
			
			String type = field.getGenericType().toString().toLowerCase();
			if(type.contains("int")) {
				field.set(obj, new Integer(value));
			}else if(type.contains("long")) {
				field.set(obj, new Long(value));
			}else if(type.contains("short")) {
				field.set(obj, new Short(value));
			}else if(type.contains("double")) {
				field.set(obj, new Double(value));
			}else if(type.contains("float")) {
				field.set(obj, new Float(value));
			}else if(type.contains("timestamp")) {
				field.set(obj, Timestamp.valueOf(value));
			}else {
				field.set(obj, value);
			}
			
		}

	}
