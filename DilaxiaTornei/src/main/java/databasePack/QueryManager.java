package databasePack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryManager {
	private static final String INSERT_USER = "INSERT INTO utenti(email_utente, nome, cognome, password, data_nascita, sesso, data_creazione, privilegi_flg) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_TEMP_USER = "INSERT INTO temp_utenti(email_utente, nome, cognome, password, data_nascita, sesso, otp, privilegi_flg, expire_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_TEMP_PASS = "INSERT INTO temp_pass_recover(email_utente, passkey, expire_at) VALUES(?, ?, ?)";
	//insert nelle tabelle: partecipazioni, tornei, sports
	private static final String INSERT_PARTECIPAZIONE = "INSERT INTO partecipazioni(email_partecipante, nome_torneo, data_torneo) VALUES(?, ?, ?)";
	private static final String INSERT_TORNEO = "INSERT INTO tornei(nome_torneo, data_torneo, descrizione, eta_minima, min_partecipanti, max_partecipanti, is_interno, email_organizzatore, sport) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_SPORT = "INSERT INTO sports(nome) VALUES(?)";
	
	private static final String SELECT_TEMP_USER = "SELECT email_utente as email, nome, cognome, password, otp, sesso, data_nascita, expire_at FROM temp_utenti WHERE email_utente = ?";
	private static final String SELECT_USER = "SELECT email_utente as email, nome, cognome, password, sesso, data_nascita, data_creazione FROM utenti WHERE email_utente = ?";
	private static final String SELECT_TEMP_PASS = "SELECT email_utente as email FROM temp_pass_recover WHERE passkey = ?";
	//select dalle tabelle: partecipazioni, tornei, sports
	private static final String SELECT_PARTECIPAZIONI = "SELECT email_partecipante FROM partecipazioni WHERE (email_partecipante=? AND DATE(data_torneo)=?)";
	private static final String SELECT_TORNEO = "SELECT nome_torneo, data_torneo, descrizione, eta_minima, min_partecipanti, max_partecipanti, is_interno, email_organizzatore, sport FROM tornei WHERE (nome_torneo=? AND data_torneo=?)";
	private static final String SELECT_SPORTS = "SELECT nome FROM sports";
	private static final String SELECT_TORNEO_BY_DATE = "SELECT nome_torneo FROM tornei WHERE DATE(data_torneo) = ? AND nome_torneo = ?";
	private static final String SELECT_ALL_TORNEO_MESE = "SELECT DISTINCT DATE(data_torneo) as data_torneo FROM tornei WHERE MONTH(data_torneo) = ? AND YEAR(data_torneo) = ?";
	private static final String SELECT_SPECIFIC_TORNEO_MESE = "SELECT DISTINCT DATE(data_torneo) as data_torneo FROM tornei WHERE MONTH(data_torneo) = ? AND YEAR(data_torneo) = ? AND is_interno = ?";
	private static final String SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC = "SELECT nome_torneo, data_torneo FROM tornei WHERE (DATE(tornei.data_torneo) = ? AND tornei.is_interno = ?)";
	private static final String SELECT_TORNEI_NON_PARTECIPANTI_ALL = "SELECT nome_torneo, data_torneo FROM tornei WHERE DATE(tornei.data_torneo) = ?";
	private static final String SELECT_TORNEI_PARTECIPANTI = "SELECT nome_torneo, data_torneo FROM partecipazioni WHERE DATE(data_torneo)=? AND email_partecipante=?";
	private static final String SELECT_EVENTI_CONCESSI = "SELECT evento FROM privilegi WHERE utente = ?";
	private static final String SELECT_IS_INTERNO = "SELECT is_interno FROM tornei WHERE data_torneo = ? AND nome_torneo = ?";
	private static final String SELECT_PARTECIPANTI = "SELECT email_partecipante FROM partecipazioni WHERE (nome_torneo = ? AND data_torneo = ?)";
	
	
	private static final String UPDATE_PASS = "UPDATE utenti SET password = ? WHERE email_utente = ?";
	//update delle tabelle: partecipazioni, tornei, sports
	private static final String UPDATE_PARTECIPAZIONE = "UPDATE partecipazioni SET ? = ? WHERE (email_partecipante=? AND nome_torneo=? AND data_torneo=?)";
	private static final String UPDATE_TORNEO = "UPDATE tornei SET descrizione = ?, eta_minima = ?, max_partecipanti = ?, sport = ? WHERE (nome_torneo=? AND data_torneo=?)";
	private static final String UPDATE_SPORT = "UPDATE sports SET nome=? WHERE nome=?";
	
	private static final String DELETE_RESET_PASS_REQUEST = "DELETE FROM temp_pass_recover WHERE email_utente = ?";
	//delete nelle tabelle: partecipazioni, tornei, sports
	private static final String DELETE_PARTECIPAZIONE = "DELETE FROM partecipazioni WHERE (email_partecipante=? AND nome_torneo=? AND data_torneo=?)";
	private static final String DELETE_TORNEO = "DELETE FROM tornei WHERE (nome_torneo=? AND data_torneo=? AND email_organizzatore=?)";
	private static final String DELETE_SPORT = "DELETE FROM sports WHERE nome = ?";
	
	private static final String COUNT_PARTECIPAZIONI = "SELECT COUNT(email_partecipante) as num_iscritti FROM partecipazioni WHERE (nome_torneo=? AND data_torneo=?)";
	
	public static PreparedStatement INSERT_USER_STM;
	public static PreparedStatement INSERT_TEMP_USER_STM;
	public static PreparedStatement SELECT_USER_STM;
	public static PreparedStatement SELECT_TEMP_USER_STM;
	public static PreparedStatement INSERT_TEMP_PASS_STM;
	public static PreparedStatement SELECT_TEMP_PASS_STM;
	public static PreparedStatement UPDATE_PASS_STM;
	public static PreparedStatement DELETE_RESET_PASS_REQUEST_STM;
	public static PreparedStatement INSERT_PARTECIPAZIONE_STM;
	public static PreparedStatement INSERT_TORNEO_STM;
	public static PreparedStatement SELECT_TORNEO_BY_DATE_STM;
	public static PreparedStatement DELETE_TORNEO_STM;
	public static PreparedStatement SELECT_TORNEO_STM;
	public static PreparedStatement UPDATE_TORNEO_STM;
	public static PreparedStatement COUNT_PARTECIPAZIONI_STM;
	public static PreparedStatement DELETE_PARTECIPAZIONE_STM;
	public static PreparedStatement SELECT_ALL_TORNEO_MESE_STM;
	public static PreparedStatement SELECT_SPECIFIC_TORNEO_MESE_STM;
	public static PreparedStatement SELECT_TORNEI_NON_PARTECIPANTI_ALL_STM;
	public static PreparedStatement SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC_STM;
	public static PreparedStatement SELECT_TORNEI_PARTECIPANTI_STM;
	public static PreparedStatement SELECT_EVENTI_CONCESSI_STM;
	public static PreparedStatement SELECT_SPORTS_STM;
	public static PreparedStatement SELECT_IS_INTERNO_STM;
	public static PreparedStatement SELECT_PARTECIPAZIONI_STM;
	public static PreparedStatement SELECT_PARTECIPANTI_STM;
	
	public QueryManager(Connection conn) throws SQLException {
		INSERT_USER_STM = conn.prepareStatement(INSERT_USER);
		INSERT_TEMP_USER_STM = conn.prepareStatement(INSERT_TEMP_USER);
		SELECT_USER_STM = conn.prepareStatement(SELECT_USER);
		SELECT_TEMP_USER_STM = conn.prepareStatement(SELECT_TEMP_USER);
		INSERT_TEMP_PASS_STM = conn.prepareStatement(INSERT_TEMP_PASS);
		INSERT_TORNEO_STM = conn.prepareStatement(INSERT_TORNEO);
		INSERT_PARTECIPAZIONE_STM = conn.prepareStatement(INSERT_PARTECIPAZIONE);
		SELECT_TEMP_PASS_STM = conn.prepareStatement(SELECT_TEMP_PASS);
		UPDATE_PASS_STM = conn.prepareStatement(UPDATE_PASS);
		DELETE_RESET_PASS_REQUEST_STM = conn.prepareStatement(DELETE_RESET_PASS_REQUEST);
		SELECT_TORNEO_BY_DATE_STM =  conn.prepareStatement(SELECT_TORNEO_BY_DATE);
		DELETE_TORNEO_STM = conn.prepareStatement(DELETE_TORNEO);
		SELECT_TORNEO_STM = conn.prepareStatement(SELECT_TORNEO);
		UPDATE_TORNEO_STM = conn.prepareStatement(UPDATE_TORNEO);
		COUNT_PARTECIPAZIONI_STM = conn.prepareStatement(COUNT_PARTECIPAZIONI);
		DELETE_PARTECIPAZIONE_STM = conn.prepareStatement(DELETE_PARTECIPAZIONE);
		SELECT_ALL_TORNEO_MESE_STM = conn.prepareStatement(SELECT_ALL_TORNEO_MESE);
		SELECT_SPECIFIC_TORNEO_MESE_STM = conn.prepareStatement(SELECT_SPECIFIC_TORNEO_MESE);
		SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC_STM = conn.prepareStatement(SELECT_TORNEI_NON_PARTECIPANTI_SPECIFIC);
		SELECT_TORNEI_NON_PARTECIPANTI_ALL_STM = conn.prepareStatement(SELECT_TORNEI_NON_PARTECIPANTI_ALL);
		SELECT_TORNEI_PARTECIPANTI_STM = conn.prepareStatement(SELECT_TORNEI_PARTECIPANTI);
		SELECT_EVENTI_CONCESSI_STM = conn.prepareStatement(SELECT_EVENTI_CONCESSI);
		SELECT_SPORTS_STM = conn.prepareStatement(SELECT_SPORTS);
		SELECT_IS_INTERNO_STM = conn.prepareStatement(SELECT_IS_INTERNO);
		SELECT_PARTECIPAZIONI_STM = conn.prepareStatement(SELECT_PARTECIPAZIONI);
		SELECT_PARTECIPANTI_STM = conn.prepareStatement(SELECT_PARTECIPANTI);
		
		
	}
}