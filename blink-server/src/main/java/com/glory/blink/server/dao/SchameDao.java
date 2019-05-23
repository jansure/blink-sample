package com.glory.blink.server.dao;

import java.util.List;

import com.glory.blink.server.domain.Schame;

public interface SchameDao {
	
	List<Schame> list(Schame schame);
	
    Integer insert(Schame schame);

    Integer update(Schame schame);

	Schame getByTableName(String tableName);

	

}
