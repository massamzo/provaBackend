package tornei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import databasePack.QueryManager;
import databasePack.User;
import sessions.Redirections;

/**
 * Servlet implementation class TorneiManager
 */
@WebServlet("/TorneiManager")
public class TorneiManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
	
	private final String[] typesOfRequests = {"c","d","u"};
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TorneiManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		
		String session_nome = (String) session.getAttribute("nome");
		
		if(session_nome != null) {
			response.sendRedirect(redirect.getHOME_PAGE());  
		}else {
			response.sendRedirect(redirect.getLOGIN_PAGE());  // register page
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// i will get the details
		response.setHeader("Access-Control-Allow-Origin", redirect.getCORS_ALLOWED()); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
	    response.setHeader("Access-Control-Allow-Credentials", "true"); 
	    
	    // reading the string sent in request body
	    BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        
        // building the string reading line by line the body
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        
        // Parse the JSON data
        Gson gson = new Gson();
        JsonObject jsonData = gson.fromJson(requestBody.toString(), JsonObject.class);

        // Access the JSON data
     
		
		String sessionID = jsonData.get("sessionId").getAsString();
		String type_req = jsonData.get("type_req").getAsString();
		
		
		
		
		// gestisco i dati se la richiesta è di creazione
		if(type_req.equals(typesOfRequests[0])) {
			
			
			String data = jsonData.get("data").getAsString();
			String nome_evento = jsonData.get("nome_evento").getAsString();
			String orario = jsonData.get("orario").getAsString();
			String descrizione = jsonData.get("descrizione").getAsString();
			String numero_partecipanti_max = jsonData.get("numero_partecipanti_max").getAsString();
			String eta_minima = jsonData.get("eta_minima").getAsString();
			String sport = jsonData.get("sport").getAsString();
			String tipo_evento = jsonData.get("tipo_evento").getAsString();
			
			
			
			//check if the person is creating the torneo not in the past
			
			Torneo torneo = null;
			try {
				torneo = new Torneo(sessionID, data,nome_evento, orario, descrizione, numero_partecipanti_max, eta_minima,sport, tipo_evento);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(torneo.canCreateTorneo()) { // controlla le date
				
				try {
					
					torneo.createTorneo();
					
					User utente = torneo.getUserKnowingID();
					
					// utente che crea il torneo si iscrive anche
					torneo.iscriviAlTorneo(utente.getEmail());
					
					// se tutto ok, allora mando valore esecuzione
					response.getWriter().append("true");
					
				}catch(SQLException e) {
					
					String error = "torneo esiste per quella data";
					response.getWriter().append(error);
					e.printStackTrace();
					
					
				}catch(Exception e) {
					
					String error = e.getMessage();
					response.getWriter().append(error);
					e.printStackTrace();
				}
				
			}else {
				String error = "hai scelto una data o un orario in passato";
				response.getWriter().append(error);
			}
			
			
			
		}else if(type_req.equals(typesOfRequests[1])) {  // if the user wants to delete it
			
			String data = jsonData.get("data").getAsString();
			String nome_evento = jsonData.get("nome_evento").getAsString();
			String orario = jsonData.get("orario").getAsString();
			
			
			Torneo torneo = null;
			
			try {
				
				torneo = new Torneo(sessionID, data, nome_evento, orario);
				torneo.delete();
				response.getWriter().append("true");
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				String error = e.getMessage();
				response.getWriter().append(error);
				e.printStackTrace();
				
			}catch (Exception e) {
				// TODO Auto-generated catch block
				String error = "torneo non eliminato";
				response.getWriter().append(error);
				e.printStackTrace();
			}
			
		}else if(type_req.equals(typesOfRequests[2])) { // if the user want's to update it
			
			String data = jsonData.get("data").getAsString();
			String nome_evento = jsonData.get("nome_evento").getAsString();
			String orario = jsonData.get("orario").getAsString();
			String descrizione = jsonData.get("descrizione").getAsString();
			String numero_partecipanti_max = jsonData.get("numero_partecipanti_max").getAsString();
			String eta_minima = jsonData.get("eta_minima").getAsString();
			String sport = jsonData.get("sport").getAsString();
			
			Torneo torneo = null;
			try {
				torneo = new Torneo(sessionID, data,nome_evento, orario, descrizione, numero_partecipanti_max, eta_minima,sport, "");
				torneo.update();
				
				response.getWriter().append("true");
				torneo.alertPartecipanti();
				
				//ottieni tutti i risultati
				
			
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				String error = e.getMessage();
				response.getWriter().append(error);
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				String error = e.getMessage();
				response.getWriter().append(error);
				e.printStackTrace();
				
			}
			
		}
		
		
	}

}
