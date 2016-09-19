package com.newcrawler.plugin.datapush.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;


public final class ConnectionPool {
	private static Log logger = LogFactory.getLog(ConnectionPool.class);
	private static volatile Map<String, String> map=new HashMap<String, String>();
	
	public static Connection getConnection(Map<String, String> properties) throws SQLException{
		String alias=properties.get("jdbc-0.proxool.alias");
		String content=properties.get("jdbc-0.proxool.driver-url")+
				properties.get("jdbc-0.user")+
				properties.get("jdbc-0.password")+
				properties.get("jdbc-0.proxool.maximum-connection-count")+
				properties.get("jdbc-0.proxool.minimum-connection-count")+
				properties.get("jdbc-0.proxool.simultaneous-build-throttle");
		
		if(map.containsKey(alias) && !content.equals(map.get(alias+"_content"))){
			destory(alias);
		}
		if(!map.containsKey(alias)){
			synchronized(ConnectionPool.class){
				if(!map.containsKey(alias)){
					init(properties);
					map.put(alias, alias);
					map.put(alias+"_content", content);
				}
			}
		}
		try {
			Connection conn = DriverManager.getConnection("proxool."+alias);
			return conn;
		} catch (Exception e) {
			destory(alias);
			throw e;
		}
	}
	private static void destory(String alias){
		map.remove(alias);
		map.remove(alias+"_content");
		try {
			ProxoolFacade.removeConnectionPool(alias);
		} catch (ProxoolException e) {
			logger.error("Destroy db connection pool error.", e);
		}
	}
	private static void init(Map<String, String> properties){
		Properties systemProp = new Properties();
		String alias=properties.get("jdbc-0.proxool.alias");
		systemProp.put("jdbc-0.proxool.alias", 							alias);
		systemProp.put("jdbc-0.proxool.driver-url", 					properties.get("jdbc-0.proxool.driver-url"));
		systemProp.put("jdbc-0.user", 									properties.get("jdbc-0.user"));
		systemProp.put("jdbc-0.password", 								properties.get("jdbc-0.password"));
		systemProp.put("jdbc-0.proxool.maximum-connection-count", 		properties.get("jdbc-0.proxool.maximum-connection-count"));
		systemProp.put("jdbc-0.proxool.minimum-connection-count", 		properties.get("jdbc-0.proxool.minimum-connection-count"));
		systemProp.put("jdbc-0.proxool.simultaneous-build-throttle", 	properties.get("jdbc-0.proxool.simultaneous-build-throttle"));
		
		systemProp.put("jdbc-0.proxool.driver-class", "com.mysql.jdbc.Driver");
		systemProp.put("jdbc-0.proxool.house-keeping-sleep-time", "40000");
		systemProp.put("jdbc-0.proxool.house-keeping-test-sql", "select CURRENT_DATE");
		systemProp.put("jdbc-0.proxool.maximum-connection-lifetime", "600000");
		logger.warn("初始化，连接池["+properties.get("jdbc-0.proxool.alias")+"]。");
		try {
			PropertyConfigurator.configure(systemProp);
		} catch (ProxoolException e) {
			logger.error("Proxool config error.", e);
			destory(alias);
		}
	}

}