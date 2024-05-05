package invitation;

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
import mail.Mailer;
import sessions.Redirections;
import tornei.Torneo;

/**
 * Servlet implementation class InviteFriends
 */
@WebServlet("/InviteFriends")
public class InviteFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
	private final String[] typesOfRequests = {"i","g"}; // i = invite, g = get list
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InviteFriends() {
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
			
			// invite the person
			
			String data = jsonData.get("data").getAsString();
			String nome_evento = jsonData.get("nome_evento").getAsString();
			String orario = jsonData.get("orario").getAsString();
			String friend_email = jsonData.get("friend_email").getAsString(); 
			
			// check if the the user can partecipate
			Torneo torneo  = null;
			try {
				
				torneo = new Torneo(sessionID, data, nome_evento, orario);
				Torneo actualTorneo = torneo.getTorneo();
				
				User user = new User(friend_email, "");
				User friend = user.getUserFromdb();

				User inviter = torneo.getUserKnowingID();
				
				if(actualTorneo.canPartecipate(friend)) {
					
					// mail the friend
					
					String subject = "Invito ad un torneo";
					String link = redirect.getHOME_PAGE()+"?nome_evento="+actualTorneo.getNome_evento()+"&data_evento="+actualTorneo.getData()+"&orario="+actualTorneo.getOrario();
					String text = "Ciao "+friend.getNome()+",\nhai ricevuto un invito da "+inviter.getNome()+" "+inviter.getCognome()+".\nclicca sul link per vedere i dettagli del torneo e per partecipare : \n\n"+link;
				
					Mailer mail = new Mailer(friend.getEmail(), subject, text, false);
					mail.send();
					
					response.getWriter().append("true");
					
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				String error = "utente invitato non esiste";
				response.getWriter().append(error);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				String error = "utente invitato non puo partecipare a questo torneo";
				response.getWriter().append(error);
			}
			
			
			
			
			
		}else if(type_req.equals(typesOfRequests[1])) {
			
			// get the list of the people
			
			String input = jsonData.get("input").getAsString(); // letters
			
			String data = jsonData.get("data").getAsString();
			String nome_evento = jsonData.get("nome_evento").getAsString();
			String orario = jsonData.get("orario").getAsString();
			
			Torneo torneo = null;
			
			try {
				
				torneo = new Torneo(sessionID, data, nome_evento, orario);
				
				Torneo actualTor = torneo.getTorneo();
				
				User user = new User("trial@gmail.com","");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("users", new Gson().toJson(user.getUserList(actualTor.getIsInterno(), input)));
				
				String jsonRisposta = new Gson().toJson(jsonObject);
				
				response.getWriter().append(jsonRisposta);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String error = "{'error':'non trovato'}";
				response.getWriter().append(error);
			}
			
			
			
		}
		
		
		
		
	}

}
