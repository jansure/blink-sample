package com.glory.blink.server.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.glory.blink.server.domain.Schame;
import com.glory.blink.server.utils.mybatis.SimpleInsertLangDriver;
import com.glory.blink.server.utils.mybatis.SimpleSelectLangDriver;
import com.glory.blink.server.utils.mybatis.SimpleUpdateLangDriver;

@Mapper

public interface SchameMapper {

	@Select("select * from schame (#{schame})")
	@Lang(SimpleSelectLangDriver.class)
	List<Schame> list(Schame schame);

	@Insert("insert into schame (#{schame})")
	@Lang(SimpleInsertLangDriver.class)
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer insert(Schame schame);

	@Update("update schame (#{schame}) WHERE id = #{id}")
	@Lang(SimpleUpdateLangDriver.class)
	Integer update(Schame schame);

}