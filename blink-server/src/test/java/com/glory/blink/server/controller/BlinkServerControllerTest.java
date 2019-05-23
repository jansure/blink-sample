package com.glory.blink.server.controller;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BlinkServerControllerTest {
	
	
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
    // 初始化执行
    @Before
    public void setUp() throws Exception {
    	// 项目拦截器有效  初始化MockMvc对象
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @Test
	public void updateClipboardStrategyTest() throws Exception{
    	
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/"))
      		   )
 		    	.andDo(MockMvcResultHandlers.print())
 		        .andReturn(); 
	}
    
    
    @Test
	public void updateFileStrategyTest() throws Exception{
    	
//    	FileStrategyDTO dto = new FileStrategyDTO();
//    	dto.setUserId(168);
//    	dto.setRedirectionMode("DISABLED");
//    	String requestJson = JsonUtil.serialize(dto);
//		mockMvc.perform(post("/strategy/updateFileStrategy").contentType(MediaType.APPLICATION_JSON)
//    			.content(requestJson )
//      		   )
// 		    	.andDo(MockMvcResultHandlers.print())
// 		        .andReturn(); 
	}
    
}
