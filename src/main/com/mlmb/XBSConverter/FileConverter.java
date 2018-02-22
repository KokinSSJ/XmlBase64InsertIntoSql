package com.mlmb.XBSConverter;

import java.nio.file.Path;
import java.util.List;

public class FileConverter {

	// moze tutaj wszysktie parametry sqla? i tworzy dla nich plik sql
	private Path pathToXml;
	private Path pathToSql;
	private String base64;
	private List<String> sqlBody;

	private String newSqlBody;

	private int fileSize;

	public FileConverter(Path pathToSql) {
		this.pathToSql = pathToSql;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getNewSqlBody() {
		return newSqlBody;
	}

	public void setNewSqlBody(String newSqlBody) {
		this.newSqlBody = newSqlBody;
	}

	public List<String> getSqlBody() {
		return sqlBody;
	}

	public void setSqlBody(List<String> sqlBody) {
		this.sqlBody = sqlBody;
	}

	public Path getPathToXml() {
		return pathToXml;
	}

	public void setPathToXml(Path pathToXml) {
		this.pathToXml = pathToXml;
	}
	public Path getPathToSql() {
		return pathToSql;
	}

	public void setPathToSql(Path pathToSql) {
		this.pathToSql = pathToSql;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
