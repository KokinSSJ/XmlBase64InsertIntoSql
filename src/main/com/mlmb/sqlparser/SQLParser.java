package com.mlmb.sqlparser;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class SQLParser {

	private String statement;

	private String tableName;

	private SqlQueryType type;

	private Map<String, String> columnParameterMap;

	public SQLParser(String statement) {
		if (StringUtils.isNotBlank(statement)) {
			this.statement = statement.trim().toLowerCase();
		} else {
			throw new IllegalArgumentException("Empty string");
		}
				
	}

	public void parse() {
		findStarter();
		if (type == SqlQueryType.INSERT_INTO) {
			String patternStartName = type.getStarter() + "\\s+";
			String patternEndName = patternStartName + "[a-zA-Z0-9\\_]*";

			Matcher matcherStartName = Pattern.compile(patternStartName).matcher(statement);
			Matcher matcherEndName = Pattern.compile(patternEndName).matcher(statement);

			if (matcherStartName.find() && matcherEndName.find()) {
				int startName = matcherStartName.end();
				int endName = matcherEndName.end();
				System.out.println(matcherEndName.group());
				this.tableName = statement.substring(startName, endName);
				System.out.println("\"" + this.tableName + "\"");
				System.out.println(statement + " : " + endName);
				System.out.println("odciecie: " + statement.substring(endName));
			} else {
				throw new IllegalArgumentException("Problem during reading table name");
			}

		} else {
			throw new IllegalArgumentException("SQL query type not supported :\"" + statement + "\"");
		}
	}

	private void findStarter() {
		setType(Arrays.stream(SqlQueryType.values()).filter(this::checkStart)
				.findFirst()
				.orElse(null));
	}

	private boolean checkStart(SqlQueryType type) {
		return Pattern.compile(type.getStarter()).matcher(statement).find();
	}

	public Map<String, String> getColumnParameterMap() {
		return columnParameterMap;
	}

	public void setColumnParameterMap(Map<String, String> columnParameterMap) {
		this.columnParameterMap = columnParameterMap;
	}

	public String getStatement() {
		return statement;
	}

	public String getTableName() {
		return tableName;
	}

	public SqlQueryType getType() {
		return type;
	}

	private void setType(SqlQueryType type) {
		this.type = type;
	}

}
