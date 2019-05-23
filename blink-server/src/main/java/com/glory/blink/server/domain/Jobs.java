package com.glory.blink.server.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jobs {
	
	/** 自增主键 */
	private Integer id;
	
    /**任务ID */
    private String uuid;
    
    /**任务状态：0，创建中；1，成功；-1，失败 */
    private Integer jobStatus;
    
    /**错误信息 */
    private String error;
    
    /**创建时间 */
    private LocalDateTime createTime;
    
    /**结束时间 */
    private LocalDateTime endTime;
    
    private Integer userId;

}