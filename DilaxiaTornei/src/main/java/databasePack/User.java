package databasePack;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap; 

public class User extends Database{
	private final String PROF = "avbo.it";
	private final String STUDENTE = "aldini.istruzioneer.it";
	
	private final String[] admins = {"massam.zohaib@aldini.istruzioneer.it", "andrea.sapio@aldini.istruzioneer.it"};
	
	private Connection conn = null;
	private String email;
	private String nome;
	private String cognome;
	private String password;
	private String sesso;
	private String ddn;
	private String dataCreazione;
	private String flag;
	
	private HashMap<String, String> available_flags = new HashMap<>();
	
	private QueryManager qm;

	
	private boolean userExists;

	public User(String email, String nome, String cognome, String password, String sesso, String ddn) throws SQLException {
		super();
		
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.password = password;
		this.dataCreazione = null;
		this.sesso = sesso;
		this.ddn = ddn;
		
		
		
		available_flags.put("prof", "p");
		available_flags.put("studente", "s");
		available_flags.put("admin", "a");
		available_flags.put("esterno", "e");
		
		conn = getConn();
		qm = new QueryManager(conn);
		
		calculateFlag();
		
	}
	
	public User(String email, String password) throws SQLException {
		super();
		this.email = email;
		this.password = password;
		
		conn = getConn();
		qm = new QueryManager(conn);
		
		available_flags.put("prof", "p");
		available_flags.put("studente", "s");
		available_flags.put("admin", "a");
		available_flags.put("esterno", "e");
		
		calculateFlag();
		
	}

	
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isUserExists() {
		return userExists;
	}

	public void setUserExists(boolean userExists) {
		this.userExists = userExists;
	}
	
	
	
	public HashMap<String, String> getAvailable_flags() {
		return available_flags;
	}

	public void setAvailable_flags(HashMap<String, String> available_flags) {
		this.available_flags = available_flags;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getDdn() {
		return ddn;
	}

	public void setDdn(String ddn) {
		this.ddn = ddn;
	}

	public String getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public boolean userExists() throws SQLException {
		
//		 Connection conn = getConn();
		
		 
		// QueryManager.SELECT_USER_STM.setString(1,utentiTable);
		 QueryManager.SELECT_USER_STM.setString(1,this.email);
		
		
//		 Statement stmt = conn.createStatement();
//		 
//		 String query = "SELECT email_utente as email, nome, cognome, password FROM "+utentiTable+" WHERE email_utente='"+this.email+"'";
		 ResultSet rs =  QueryManager.SELECT_USER_STM.executeQuery();
		 
		 if(rs.next()) {
			 if(this.email.equals(rs.getString("email"))) {
				 return true;
			 }
		 }
		 
		 return false;
		
	}
	
	
	
	public User userWithCryptedPass() throws SQLException {
		
		
		
//		 Connection conn = getConn();
		
		//QueryManager.SELECT_USER_STM.setString(1,utentiTable);
		QueryManager.SELECT_USER_STM.setString(1,this.email);
		
		// Statement stmt = conn.createStatement();
		 
		// String query = "SELECT email_utente as email, nome, cognome, password, data_creazione FROM "+utentiTable+" WHERE email_utente = '"+this.email+"'";
		 ResultSet rs = QueryManager.SELECT_USER_STM.executeQuery();
		 
		 User newuser = null;
		 if(rs.next()) {
			 newuser = new User(rs.getString("email"), rs.getString("nome"), rs.getString("cognome"), rs.getString("password"), rs.getString("sesso"), rs.getString("data_nascita"));
			 newuser.setDataCreazione(rs.getString("data_creazione"));
			 return newuser;
		 }
		 
		 
		 return newuser;
		
	}
	
	
	private void calculateFlag() {
		
		String emailProvider = this.email.split("@")[1]; //ex.  @avbo.it
		boolean foundAdmin = false;
		
		for(int i=0; i < admins.length; i++) {
			
			if(admins[i].equals(this.email)) {
				foundAdmin = true;
				break;
			}
		}
		
		if(foundAdmin) {
			setFlag(available_flags.get("admin"));
		}else if(emailProvider.equals(PROF)){
			setFlag(available_flags.get("prof"));
		}else if(emailProvider.equals(STUDENTE)){
			setFlag(available_flags.get("studente"));
		}else {
			setFlag(available_flags.get("esterno"));
		}
		
		
	}
	
	public User getUserFromdb() throws SQLException {
		
		QueryManager.SELECT_USER_STM.setString(1, this.email);
		ResultSet rs = QueryManager.SELECT_USER_STM.executeQuery();
		
		if(rs.next()) {
			return new User(rs.getString("email"), rs.getString("nome"), rs.getString("cognome"), rs.getString("password"), rs.getString("sesso"), rs.getString("data_nascita"));
		}
		
		throw new SQLException();
	}
	
	
	
}
