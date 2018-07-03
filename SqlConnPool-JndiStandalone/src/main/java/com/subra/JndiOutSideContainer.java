package com.subra;

// https://stackoverflow.com/questions/44093822/how-to-setup-mysql-connection-with-jndi-using-java-in-desktop-application
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class JndiOutSideContainer {

	public static void main(String[] args) throws NamingException, SQLException {
		InitialContextFactoryBuilder initialContextFactoryBuilder = new MyInitialContextFactoryBuilder();
		
		/* On startup phase of a webcontainer tomcat or any JEE container real or emulator jndi service 
		   will create the Initialcontext as done with the above call */ 
		
		NamingManager.setInitialContextFactoryBuilder(initialContextFactoryBuilder); 
		
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("jndi/mypractdb");
		 Connection connection = ds.getConnection();
         Statement statement = connection.createStatement();
         ResultSet rs = statement.executeQuery("select * from usr");
         while(rs.next()){
        	 System.out.println( rs.getInt(1) + ' ' + rs.getString("name") + '-' + rs.getString(3));
         }				
	}
}
//----------------
class MyInitialContextFactoryBuilder implements InitialContextFactoryBuilder{

	@Override
	public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
		return new MyInitialContextFactory();
	}
	
//-------------------
	class MyInitialContextFactory implements InitialContextFactory {

		@Override
		public Context getInitialContext(Hashtable<?, ?> environment)throws NamingException {
			return new MyInitialContext();
		}

	}
//---------------------	
	class MyInitialContext extends InitialContext{

		private Hashtable<String, DataSource> dataSources = new Hashtable<String, DataSource>();
		public MyInitialContext() throws NamingException {
			super();
		}
				
		@Override public Object lookup(String name) throws NamingException {
			if(dataSources.isEmpty()){
				BasicDataSource ds = new BasicDataSource();
				ds.setUrl("jdbc:mysql://localhost:3306/practdb");
	            ds.setUsername("sdass");
	            ds.setPassword("xxxxx");
	            ds.setDriverClassName("com.mysql.jdbc.Driver");
	            dataSources.put("jndi/mypractdb", ds);
			}
			//dataSources.entrySet().forEach(x-> {System.out.println("debug - -" + x.getKey() + " ::: " + x.getValue());});
			if(dataSources.containsKey(name)){
				return dataSources.get(name);
			}
			throw new NamingException("unable to find datascource: " + name);
		}
		
	}
}
