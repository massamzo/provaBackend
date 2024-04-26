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

import sessions.Redirections;

/**
 * Servlet implementation class GetTornei
 */
@WebServlet("/GetTornei")
public class GetTornei extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
	private final String[] typesOfRequests = {"m","d"}; // m = eventi di quel mese, d= eventi di quella data
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTornei() {
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
		 // reading the string sent in request body
		
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
		
		
		if(type_req.equals(typesOfRequests[0])) { // client is requesting the events in that month
			
			// get the month
			
			String month = jsonData.get("mese").getAsString();
			String anno = jsonData.get("anno").getAsString();
			Torneo torneo = null;
			
			
			try {
				
				
				torneo = new Torneo(sessionID);
				String jsonRisposta = torneo.getTorneoByMonth(month,anno);
				
				response.getWriter().append(jsonRisposta);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				String error = "{dates:false}";
				response.getWriter().append(error);
				e.printStackTrace();
			}
			
			
		}else if(type_req.equals(typesOfRequests[1])) {
			
			String data= jsonData.get("data").getAsString();
			
			Torneo torneo = null;
			
			try {
				
				
				torneo = new Torneo(sessionID);
				String jsonRisposta = torneo.getTorneiByData(data);
				response.getWriter().append(jsonRisposta);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String error = "{dates:false}";
				response.getWriter().append(error);
				e.printStackTrace();
			}
			//String jsonRisposta = 
			
			
		}
		
	}

}
