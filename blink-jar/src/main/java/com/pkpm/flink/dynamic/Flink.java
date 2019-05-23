package com.pkpm.flink.dynamic;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.sinks.csv.CsvTableSink;
import org.apache.flink.types.Row;

public class Flink {

	public static void main(String[] args) throws Exception {
		apply();
	}

	public static void apply() {
//		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		 
		String jarFiles = "D:\\test\\1557209337208\\1562/app.jar";
		StreamExecutionEnvironment env = StreamExecutionEnvironment.createRemoteEnvironment("localhost", 6123, jarFiles );
		StreamTableEnvironment tEnv = StreamTableEnvironment.getTableEnvironment(env);

		DataStreamSource<String> dss = env.readTextFile("file:///d:/test/user.txt");
		DataStream<User> ds = dss.flatMap(new Splitter());

		tEnv.registerDataStream("tableName", ds, "id, name, salary");

		Table result = tEnv.sqlQuery("SELECT * FROM  tableName WHERE salary > 5.9");

		tEnv.toAppendStream(result, Row.class).print();

		WriteMode mode = WriteMode.OVERWRITE;
		CsvTableSink sink = new CsvTableSink("file:///d:/test/user.out", "|", ExecutionConfig.PARALLELISM_DEFAULT,
				mode);
		result.writeToSink(sink);

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
