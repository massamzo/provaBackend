package databasePack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class ResetPassDatabase extends Database {
	
	private Connection conn = null;
	private QueryManager qm;
	
	private String associated_email;
	private String passkey;
	
	public ResetPassDatabase(String associated_email, String passkey) throws SQLException {
		super();
		conn = getConn();
		qm = new QueryManager(conn);
		
		this.associated_email = associated_email;
		this.passkey = passkey;
	}
	
	
	public void insertPassKey() throws SQLException {
		String now = DbRegisterLogin.dateTime();
		
		Argon2 argon2 = Argon2Factory.create();
		String hashPass = argon2.hash(10, 63312, 1, passkey);
		
		QueryManager.INSERT_TEMP_PASS_STM.setString(1,associated_email);
		QueryManager.INSERT_TEMP_PASS_STM.setString(2,hashPass);
		QueryManager.INSERT_TEMP_PASS_STM.setString(3,now);
		
		QueryManager.INSERT_TEMP_PASS_STM.executeUpdate();
	}
	
	
	public String getAssEmail() throws SQLException {
		
		Argon2 argon2 = Argon2Factory.create();
		
		
		ResultSet rs = QueryManager.SELECT_TEMP_PASS_STM.executeQuery();
		
		while(rs.next()) {
			if(argon2.verify(rs.getString("passkey"), passkey)) {
				return rs.getString("email");
			}
		}
		
		
		throw new SQLException();
		
		

	}
	
	
	public void resetPassword(String newPass) throws SQLException {
		Argon2 argon2 = Argon2Factory.create();
		String hashPass = argon2.hash(10, 63312, 1, newPass); 
		
		QueryManager.UPDATE_PASS_STM.setString(1, hashPass);
		QueryManager.UPDATE_PASS_STM.setString(2, associated_email);
		
		QueryManager.UPDATE_PASS_STM.executeUpdate();
	}
	
	
	public void removeAllResetRequests() throws SQLException {
		
		QueryManager.DELETE_RESET_PASS_REQUEST_STM.setString(1, associated_email);
		QueryManager.DELETE_RESET_PASS_REQUEST_STM.executeUpdate();
	}

	public String getAssociated_email() {
		return associated_email;
	}


	public void setAssociated_email(String associated_email) {
		this.associated_email = associated_email;
	}


	public String getPasskey() {
		return passkey;
	}


	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}
	
	
	
}
