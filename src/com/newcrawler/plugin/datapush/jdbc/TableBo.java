package com.newcrawler.plugin.datapush.jdbc;

public class TableBo {
	
	//[COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT, EXTRA]
	private String COLUMN_NAME;
	private String COLUMN_TYPE;
	
	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}
	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}
	public String getCOLUMN_TYPE() {
		return COLUMN_TYPE;
	}
	public void setCOLUMN_TYPE(String cOLUMN_TYPE) {
		COLUMN_TYPE = cOLUMN_TYPE;
	}
	
}
