package com.glory.blink.server.utils.schema;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;

@Data
public class SchameJsonBean {
	
	private String tableName;
	
	private String path;
	
	private String splitter;
	
	private List<Schemas> schemas;
	
}

@Data
class Schemas {
	private String key;
	
	private String type;
}


