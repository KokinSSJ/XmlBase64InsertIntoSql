package com.mlmb.sqlparser;

import java.lang.reflect.Field;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SQLParser.class })
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
		String statement = "insert     Into    table_name(column1) values('1' , 2, 3  );";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table_name");
	}

	@Test
	public void insertInto_newLineBetweenInsertAndIntoAndTableName() {
		String statement = "insert \n Into  \n table_name (column1) values ('1');";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table_name");
	}

	@Test
	public void insertInto_checkAllowedTableSigns() {
		String statement = "insert into table1_n@a$m#e (column1) values ('1');";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table1_n@a$m#e");
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

	@Test(expected = IllegalArgumentException.class)
	public void unknownQuery_notSupported() {
		String statement = "unknown query from table1 where column1 = '1';";
		sqlParser = new SQLParser(statement);
		sqlParser.parse();
	}

	@Test
	public void test_privateMethod() throws Exception {
		SQLParser sqlParser = PowerMockito.spy(new SQLParser("blabla"));
		// SQLParser sqlParser = PowerMockito.mock(SQLParser.class);
		PowerMockito.when(sqlParser, PowerMockito.method(SQLParser.class, "checkStart", SqlQueryType.class))
				.withArguments(SqlQueryType.INSERT_INTO).thenReturn(true);
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getType()).isEqualTo(SqlQueryType.INSERT_INTO);

	}

	// throws error because its only for example purposes
	@Test(expected = IllegalArgumentException.class)
	public void test2_privateStaticVariable() throws Exception {
		SQLParser sqlParser = PowerMockito.spy(new SQLParser("blabla"));
		Field field = PowerMockito.field(SQLParser.class, "state");
		field.set(SQLParser.class, new String("insert into table(column1) values (ble)"));
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table");

	}

	@Test
	public void test3_privateObjectVariable() throws Exception {
		SQLParser sqlParser = PowerMockito.spy(new SQLParser("blabla"));
		Field field = PowerMockito.field(SQLParser.class, "statement");
		field.set(sqlParser, new String("insert into table(column1) values (ble)"));
		sqlParser.parse();
		Assertions.assertThat(sqlParser.getTableName()).isEqualTo("table");
	
	 }

}
