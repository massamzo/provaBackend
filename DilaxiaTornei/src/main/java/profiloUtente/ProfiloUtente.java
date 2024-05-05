package profiloUtente;

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

import databasePack.ResetPassDatabase;
import databasePack.User;
import mail.Mailer;
import sessions.Redirections;
import tornei.Torneo;

/**
 * Servlet implementation class ProfiloUtente
 */
@WebServlet("/ProfiloUtente")
public class ProfiloUtente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
	private final String[] typesOfRequests = {"p","",""}; // p = modifica password
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfiloUtente() {
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
			
			// modifica la password con la nuova
			
			String oldPassword = jsonData.get("oldPassword").getAsString();
			String newPassword = jsonData.get("newPassword").getAsString();
			
			Torneo tor = null;
			try {
				
				tor = new Torneo(sessionID);
				User user = tor.getUserKnowingID();
				
				// verify the password
				
				if(user.verifyPassword(oldPassword)) {
					
					// update password
					ResetPassDatabase db = new ResetPassDatabase(user.getEmail(), "");
					
					db.resetPassword(newPassword);
					
					
					//mandare la mail che hai aggiornato la password
					
					String subject = "Password aggiornata";
					String text = "Ciao "+user.getNome()+", \nla tua password e' stata aggiornata";
					
					Mailer mail = new Mailer(user.getEmail(), subject, text, false); // false means it's a normal mail 
					mail.send();
					
					response.getWriter().append("true");
					
				}else {
					
					// throw an exception
					
					throw new Exception("password vecchia sbagliata!");
				}
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String error = e.getMessage();
				response.getWriter().append(error);
			}
			
			
		}
		
	}

}
