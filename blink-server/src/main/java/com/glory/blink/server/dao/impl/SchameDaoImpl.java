package com.glory.blink.server.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.glory.blink.server.dao.SchameDao;
import com.glory.blink.server.dao.mapper.SchameMapper;
import com.glory.blink.server.domain.Schame;

@Repository
public class SchameDaoImpl implements SchameDao{

	@Resource
	private SchameMapper schameMapper;

	@Override
	public List<Schame> list(Schame schame) {
		return schameMapper.list(schame);
	}

	@Override
	public Integer insert(Schame schame) {
		return schameMapper.insert(schame);
	}

	@Override
	public Integer update(Schame schame) {
		return schameMapper.update(schame);
	}

	@Override
	public Schame getByTableName(String tableName) {
		
		Schame schame = new Schame();
		schame.setTableName(tableName);
		List<Schame> list = schameMapper.list(schame );
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	  
}
