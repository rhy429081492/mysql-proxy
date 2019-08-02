package com.yzf.proxy.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

import java.util.LinkedList;
import java.util.Queue;

/*
连接队列类
 */
public class Allocation extends Thread {
	private volatile Queue<NioConnection> queue = new LinkedList<NioConnection>();//连接队列
	private ComboPooledDataSource pool;//连接数据源
	public Allocation(ComboPooledDataSource pool) {
		this.pool = pool;
	}
	public synchronized void addNioConnection(NioConnection nio) {
		queue.add(nio);
	}
	
	@Override
	public void run() {
		super.run();
		while(true) {//循环检测
			if(!queue.isEmpty()) {//队列检测
				try {
					int i = ((PooledDataSource)pool).getNumIdleConnectionsDefaultUser();//数据源检测
					if(i>0) {
						NioConnection nio = queue.poll();
						nio.setConn(pool.getConnection());
						nio.latch.countDown();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
