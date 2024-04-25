package partecipazioni;

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

import databasePack.User;
import sessions.Redirections;
import tornei.Torneo;

/**
 * Servlet implementation class Partecipazioni
 */
@WebServlet("/Partecipazioni")
public class Partecipazioni extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
	private final String[] typesOfRequests = {"i","ed","u"};
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Partecipazioni() {
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
		
		if(type_req.equals(typesOfRequests[0])) {
			
			String data = jsonData.get("data").getAsString();
			String nome_evento = jsonData.get("nome_evento").getAsString();
			String orario = jsonData.get("orario").getAsString();
			
			Torneo torneo = null;
			
			try {
				
				torneo = new Torneo(sessionID, data, nome_evento, orario);
				Torneo torneoDaIscreversi = torneo.getTorneo();
				
				User userDaIscrivere = torneo.getUserKnowingID(); // prendo l'utente utilizzando sessionID
				
				torneoDaIscreversi.iscriviAlTorneo(userDaIscrivere.getEmail());
				
				response.getWriter().append("true");
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String error = "iscrizione non effettuata";
				response.getWriter().append(error);
				e.printStackTrace();
			}
		}
	}

}
