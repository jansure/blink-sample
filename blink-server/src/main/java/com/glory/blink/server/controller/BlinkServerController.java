package com.glory.blink.server.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.glory.blink.server.domain.Schame;
import com.glory.blink.server.service.IBlinkService;
import com.glory.blink.server.utils.ResultObject;
import com.glory.blink.server.utils.SqlParseUtils;
import com.glory.blink.server.utils.schema.ConfigUtil;
import com.glory.blink.server.utils.schema.SchameJsonBean;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j
public class BlinkServerController {

	@Resource
	private IBlinkService blinkService;

	@GetMapping("/")
	public ResultObject index() {
		String ok = "ok";
		return ResultObject.success(ok);
	}

	@PostMapping("/createSchame")
	public ResultObject createSchame(MultipartFile file) {

		Preconditions.checkArgument(!file.isEmpty(), "请上传配置文件文件!");

		String configStr = null;
		try {
			configStr = ConfigUtil.convertStreamToString(file.getInputStream());
		} catch (IOException e) {
			return ResultObject.failure("配置文件解析出现错误!");
		}

		SchameJsonBean bean = ConfigUtil.configToBean(configStr);
		if (bean.getSchemas() == null || bean.getTableName() == null || "".equals(bean.getTableName())
				|| bean.getSchemas().size() == 0) {
			return ResultObject.failure("配置文件错误!请下载表模版！");
		}

		Schame schame = blinkService.getByTableName(bean.getTableName());
		if(schame != null) {
			return ResultObject.failure("表:" + schame.getTableName() + "已经存在，请直接查询！");
		}
		
		schame = new Schame();
		schame.setTableName(bean.getTableName());
		schame.setDescribe(ConfigUtil.beanToConfig(bean));
		blinkService.insert(schame);

		return ResultObject.success("创建表结构成功");
	}

	@GetMapping("/submit")
	public ResultObject submit(String sql) {

		try {
			
			List<String> nameList = SqlParseUtils.getTableNameList(sql);
			if(nameList.size() > 1) {
				return ResultObject.failure("不支持多表查询!");
			}

			Schame schame = blinkService.getByTableName(nameList.get(0));
			if(schame == null) {
				return ResultObject.failure(nameList.get(0) + "，此表不存在!");
			}
			
			String uuid = blinkService.doJob(sql, schame);
			return ResultObject.success(uuid, "任务提交成功");

		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.failure("提交任务失败!");

		}
		
	}
	
	@GetMapping("/getSchame")
	public ResultObject getSchame() {
		String value = blinkService.getSchame();
		return ResultObject.success(value, "获取表模板成功");
	}
	
	
	@GetMapping("/queryTask")
	public ResultObject queryTask(String uuid) {

		try {

			String result = blinkService.queryTask(uuid);
			return ResultObject.success(result);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return ResultObject.failure("查询任务失败!");
	}

}
