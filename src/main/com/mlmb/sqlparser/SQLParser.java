package com.mlmb.sqlparser;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class SQLParser {
	// leave for test purposes
	private static String state;

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
			getTableNameFromStatement();

			// check if 2x () -> only with columns names supported
			// String patternStartName =
			// "[a-zA-Z0-9_#$@]*\\([a-zA-Z0-9_@#$\\s']*\\)";
			String patternStartName = "\\([a-zA-Z0-9_@#$\\s'\",]*\\)";
			// popraw
			// !
			// zeby
			// zwraca³o
			// tylko
			// wartosci
			// albo
			// chociaz
			// przedzial

			Matcher matcherStartName = Pattern.compile(patternStartName).matcher(statement);
			while (matcherStartName.find()) {

				// int startName = matcherStartName.end();
				// statement.substring(startName);
				// System.out.println(statement.substring(startName));
				System.out.println(matcherStartName.start() + " " + matcherStartName.end());
				System.out.println("B:" + matcherStartName.group());
			}
			// if() group != 2 -> error -> not supported!

		} else {
			throw new IllegalArgumentException("SQL query type not supported :\"" + statement + "\"");
		}
	}

	private void findStarter() {
		// tutaj ppowinna byc zaleznosc od calego query zeby sprawdzic
		setType(Arrays.stream(SqlQueryType.values()).filter(this::checkStart)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No pattern found -> Unknown query")));
	}

	private boolean checkStart(SqlQueryType type) {
		return Pattern.compile(type.getWholeQueryPatter()).matcher(statement).find();
	}

	private void getTableNameFromStatement() {
		String patternStartName = type.getStarterPattern() + "\\s+";
		String patternEndName = patternStartName + "[a-zA-Z0-9_#$@]*";

		Matcher matcherStartName = Pattern.compile(patternStartName).matcher(statement);
		Matcher matcherEndName = Pattern.compile(patternEndName).matcher(statement);

		if (matcherStartName.find() && matcherEndName.find()) {
			int startName = matcherStartName.end();
			int endName = matcherEndName.end();
			this.tableName = statement.substring(startName, endName);
		} else {
			throw new IllegalArgumentException("Problem during reading table name");
		}
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
