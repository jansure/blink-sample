package com.glory.blink.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schame {
	
	/** 自增主键 */
	private Integer id;
	
    /**表名 */
    private String tableName;

    /**描述 */
    private String describe;

}