package databasePack;
import java.sql.SQLException;
import java.sql.Connection;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;


public class Database {
	
	public static final String percorso = "jdbc:mysql://localhost:3306/";
	public static final String username = "playsphere";
	public static final String password = "TorDil432@!";
	public static final String database = "playsphere";
	
	public String utentiTable = "utenti";
	public String tempUtentiTable = "temp_utenti";
	public String torneiTable = "tornei";
	public String sportsTable = "sports";
	public String partecipazioniTable = "partecipazioni";
	
	
	private Connection conn = null;
	BasicDataSource mysqldb = null;
	
	
	
	 public Database(){
		 
		// connection details
		mysqldb = new BasicDataSource();
		mysqldb.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    mysqldb.setUrl(Database.percorso+database);
	    mysqldb.setUsername(Database.username);
	    mysqldb.setPassword(Database.password);
		 
		
		
		try {
			conn = mysqldb.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR WHILE CONNECTING TO DATABASE");
			e.printStackTrace();
		}
	 }
	 
	 public Connection getConn() {
		 return conn;
	 }
	 
	 public void closeConnection() throws SQLException {
		 conn.close();
	 }

}
