package com.yzf.proxy.core;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NioConnection {//一个服务器镜像
	private long MAXTIME = 180000;//最长等待时间/ms/默认3分钟

	public void setMAXTIME(long MAXTIME) {
		this.MAXTIME = MAXTIME;
	}
	public long getMAXTIME() {
		return MAXTIME;
	}

	private NioConnection nioConnection = this;
	//自身对象，用于线程检测对象
	private long dateTime;//最近使用时间
	private int id;//镜像序号，客户端访问标识
	private Connection conn = null;//唯一的Connection
	private DatabaseMetaData dbmd = null;//唯一的数据库元数据
	private Map<Integer, StatementInfo> stmap = new HashMap<Integer, StatementInfo>();
	//多个Statement，特殊的PreparedStatement拥有ParameterMetaData
	private int is = 0;//Statement ID
	private Map<Integer, ResultSetInfo> rsmap = new HashMap<Integer, ResultSetInfo>();
	private int ir = 0;//ResultSet ID

	public NioConnection() {
		this.dateTime = dateTime;
		new Thread(() -> {
			while (nioConnection != null){
				try {
					nioConnection.check();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public int getId() {
		this.dateTime = System.currentTimeMillis();
		return id;
	}
	public void setId(int id) {
		this.dateTime = System.currentTimeMillis();
		this.id = id;
	}
	public Connection getConn() {
		this.dateTime = System.currentTimeMillis();
		return conn;
	}
	public void setConn(Connection conn) {
		this.dateTime = System.currentTimeMillis();
		this.conn = conn;
	}
	public DatabaseMetaData getDbmd() {
		this.dateTime = System.currentTimeMillis();
		return dbmd;
	}
	public void setDbmd(DatabaseMetaData dbmd) {
		this.dateTime = System.currentTimeMillis();
		this.dbmd = dbmd;
	}
	
	public int addStatement(Statement st) {
		this.dateTime = System.currentTimeMillis();
		is++;
		StatementInfo sti = new StatementInfo();
		sti.st = st;
		stmap.put(is, sti);
		return is;
	}
	public Statement getStatement(int id) {
		this.dateTime = System.currentTimeMillis();
		return stmap.get(id).st;
	}
	public PreparedStatement getPreparedStatement(int id) {
		this.dateTime = System.currentTimeMillis();
		return (PreparedStatement) stmap.get(id).st;
	}
	public CallableStatement getCallableStatement(int id) {
		this.dateTime = System.currentTimeMillis();
		return (CallableStatement) stmap.get(id).st;
	}
	public ParameterMetaData getParameterMetaData(int id) {
		this.dateTime = System.currentTimeMillis();
		return stmap.get(id).pmd;
	}
	public void addParameterMetaData(int id, ParameterMetaData pmd) {
		this.dateTime = System.currentTimeMillis();
		StatementInfo sti = stmap.get(id);
		sti.pmd = pmd;
		stmap.put(id, sti);
	}
	public ResultSet getResultSet(int id) {
		this.dateTime = System.currentTimeMillis();
		return rsmap.get(id).rs;
	}
	public ResultSetMetaData getResultSetMetaData(int id) {
		this.dateTime = System.currentTimeMillis();
		return rsmap.get(id).rsmd;
	}
	public void addResultSetMetaData(int id, ResultSetMetaData rsmd) {
		this.dateTime = System.currentTimeMillis();
		ResultSetInfo rsi = rsmap.get(id);
		rsi.rsmd = rsmd;
		rsmap.put(id, rsi);
	}
	public int addResultSet(ResultSet rs) {
		this.dateTime = System.currentTimeMillis();
		ir++;
		ResultSetInfo rsi = new ResultSetInfo();
		rsi.rs = rs;
		rsmap.put(ir, rsi);
		return ir;
	}

	private void check() throws SQLException {
		if (this.conn != null){
			if (System.currentTimeMillis()-this.dateTime >  MAXTIME){
				this.conn.close();
				new ConnectionSet().removeNioCOnnection(this.id);
			}
		}
	}

	private class StatementInfo{
		Statement st;
		ParameterMetaData pmd;
		StatementInfo() {
		}
	}
	private class ResultSetInfo{
		ResultSet rs;
		ResultSetMetaData rsmd;
		ResultSetInfo() {
		}
	}
}
