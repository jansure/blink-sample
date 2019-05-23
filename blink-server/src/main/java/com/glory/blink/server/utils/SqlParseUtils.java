package com.glory.blink.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.util.JdbcConstants;

public class SqlParseUtils {

	public static void main(String[] args) throws Exception {
		String dbType = JdbcConstants.MYSQL; // JdbcConstants.MYSQL或者JdbcConstants.POSTGRESQL
		String sql = "select * from mytable a, test b  where a.id = 3 and b.name='dd'";
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

		ExportTableNameVisitor visitor = new ExportTableNameVisitor();
		for (SQLStatement stmt : stmtList) {
			stmt.accept(visitor);
		}

		System.out.println(visitor.getNameList());
	}

	public static List<String> getTableNameList(String sql) throws Exception {
		String dbType = JdbcConstants.MYSQL;
		
		try {
			List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
			ExportTableNameVisitor visitor = new ExportTableNameVisitor();
			for (SQLStatement stmt : stmtList) {
				stmt.accept(visitor);
			}
			
			return visitor.getNameList();
			
			
		}catch(Exception ex) {
			throw ex;
		}
		
	}

	static class ExportTableNameVisitor extends MySqlASTVisitorAdapter {
		private List<String> nameList = new ArrayList<String>();

		public boolean visit(SQLExprTableSource x) {
			nameList.add(x.toString());
			return true;
		}

		public List<String> getNameList() {
			return nameList;
		}

	}

}
