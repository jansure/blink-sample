package com.glory.blink.server.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.glory.blink.server.dao.SchameDao;
import com.glory.blink.server.domain.Schame;
import com.glory.blink.server.service.IBlinkService;
import com.glory.blink.server.utils.flink.DynamicBeanUtils;
import com.glory.blink.server.utils.flink.JobUtils;
import com.glory.blink.server.utils.schema.ConfigUtil;
import com.glory.blink.server.utils.schema.SchameJsonBean;

@Service
public class BlinkServiceImpl implements IBlinkService {

	@Resource
	private SchameDao schameDao;
	
	@Resource
	private JobUtils jobUtils;
	

	@Override
	public List<Schame> list(Schame schame) {
		return schameDao.list(schame);
	}

	@Override
	public Integer insert(Schame schame) {
		return schameDao.insert(schame);
	}

	@Override
	public Integer update(Schame schame) {
		return schameDao.update(schame);
	}

	@Override
	public Schame getByTableName(String tableName) {
		return schameDao.getByTableName(tableName);
	}

	@Override
	public String doJob(String sql, Schame schame) {
		String uuid = UUID.randomUUID().toString();
		SchameJsonBean config = ConfigUtil.configToBean(schame.getDescribe());
		
		//异步执行
		ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
		pool.execute(new Runnable() {

			@Override
			public void run() {
				jobUtils.doJob(uuid, sql, config);
			}
			
		});
		pool.shutdown();
		return uuid;
	}

	@Override
	public String getSchame() {
		String result = "";
		try {
			result = DynamicBeanUtils.readTemplate("schema.json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String queryTask(String uuid) {
		
		String result = "";
		try {
			String path = JobUtils.taskFolder + "/" + uuid + "/result.out";
			result = DynamicBeanUtils.readFile(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
