package com.glory.blink.server.utils.mybatis;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SimpleInsertLangDriver
 *
 * @author zhangshuai
 * @since 10/01/2018
 */
public class SimpleInsertLangDriver extends XMLLanguageDriver implements LanguageDriver {

    private final Pattern inPattern = Pattern.compile("\\(#\\{(\\w+)\\}\\)");

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {

        Matcher matcher = inPattern.matcher(script);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            StringBuilder tmp = new StringBuilder();
            sb.append("(");

            for (Field field : parameterType.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Invisible.class)) {
                    sb.append("`").append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())).append("`").append(",");
                    tmp.append("#{").append(field.getName()).append("},");
                }
            }

            sb.deleteCharAt(sb.lastIndexOf(","));
            tmp.deleteCharAt(tmp.lastIndexOf(","));
            sb.append(") values (").append(tmp.toString()).append(")");

            script = matcher.replaceAll(sb.toString());
            script = "<script>" + script + "</script>";
        }

        return super.createSqlSource(configuration, script, parameterType);
    }
/*    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
    	
    	Matcher matcher = inPattern.matcher(script);
    	if (matcher.find()) {
    		StringBuilder sb = new StringBuilder();
    		StringBuilder tmp = new StringBuilder();
    		sb.append("(");
    		
    		for (Field field : parameterType.getDeclaredFields()) {
    			if (!field.isAnnotationPresent(Invisible.class)) {
    				sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())).append(",");
    				tmp.append("#{").append(field.getName()).append("},");
    			}
    		}
    		
    		sb.deleteCharAt(sb.lastIndexOf(","));
    		tmp.deleteCharAt(tmp.lastIndexOf(","));
    		sb.append(") values (").append(tmp.toString()).append(")");
    		
    		script = matcher.replaceAll(sb.toString());
    		script = "<script>" + script + "</script>";
    	}
    	
    	return super.createSqlSource(configuration, script, parameterType);
    }
*/
}