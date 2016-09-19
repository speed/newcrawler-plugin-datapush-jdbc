package com.newcrawler.plugin.datapush.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.newcrawler.plugin.datapush.common.BaseDao;
import com.soso.plugin.DataPushPlugin;
import com.soso.plugin.bo.DataPushPluginBo;


public final class DataPushPluginService implements com.soso.plugin.DataPushPlugin{
	
	public String init(final DataPushPluginBo dataPushPluginBo) throws Exception{
		Map<String, String[]> httpRequestParameters=dataPushPluginBo.getHttpRequestParameters();
		Map<String, String> properties=dataPushPluginBo.getProperties();
		String tableName=properties.get("jdbc-0.table");
		if(tableName==null || "".equals(tableName)){
			return "Need to set the table name.";
		}
		String isExistSQL="show tables like '"+tableName+"';";
		int count=BaseDao.getRows(properties, isExistSQL, null);
		boolean result = false;
		if(count==0){
			//create table
			String sql="CREATE TABLE `"+tableName+"` ( `ID` bigint(20) NOT NULL AUTO_INCREMENT";
			if(httpRequestParameters!=null && !httpRequestParameters.isEmpty()){
				for(String key:httpRequestParameters.keySet()){
					String cls[]=httpRequestParameters.get(key);
					sql+=", `"+cls[0]+"` varchar("+cls[1]+") DEFAULT NULL";
				}
			}
			sql+=", PRIMARY KEY (`ID`)) ENGINE=InnoDB;";
			result = BaseDao.execute(properties, sql, null);
		}else{
			//alert table
			String sql="show columns from `"+tableName+"`;";
			@SuppressWarnings("unchecked")
			List<TableBo> list = (List<TableBo>)(Object)BaseDao.query(properties, TableBo.class, sql, null);
			
			List<String> columnList=new ArrayList<String>();
			if(list!=null && !list.isEmpty()){
				for(TableBo tableBo:list){
					columnList.add(tableBo.getCOLUMN_NAME());
				}
			}
			if(httpRequestParameters!=null && !httpRequestParameters.isEmpty()){
				for(String key:httpRequestParameters.keySet()){
					String cls[]=httpRequestParameters.get(key);
					if(!columnList.contains(key)){
						String alertsql="ALTER TABLE `"+tableName+"` ADD COLUMN `"+cls[0]+"` VARCHAR("+cls[1]+") NULL;";
						result = BaseDao.execute(properties, alertsql, null);
					}
				}
			}
		}
		return result?DataPushPlugin.Status.success.name():DataPushPlugin.Status.failure.name();
	}
	
	@Override
	public String execute(final DataPushPluginBo dataPushPluginBo) throws Exception {
		Map<String, String> properties=dataPushPluginBo.getProperties();
		Map<String, String[]> httpRequestParameters=dataPushPluginBo.getHttpRequestParameters();
		
		//PARAM0=${产品ID}&PARAM2=${用户}&PARAM3=${主题}&PARAM4=${日期}&PARAM5=${评论}
		
		String tableName=properties.get("jdbc-0.table");
		if(tableName==null || "".equals(tableName)){
			return "Need to set the table name.";
		}
		
		List<Object> parameters=new ArrayList<Object>();
		String insertSQL=this.getInsertSQL(tableName, httpRequestParameters, parameters);
		
		try{
			BaseDao.saveOrUpdate(properties, insertSQL, parameters);
		}catch(Exception e){
			throw new SQLException("Insert into '"+tableName+"' failure, "+e.toString());
		}finally{
			parameters.clear();
		}
		return DataPushPlugin.Status.success.name();
	}
	
	private String getInsertSQL(String tableName, Map<String, String[]> httpRequestParameters, List<Object> parameters){
		String columnNames="";
		String columnValues="";
		
		for(String key:httpRequestParameters.keySet()){
			String[] values=httpRequestParameters.get(key);
			if(values==null || values.length==0){
				continue;
			}
			
			if(!"".equals(columnNames)){
				columnNames+=",";
			}
			columnNames+=" `"+key+"` ";
			
			if(!"".equals(columnValues)){
				columnValues+=",";
			}
			parameters.add(values[0]);
			columnValues+=" ? ";
		}
		String sql="INSERT INTO `"+tableName+"` ("+columnNames+") VALUES ("+columnValues+");";
		return sql;
	}

	@Override
	public void destory() throws Exception {
		
	}

}
