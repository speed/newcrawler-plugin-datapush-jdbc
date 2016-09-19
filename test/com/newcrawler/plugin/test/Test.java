package com.newcrawler.plugin.test;

import java.util.HashMap;
import java.util.Map;

import com.newcrawler.plugin.datapush.jdbc.DataPushPluginService;
import com.soso.plugin.bo.DataPushPluginBo;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> properties=new HashMap<String, String>();
		String deployUrl=null;
		String httpRequestEncoding=null;
		Integer httpRequestTimeout=null;
		Map<String, String> httpRequestPropertys=null;
		Map<String, String[]> httpRequestParameters=new HashMap<String, String[]>();
		
		properties.put("jdbc-0.proxool.alias", "jdbc-table");
		properties.put("jdbc-0.proxool.driver-url", "jdbc:mysql://127.0.0.1:3306/newcrawler?characterEncoding=UTF-8");
		properties.put("jdbc-0.user", "root");
		properties.put("jdbc-0.password", "z");
		properties.put("jdbc-0.proxool.maximum-connection-count", "20");
		properties.put("jdbc-0.proxool.minimum-connection-count", "2");
		properties.put("jdbc-0.proxool.simultaneous-build-throttle", "10");
		properties.put("jdbc-0.table", "crawler_jdbc");
		
		String[] c={"crawlUrl","1500"};
		httpRequestParameters.put(c[0], c);
		String[] c1={"md5Digest","1500"};
		httpRequestParameters.put(c1[0], c1);
		String[] c2={"title","1500"};
		httpRequestParameters.put(c2[0], c2);
		String[] c3={"img","1500"};
		httpRequestParameters.put(c3[0], c3);
		String[] c4={"author","1500"};
		httpRequestParameters.put(c4[0], c4);
		String[] c5={"content","1500"};
		httpRequestParameters.put(c5[0], c5);
		String[] c6={"comment","1500"};
		httpRequestParameters.put(c6[0], c6);
		String[] c7={"comment2","1500"};
		httpRequestParameters.put(c7[0], c7);
		String[] c8={"comment3","1500"};
		httpRequestParameters.put(c8[0], c8);
		
		DataPushPluginBo dataPushPluginBo=new DataPushPluginBo(properties, deployUrl, httpRequestEncoding, httpRequestTimeout, httpRequestPropertys, httpRequestParameters);
		
		DataPushPluginService pluginService=new DataPushPluginService();
		pluginService.init(dataPushPluginBo);
		
	}

}
