package com.yzf.proxy.core;

public class ConnectionInfo {
	public String database;
	public String user;
	public String password;
	
	@Override
	public int hashCode() {
		return database.hashCode()+user.hashCode()+password.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		ConnectionInfo info = (ConnectionInfo) obj;
		if(info.database.equals(database)&&
				info.user.equals(user)&&
				info.password.equals(password)) {
			return true;
		}
		return false;
	}
}
