package com.glory.blink.server.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.glory.blink.server.domain.Jobs;
import com.glory.blink.server.utils.mybatis.SimpleInsertLangDriver;
import com.glory.blink.server.utils.mybatis.SimpleSelectLangDriver;
import com.glory.blink.server.utils.mybatis.SimpleUpdateLangDriver;

@Mapper

public interface JobsMapper {

	@Select("select * from jobs (#{jobs})")
	@Lang(SimpleSelectLangDriver.class)
	List<Jobs> list(Jobs jobs);

	@Insert("insert into jobs (#{jobs})")
	@Lang(SimpleInsertLangDriver.class)
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer insert(Jobs jobs);

	@Update("update jobs (#{jobs}) WHERE id = #{id}")
	@Lang(SimpleUpdateLangDriver.class)
	Integer update(Jobs jobs);

}