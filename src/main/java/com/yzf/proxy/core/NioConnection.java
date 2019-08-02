package com.yzf.proxy.core;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class NioConnection {//一个服务器镜像
	public CountDownLatch latch = new CountDownLatch(1);//队列锁
	private int id;//镜像序号，客户端访问标识
	private Connection conn = null;//唯一的Connection
	private DatabaseMetaData dbmd = null;//唯一的数据库元数据
	private Map<Integer, StatementInfo> stmap = new HashMap<Integer, StatementInfo>();
	//多个Statement，特殊的PreparedStatement拥有ParameterMetaData
	private int is = 0;//Statement ID
	private Map<Integer, ResultSetInfo> rsmap = new HashMap<Integer, ResultSetInfo>();
	private int ir = 0;//ResultSet ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public DatabaseMetaData getDbmd() {
		return dbmd;
	}
	public void setDbmd(DatabaseMetaData dbmd) {
		this.dbmd = dbmd;
	}
	
	public int addStatement(Statement st) {
		is++;
		StatementInfo sti = new StatementInfo();
		sti.st = st;
		stmap.put(is, sti);
		return is;
	}
	public Statement getStatement(int id) {
		return stmap.get(id).st;
	}
	public PreparedStatement getPreparedStatement(int id) {
		return (PreparedStatement) stmap.get(id).st;
	}
	public CallableStatement getCallableStatement(int id) {
		return (CallableStatement) stmap.get(id).st;
	}
	public ParameterMetaData getParameterMetaData(int id) {
		return stmap.get(id).pmd;
	}
	public void addParameterMetaData(int id, ParameterMetaData pmd) {
		StatementInfo sti = stmap.get(id);
		sti.pmd = pmd;
		stmap.put(id, sti);
	}
	public ResultSet getResultSet(int id) {
		return rsmap.get(id).rs;
	}
	public ResultSetMetaData getResultSetMetaData(int id) {
		return rsmap.get(id).rsmd;
	}
	public void addResultSetMetaData(int id, ResultSetMetaData rsmd) {
		ResultSetInfo rsi = rsmap.get(id);
		rsi.rsmd = rsmd;
		rsmap.put(id, rsi);
	}
	public int addResultSet(ResultSet rs) {
		ir++;
		ResultSetInfo rsi = new ResultSetInfo();
		rsi.rs = rs;
		rsmap.put(ir, rsi);
		return ir;
	}
	private class StatementInfo{
		Statement st;
		ParameterMetaData pmd;
		public StatementInfo() {
		}
	}
	private class ResultSetInfo{
		ResultSet rs;
		ResultSetMetaData rsmd;
		public ResultSetInfo() {
		}
	}
}
