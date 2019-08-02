package com.yzf.proxy.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yzf.proxy.packet.UrlInfo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

public class ProxyCore {
	private static Set<Pool> pools = new HashSet<Pool>();//连接池
	private static Map<ConnectionInfo, Allocation> map = new HashMap<ConnectionInfo, Allocation>();//访问队列队列
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
				pool.url = map.get("jdbcUrl");
				pool.user = map.get("user");
				pool.password = map.get("password");
				//解析配置文件，获取连接信息
				pool.dataSource = new ComboPooledDataSource(pool.name);
				//使用c3p0解析配置，获取datasource
				UrlInfo ui = new UrlInfo(pool.url);
				pool.database = ui.database;
				pools.add(pool);
				//存储新建连接池
		}
		for (Pool pool : pools) {
			ConnectionInfo info = new ConnectionInfo();
			info.user = pool.user;
			info.database = pool.database;
			info.password = pool.password;
			Allocation all = new Allocation(pool.dataSource);
			//新建连接池访问队列
			map.put(info, all);
			//存储队列
			all.start();
			//队列检测执行
		}
	}

	/**
	 *	连接请求加入队列
	 * @param database 数据库名
	 * @param user	用户名
	 * @param password 密码
	 * @param nio 服务器连接镜像类
	 */
	public static void addNioConnection(String database, String user, String password, NioConnection nio) {
		ConnectionInfo info = new ConnectionInfo();
		info.database = database;
		info.user = user;
		info.password = password;
		//连接信息创建
		Allocation all = map.get(info);
		//获取目标队列
		all.addNioConnection(nio);
		//请求入队列
	}
}

class Pool{//数据源连接池类
	public ComboPooledDataSource dataSource;
	public String name;
	public String url;
	public String user;
	public String password;
	public String database;
	public Pool() {}
}