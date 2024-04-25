package tornei;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import databasePack.Database;
import databasePack.QueryManager;
import databasePack.User;
import sessions.Sessionmanager;

public class Torneo extends Database{
	
	private String sessionID;
	private String data;
	private String nome_evento;
	private String orario;
	private String descrizione;
	private int numero_partecipanti_max;
	private int eta_minima;
	private String sport;
	private String tipo_evento;
	private Boolean isInterno = false;
	private String dateTime;
	
	private Connection conn = null;
	private QueryManager qm;
	
	
	public Torneo(String sessionID, String data, String nome_evento, String orario, String descrizione,
			String numero_partecipanti_max, String eta_minima, String sport, String tipo_evento) throws SQLException {
		super();
		this.sessionID = sessionID;
		this.data = data;
		this.nome_evento = nome_evento;
		this.orario = orario;
		this.descrizione = descrizione;
		this.numero_partecipanti_max = Integer.parseInt(numero_partecipanti_max);
		this.eta_minima = Integer.parseInt(eta_minima);
		this.sport = sport;
		this.tipo_evento = tipo_evento;
		
		if(this.tipo_evento.equals("Crea interno")) {
			this.isInterno = true;
		}
		
		this.dateTime = this.data+" "+this.orario+":00";
		
		conn = getConn();
		qm = new QueryManager(conn);
		
	}
	
	public Torneo(String sessionID, String data, String nome_evento, String orario) throws SQLException {
		super();
		
		this.sessionID = sessionID;
		this.data = data;
		this.nome_evento = nome_evento;
		this.orario = orario;
		this.dateTime = this.data+" "+this.orario+":00";
		
		
		conn = getConn();
		qm = new QueryManager(conn);
		
		
	}


	public String getSessionID() {
		return sessionID;
	}


	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getNome_evento() {
		return nome_evento;
	}


	public void setNome_evento(String nome_evento) {
		this.nome_evento = nome_evento;
	}


	public String getOrario() {
		return orario;
	}


	public void setOrario(String orario) {
		this.orario = orario;
	}


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public int getNumero_partecipanti_max() {
		return numero_partecipanti_max;
	}


	public void setNumero_partecipanti_max(int numero_partecipanti_max) {
		this.numero_partecipanti_max = numero_partecipanti_max;
	}


	public int getEta_minima() {
		return eta_minima;
	}


	public void setEta_minima(int eta_minima) {
		this.eta_minima = eta_minima;
	}


	public String getSport() {
		return sport;
	}


	public void setSport(String sport) {
		this.sport = sport;
	}


	public String getTipo_evento() {
		return tipo_evento;
	}


	public void setTipo_evento(String tipo_evento) {
		this.tipo_evento = tipo_evento;
	}
	
	
	
	public boolean canCreateTorneo() {
		
		
		
		LocalDateTime now = LocalDateTime.now();
		String[] dataLoc = this.data.split("-");
		String[] timeLoc = this.orario.split(":");
        LocalDateTime torneiDateTime = LocalDateTime.of(Integer.parseInt(dataLoc[0]), Integer.parseInt(dataLoc[1]), Integer.parseInt(dataLoc[2]),  Integer.parseInt(timeLoc[0])-4, Integer.parseInt(timeLoc[1]));
        
        
        
        if(torneiDateTime.isBefore(now)) {
        	return false;
        }
        
        return true;
	
	}
	
	
	public User getUserKnowingID() throws Exception {
		
		try {
			
			HttpSession session = Sessionmanager.sessionMap.get(sessionID);
			String email = (String) session.getAttribute("email");
			User sessionUser = new User(email, "");
			
			return sessionUser.getUserFromdb();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		throw new Exception("not getting the user from the database");
		
	}
	
	public void createTorneo() throws Exception {
		
		User userfromDB = getUserKnowingID();
		boolean canCreate = true;
		
		if((this.isInterno && !userfromDB.getFlag().equals("p")) && (this.isInterno && !userfromDB.getFlag().equals("a"))) {
			
			canCreate = false;
			throw new Exception("non puoi creare un tornei interno");
		}
		
		//check if a torneo with the same name exists in that date
		
		QueryManager.SELECT_TORNEO_BY_DATE_STM.setString(1, this.data);
		QueryManager.SELECT_TORNEO_BY_DATE_STM.setString(2, this.nome_evento);
		
		ResultSet rs = QueryManager.SELECT_TORNEO_BY_DATE_STM.executeQuery();
		
		if(rs.next()) {
			throw new SQLException("torneo esiste per quella data");
		}
		
		
		
		QueryManager.INSERT_TORNEO_STM.setString(1, this.nome_evento);
		QueryManager.INSERT_TORNEO_STM.setString(2, this.dateTime);
		QueryManager.INSERT_TORNEO_STM.setString(3, this.descrizione);
		QueryManager.INSERT_TORNEO_STM.setInt(4, this.eta_minima);
		QueryManager.INSERT_TORNEO_STM.setInt(5, 0); // minimo numero di partecipanti
		QueryManager.INSERT_TORNEO_STM.setInt(6, this.numero_partecipanti_max);
		QueryManager.INSERT_TORNEO_STM.setBoolean(7, this.isInterno);
		QueryManager.INSERT_TORNEO_STM.setString(8, userfromDB.getEmail()); // email organizzatore
		QueryManager.INSERT_TORNEO_STM.setString(9, this.sport);
		
		// execute the query and insert into database
		
		QueryManager.INSERT_TORNEO_STM.executeUpdate();
		
		
		
		
	}
	
	
	public void delete() throws Exception {
		
		User userfromDB = getUserKnowingID();
		
		QueryManager.SELECT_TORNEO_STM.setString(1, this.nome_evento);
		QueryManager.SELECT_TORNEO_STM.setString(2, this.dateTime);
		
		ResultSet rs = QueryManager.SELECT_TORNEO_STM.executeQuery();
		
		if(rs.next()) {
			if(rs.getString("email_organizzatore").equals(userfromDB.getEmail())) {
				
				// proceed with deleting
				
				QueryManager.DELETE_TORNEO_STM.setString(1, this.nome_evento);
				QueryManager.DELETE_TORNEO_STM.setString(2, this.dateTime);
				QueryManager.DELETE_TORNEO_STM.setString(3, userfromDB.getEmail());
				
				QueryManager.DELETE_TORNEO_STM.executeUpdate();
			}else {
				throw new SQLException("non puoi eliminare questo torneo");
			}
		}
		
		
		
	}
	
	
	public void update() throws Exception {
		
		User userfromDB = getUserKnowingID();
		
		QueryManager.SELECT_TORNEO_STM.setString(1, this.nome_evento);
		QueryManager.SELECT_TORNEO_STM.setString(2, this.dateTime);
		
		ResultSet rs = QueryManager.SELECT_TORNEO_STM.executeQuery();
		
		if(rs.next()) {
			if(rs.getString("email_organizzatore").equals(userfromDB.getEmail())) {
				
				// proceed with deleting
				
				QueryManager.UPDATE_TORNEO_STM.setString(1, this.descrizione);
				QueryManager.UPDATE_TORNEO_STM.setInt(2, this.eta_minima);
				QueryManager.UPDATE_TORNEO_STM.setInt(3, this.numero_partecipanti_max);
				QueryManager.UPDATE_TORNEO_STM.setString(4, this.sport);
				QueryManager.UPDATE_TORNEO_STM.setString(5, this.nome_evento);
				QueryManager.UPDATE_TORNEO_STM.setString(6, this.dateTime);
				
				QueryManager.UPDATE_TORNEO_STM.executeUpdate();
				
			}else {
				throw new SQLException("modifica non effettuata");
			}
		}else {
			throw new SQLException("modifica non effettuata");
		}
	}
	
	
	public Torneo getTorneo() throws SQLException {
		
		QueryManager.SELECT_TORNEO_STM.setString(1, this.nome_evento);
		QueryManager.SELECT_TORNEO_STM.setString(2, this.dateTime);
		
		ResultSet rs = QueryManager.SELECT_TORNEO_STM.executeQuery();
		
		if(rs.next()) {
			
			String ora = rs.getString("data_torneo").split(" ")[1];
			String datadb = rs.getString("data_torneo").split(" ")[0];
			
			ora = ora.split(":")[0]+":"+ora.split(":")[1];
			
			String creaTipo = "";
			
			if(rs.getBoolean("is_interno")) {
				
				creaTipo = "Crea interno";
			}else {
				creaTipo = "Crea esterno";
			}
			
			
			return new Torneo("", datadb, rs.getString("nome_torneo"), ora, rs.getString("descrizione"), rs.getString("max_partecipanti"), rs.getString("eta_minima"), rs.getString("sport"), creaTipo);
		}
		
		throw new SQLException("torneo non trovato");
		
	}
	
	
	
	public int getPartecipantiNum() throws Exception {
		
		QueryManager.COUNT_PARTECIPAZIONI_STM.setString(1, this.nome_evento);
		QueryManager.COUNT_PARTECIPAZIONI_STM.setString(2, this.dateTime);
		
		ResultSet rs = QueryManager.COUNT_PARTECIPAZIONI_STM.executeQuery();
		
		if(rs.next()) {
			return Integer.parseInt(rs.getString("num_iscritti"));
		}
		
		return 0;
	}
	
	
	
	public void iscriviAlTorneo(String email) throws Exception {
		
		if(getPartecipantiNum() < this.numero_partecipanti_max) {
			QueryManager.INSERT_PARTECIPAZIONE_STM.setString(1, email);
			QueryManager.INSERT_PARTECIPAZIONE_STM.setString(2, this.nome_evento);
			QueryManager.INSERT_PARTECIPAZIONE_STM.setString(3, this.dateTime);
			
			QueryManager.INSERT_PARTECIPAZIONE_STM.executeUpdate();
		}else {
			
			throw new Exception("torneo pieno");
		}
		
		
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
	
	
	
	
	
}
