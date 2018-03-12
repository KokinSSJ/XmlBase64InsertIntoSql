package com.mlmb.sqlparser;

public enum SqlQueryType {

	INSERT_INTO("insert\\s+into"), UPDATE("update"), DELETE("delete"), SELECT("select");

	private String starter;


	private SqlQueryType(String starter) {
		this.starter = starter;
	}

	public String getStarter() {
		return starter;
	}
}
