package com.glory.blink.server.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.glory.blink.server.dao.JobsDao;
import com.glory.blink.server.dao.mapper.JobsMapper;
import com.glory.blink.server.domain.Jobs;

@Repository
public class JobsDaoImpl implements JobsDao{

	@Resource
	private JobsMapper jobsMapper;

	@Override
	public List<Jobs> list(Jobs jobs) {
		return jobsMapper.list(jobs);
	}

	@Override
	public Integer insert(Jobs jobs) {
		return jobsMapper.insert(jobs);
	}

	@Override
	public Integer update(Jobs jobs) {
		return jobsMapper.update(jobs);
	}

	@Override
	public Jobs getByUUID(String uuid) {
		
		Jobs jobs = new Jobs();
		jobs.setUuid(uuid);
		List<Jobs> list = jobsMapper.list(jobs);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	  
}
