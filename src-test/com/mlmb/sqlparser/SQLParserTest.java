package com.mlmb.sqlparser;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SQLParserTest {

	private SQLParser sqlParser;

	@Test(expected = IllegalArgumentException.class)
	public void emptyStatement() {
		sqlParser = new SQLParser(null);
		sqlParser.parse();
	}

	@Test
	 public void insertInto_FullQuery() {
		String statement = "insert Into table_name (column1, column2, column3) values ( value1, \"value2\", 3);";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
		// Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table_name");
	}

	@Test
	public void insertInto_manyWhitespaceBetweenInsertAndIntoAndTableName() {
		String statement = "insert     Into    table_name(column1) values('1');";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table_name");
	}

	@Test
	public void insertInto_newLineBetweenInsertAndIntoAndTableName() {
		String statement = "insert \n Into  \n table_name(column1) values('1');";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table_name");
	}

	@Test(expected = IllegalArgumentException.class)
	public void update_notSupported() {
		String statement = "update table_name SET column1 = 10, column2 = \"value2\" where column0 = 1";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
	}

	@Test(expected = IllegalArgumentException.class)
	public void selectAll_notSupported() {
		String statement = "select * from table1";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
	}

	@Test(expected = IllegalArgumentException.class)
	public void select_notSupported() {
		String statement = "select column1, column2 from table1;";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
	}

	@Test(expected = IllegalArgumentException.class)
	public void delete_notSupported() {
		String statement = "DeLete from table1 where column1 = '1';";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
	}

}
