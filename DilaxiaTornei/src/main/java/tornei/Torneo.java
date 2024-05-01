package tornei;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import databasePack.Database;
import databasePack.QueryManager;
import databasePack.User;
import mail.Mailer;
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
	private String email_organizzatore;
	
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
	
	
	public Torneo(String sessionID, String data, String nome_evento, String orario, String descrizione,
			String numero_partecipanti_max, String eta_minima, String sport, String tipo_evento, String email_organizzatore) throws SQLException {
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
		this.email_organizzatore = email_organizzatore;
		
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
	
	public Torneo(String sessionID) throws SQLException {
		super();
		
		this.sessionID = sessionID;
		conn = getConn();
		qm = new QueryManager(conn);
	}


	public String getSessionID() {
		return sessionID;
	}


	public Boolean getIsInterno() {
		return isInterno;
	}

	public void setIsInterno(Boolean isInterno) {
		this.isInterno = isInterno;
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


	public String getEmail_organizzatore() {
		return email_organizzatore;
	}


	public void setEmail_organizzatore(String email_organizzatore) {
		this.email_organizzatore = email_organizzatore;
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
			
			
			return new Torneo("", datadb, rs.getString("nome_torneo"), ora, rs.getString("descrizione"), rs.getString("max_partecipanti"), rs.getString("eta_minima"), rs.getString("sport"), creaTipo, rs.getString("email_organizzatore"));
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
	
	
	
	
	public void abbandonaTorneo(String email) throws SQLException {
		
		QueryManager.DELETE_PARTECIPAZIONE_STM.setString(1, email);
		QueryManager.DELETE_PARTECIPAZIONE_STM.setString(2, this.nome_evento);
		QueryManager.DELETE_PARTECIPAZIONE_STM.setString(3, this.dateTime);
		
		QueryManager.DELETE_PARTECIPAZIONE_STM.executeUpdate();
	}
	
	
	
	private String getTorneoPerMeseEsterno(String mese, String anno, String email) throws SQLException {

		QueryManager.SELECT_SPECIFIC_TORNEO_MESE_STM.setString(1,mese);
		QueryManager.SELECT_SPECIFIC_TORNEO_MESE_STM.setString(2,anno);
		QueryManager.SELECT_SPECIFIC_TORNEO_MESE_STM.setBoolean(3,false); // devo vedere solo quelli esterni
		
		ResultSet rs = QueryManager.SELECT_SPECIFIC_TORNEO_MESE_STM.executeQuery();
		
		JsonObject jsonObject = new JsonObject();
		
		
		ArrayList<ArrayList<String>> dates = new ArrayList<>();
		
		while(rs.next()) {
			
			String date = rs.getString("data_torneo").split(" ")[0];
			
			ArrayList<String> singleArray = new ArrayList<>();
			
			singleArray.add(date);
			
			// enter the 1 = partecipa in quella data a qualche torneo, 0 = non pasrtecipa
			
			QueryManager.SELECT_PARTECIPAZIONI_STM.setString(1, email);
			QueryManager.SELECT_PARTECIPAZIONI_STM.setString(2, date);
			
			ResultSet rs2 = QueryManager.SELECT_PARTECIPAZIONI_STM.executeQuery();
			
			if(rs2.next()) {
				singleArray.add("1");
			}else {
				singleArray.add("0");
			}
			
			dates.add(singleArray);
		}
		
		String datesArray = new Gson().toJson(dates);
		jsonObject.addProperty("dates", datesArray);
		
		return new Gson().toJson(jsonObject);
	}
	
	
	private String getAllTorneoPerMese(String mese, String anno, String email) throws SQLException {
		
		QueryManager.SELECT_ALL_TORNEO_MESE_STM.setString(1,mese);
		QueryManager.SELECT_ALL_TORNEO_MESE_STM.setString(2,anno);

		ResultSet rs = QueryManager.SELECT_ALL_TORNEO_MESE_STM.executeQuery();
		
		JsonObject jsonObject = new JsonObject();
		
		
		ArrayList<ArrayList<String>> dates = new ArrayList<>();
		
		while(rs.next()) {
			
			String date = rs.getString("data_torneo").split(" ")[0];
			
			ArrayList<String> singleArray = new ArrayList<>();
			
			singleArray.add(date);
			
			// enter the 1 = partecipa in quella data a qualche torneo, 0 = non pasrtecipa
			
			QueryManager.SELECT_PARTECIPAZIONI_STM.setString(1, email);
			QueryManager.SELECT_PARTECIPAZIONI_STM.setString(2, date);
			
			ResultSet rs2 = QueryManager.SELECT_PARTECIPAZIONI_STM.executeQuery();
			
			if(rs2.next()) {
				singleArray.add("1");
			}else {
				singleArray.add("0");
			}
			
			dates.add(singleArray);
		}
		
		String datesArray = new Gson().toJson(dates);
		jsonObject.addProperty("dates", datesArray);
		
		return new Gson().toJson(jsonObject);
		
	}
	
	
	
	
	
	
	
	public String getTorneoByMonth(String mese,String anno) throws Exception {
		
		// get the type of user who is requesting the data
		
		User user = this.getUserKnowingID();
		
		// run different tasks based on the user
		
		if(user.getFlag().equals(user.getAvailable_flags().get("esterno"))) {
			
			// eventi da mandare se un utente è esterno
			
			return getTorneoPerMeseEsterno(mese,anno, user.getEmail());
			
		}else {
			
			return getAllTorneoPerMese(mese,anno, user.getEmail());
			
		}
		
		
	}
	
	
	
	
	private String getTorneiPerDataEsterno(String data, ArrayList<ArrayList<String>> arrayPartecipanti) throws SQLException {

		QueryManager.SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC_STM.setString(1, data);
		QueryManager.SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC_STM.setBoolean(2, false);
		
		ResultSet rs = QueryManager.SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC_STM.executeQuery();
		
		ArrayList<ArrayList<String>> eventi = new ArrayList<>();
		
		
		while(rs.next()) {
			
			ArrayList<String> single_evento = new ArrayList<>();
			
			single_evento.add(rs.getString("nome_torneo"));
			String orario = rs.getString("data_torneo").split(" ")[1];
			orario = orario.split(":")[0]+":"+orario.split(":")[1];
			single_evento.add(orario);
			
			QueryManager.SELECT_IS_INTERNO_STM.setString(1, rs.getString("data_torneo"));
			QueryManager.SELECT_IS_INTERNO_STM.setString(2, rs.getString("nome_torneo"));
			
			ResultSet rs2 = QueryManager.SELECT_IS_INTERNO_STM.executeQuery();
			
			if(rs2.next()) {
				if(rs2.getBoolean("is_interno")) {
					single_evento.add("1");
				}else {
					single_evento.add("0");
				}
			}else {
				throw new SQLException();
			}
			
			
			if(!arrayPartecipanti.contains(single_evento)) {
				eventi.add(single_evento);
			}
			
		}
		
		String nomiArray = new Gson().toJson(eventi);
		
		
		return nomiArray;
		
	}
	
	
	private String getTorneiPerDataAll(String data, ArrayList<ArrayList<String>> arrayPartecipanti) throws SQLException {
		
		
		QueryManager.SELECT_TORNEI_NON_PARTECIPANTI_ALL_STM.setString(1, data);
		
		ResultSet rs = QueryManager.SELECT_TORNEI_NON_PARTECIPANTI_ALL_STM.executeQuery();
		
		ArrayList<ArrayList<String>> eventi = new ArrayList<>();
		
		
		while(rs.next()) {
			
			ArrayList<String> single_evento = new ArrayList<>();
			
			single_evento.add(rs.getString("nome_torneo"));
			String orario = rs.getString("data_torneo").split(" ")[1];
			orario = orario.split(":")[0]+":"+orario.split(":")[1];
			single_evento.add(orario);
			
			QueryManager.SELECT_IS_INTERNO_STM.setString(1, rs.getString("data_torneo"));
			QueryManager.SELECT_IS_INTERNO_STM.setString(2, rs.getString("nome_torneo"));
			
			ResultSet rs2 = QueryManager.SELECT_IS_INTERNO_STM.executeQuery();
			if(rs2.next()) {
				if(rs2.getBoolean("is_interno")) {
					single_evento.add("1");
				}else {
					single_evento.add("0");
				}
			}else {
				throw new SQLException();
			}
			
			if(!arrayPartecipanti.contains(single_evento)) {
				eventi.add(single_evento);
			}
			
		}
		
		String nomiArray = new Gson().toJson(eventi);
		
		
		return nomiArray;
		
	}
	
	
	
	private ArrayList<ArrayList<String>> getTorneiPartecipanti(String data, String email) throws SQLException {
		
		QueryManager.SELECT_TORNEI_PARTECIPANTI_STM.setString(1, data);
		QueryManager.SELECT_TORNEI_PARTECIPANTI_STM.setString(2, email);
		
		ResultSet rs = QueryManager.SELECT_TORNEI_PARTECIPANTI_STM.executeQuery();
		
		ArrayList<ArrayList<String>> eventi = new ArrayList<>();
		
		while(rs.next()) {
			
			ArrayList<String> single_evento = new ArrayList<>();
			
			single_evento.add(rs.getString("nome_torneo"));
			String orario = rs.getString("data_torneo").split(" ")[1];
			orario = orario.split(":")[0]+":"+orario.split(":")[1];
			
			
			single_evento.add(orario);
			
			// is interno or not
			
			QueryManager.SELECT_IS_INTERNO_STM.setString(1, rs.getString("data_torneo"));
			QueryManager.SELECT_IS_INTERNO_STM.setString(2, rs.getString("nome_torneo"));
			
			ResultSet rs2 = QueryManager.SELECT_IS_INTERNO_STM.executeQuery();
			
			if(rs2.next()) {
				if(rs2.getBoolean("is_interno")) {
					single_evento.add("1");
				}else {
					single_evento.add("0");
				}
			}else {
				throw new SQLException();
			}
		
			eventi.add(single_evento);
		}
		
		
		
		return eventi;
		
		
	}
	
	
	public String getTorneiByData(String data) throws Exception {  // NP = non partecipanti
		
		// get the type of user who is requesting the data
		User user = this.getUserKnowingID();
		
		// get the tornei partecipanti dalla persona
		JsonObject jsonObject = new JsonObject();
		
		ArrayList<ArrayList<String>> arrayPartecipanti = getTorneiPartecipanti(data, user.getEmail());
		
		jsonObject.addProperty("partecipanti", new Gson().toJson(arrayPartecipanti)); // converto in json array
				
		if(user.getFlag().equals(user.getAvailable_flags().get("esterno"))) {
			
			// nomi da mandare se un utente è esterno (solo quelli esterni)
			
			jsonObject.addProperty("non_partecipanti",getTorneiPerDataEsterno(data, arrayPartecipanti));
			
		}else {
			// altrimenti mando tutti
			jsonObject.addProperty("non_partecipanti",getTorneiPerDataAll(data,arrayPartecipanti));
			
		}
		
		return new Gson().toJson(jsonObject);
		
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
	
	public void alertPartecipanti() throws SQLException {
		
		QueryManager.SELECT_PARTECIPANTI_STM.setString(1, this.nome_evento);
		QueryManager.SELECT_PARTECIPANTI_STM.setString(2, this.dateTime);
		
		ResultSet rs = QueryManager.SELECT_PARTECIPANTI_STM.executeQuery();
		
		ArrayList<String> recievers = new ArrayList<>();
		
		
		while(rs.next()) {
			
			recievers.add(rs.getString("email_partecipante"));
		}
		
	
		if(recievers.size() > 0) {
			

			String subject = "Torneo aggiornato";
			String text = "Ciao, ti avvisiamo che il torneo '"+this.nome_evento+"' e' stato modificato,\necco i dettagli : \n\ndata torneo : "+this.data+"\norario : "+this.orario+"\ndescrizione : "+this.descrizione+"\nnumero partecipanti massimi : "+this.numero_partecipanti_max+"\neta minima : "+this.eta_minima+"\nsport : "+this.sport;
			
			Mailer mail = new Mailer(recievers, subject, text);
			
			try {
				mail.sendMultiple();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		
	}
	
	
	
}
