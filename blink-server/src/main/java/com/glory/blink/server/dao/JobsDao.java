package com.glory.blink.server.dao;

import java.util.List;

import com.glory.blink.server.domain.Jobs;

public interface JobsDao {
	
	List<Jobs> list(Jobs jobs);
	
    Integer insert(Jobs jobs);

    Integer update(Jobs jobs);

	Jobs getByUUID(String uuid);

	

}
