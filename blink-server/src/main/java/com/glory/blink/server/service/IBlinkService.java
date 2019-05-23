package com.glory.blink.server.service;

import java.util.List;

import com.glory.blink.server.domain.Schame;

public interface IBlinkService {
	
List<Schame> list(Schame schame);
	
    Integer insert(Schame schame);

    Integer update(Schame schame);

	Schame getByTableName(String tableName);

	String doJob(String sql, Schame schame);

	String getSchame();

	String queryTask(String uuid);
}
