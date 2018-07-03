package com.subra;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

// https://tomcat.apache.org/tomcat-8.0-doc/jdbc-pool.html

public class MysqlJdbcPool {


	
	public static void main(String[] args){
		System.out.println("this is test");
		mysqlConnect();
		
	}
	

	public static void mysqlConnect(){
		
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://localhost:3306/practdb");
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername("sdass");
		p.setPassword("xxxx");
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);
        
        Connection con = null;
        		
        		try {
					con = datasource.getConnection();
		     		Statement st = con.createStatement();
		     		ResultSet rs = st.executeQuery("select * from usr");
		     		int cnt = 1;
		     		while(rs.next()){
		     			System.out.println((cnt++) + " --> " + rs.getInt(1) + ' ' + rs.getString("name") + ':' + rs.getString(3));
		     		}
		     		rs.close();
		     		st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if(con != null){
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}		
	}
}


