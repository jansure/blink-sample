package com.glory.blink.server.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.glory.blink.server.utils.flink.DynamicBeanUtils;

@Component
public class InitEnv {
	
	@Value("${blink.compiler.jar}")
	private String blinkJar;
	
	@Value("${blink.server.host}")
	private String serverHost;
	
	@Value("${blink.server.port}")
	private String serverPort;
	
	@PostConstruct
    public void init() {
		DynamicBeanUtils.jarName = blinkJar;
		
		DynamicBeanUtils.serverHost = serverHost;
		
		DynamicBeanUtils.serverPort = serverPort;
    }

}
