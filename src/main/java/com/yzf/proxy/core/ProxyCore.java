package com.yzf.proxy.core;

import com.yzf.proxy.packet.UrlInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.File;
import java.util.*;

public class ProxyCore {
	private static Set<Pool> pools = new HashSet<Pool>();//连接池
	private static Map<ConnectionInfo, DataSource> map = new HashMap<ConnectionInfo, DataSource>();
	private ProxyCore() {
	}
	
	public static void initialProxyCore(File file) throws Exception {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element rootElement = document.getRootElement();
		List<Element> elements = rootElement.elements();
		for (Element info : elements) {
			Pool pool = new Pool();
			pool.name = info.attributeValue("name");
			List<Element> elements1=info.elements();
			Map<String,String> map = new HashMap<String, String>();
			for (Element element : elements1) {
				map.put(element.attributeValue("name"), element.getText());
			}
				//获取连接数据
				pool.url = map.get("jdbcUrl");
				pool.user = map.get("user");
				pool.password = map.get("password");
				//解析配置文件，获取连接信息
				pool.dataSource = new HikariDataSource(new HikariConfig("src/"+pool.name+".properties"));
				//HikariCP连接池获取datasource
				UrlInfo ui = new UrlInfo(pool.url);
				pool.database = ui.database;
				pools.add(pool);
				//存储新建连接池
		}
		for (Pool pool : pools) {
			//创建连接信息
			ConnectionInfo info = new ConnectionInfo();
			info.user = pool.user;
			info.database = pool.database;
			info.password = pool.password;
			//存储数据源
			map.put(info, pool.dataSource);
		}
	}

	/**
	 * 获取连接
	 * @param database 数据库名
	 * @param user	用户名
	 * @param password 密码
	 * @param nio 服务器镜像连接对象
	 */
	public static void getNioConnection(String database, String user, String password, NioConnection nio) {
		ConnectionInfo info = new ConnectionInfo();
		info.database = database;
		info.user = user;
		info.password = password;//连接信息创建
		DataSource dataSource = map.get(info);//获取数据源
		try{
			nio.setConn(dataSource.getConnection());//设置连接
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}

class Pool{//数据源连接池类
	public HikariDataSource dataSource;
	public String name;
	public String url;
	public String user;
	public String password;
	public String database;
	public Pool() {}
}