package com.mlmb.sqlparser;

/**
 * Enum with all main queries types with theirs start names;
 * 
 * @author Kokin
 *
 */
public enum SqlQueryType {
	// mozna zamiast starter --> dac cale query lub oba i manipulowac nimi
	// tzn. starter + "\\s+" + bracket + joiner + bracket + ";"
	// rest = \\([a-zA-Z0-9_@#&,])\\
	// dobrze by bylo przekazac np. tablice, gdzie kazdy kolejny element to
	// kolejna czesc query

	INSERT_INTO("insert\\s+into",
			"insert\\s+into\\s+[a-zA-Z0-9_#$@]*\\s*\\([a-zA-Z0-9_@#$\\s'\",]*\\)\\s*values\\s*\\([a-zA-Z0-9_@#$\\s'\",]*\\)"),
	UPDATE("update", "update"), DELETE("delete", "delete"), SELECT("select", "select");
	// CREATE("create\\s+table");
	 
	private String starterPattern;
	
	private String wholeQueryPatter;


	private SqlQueryType(String starter, String wholeQueryPattern) {
		this.starterPattern = starter;
		this.wholeQueryPatter = wholeQueryPattern;
	}

	public String getStarterPattern() {
		return starterPattern;
	}

	public String getWholeQueryPatter() {
		return wholeQueryPatter;
	}

}
