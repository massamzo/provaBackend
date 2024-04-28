package sessions;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import databasePack.DbRegisterLogin;
import databasePack.User;
import sessions.Sessionmanager;

import javax.servlet.http.Cookie;

/**
 * Servlet implementation class Sessioninfo
 */
@WebServlet("/Sessioninfo")
public class Sessioninfo2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Redirections redirect = new Redirections();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Sessioninfo2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		response.setHeader("Access-Control-Allow-Origin", redirect.getCORS_ALLOWED()); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
	    response.setHeader("Access-Control-Allow-Credentials", "true"); 
	    
	    try {
	    //	System.out.println("sto eseguendo get");
	    	String sessionIdValue = request.getParameter("sessionID");
	    	HttpSession session = request.getSession();
	    	String sessionId = "";
		    
	    	if(sessionIdValue.equals("retrieve")) {
	    		
	    		sessionId = session.getId();
	    		String nome = (String) session.getAttribute("nome");
	    		
	    		if(nome != null) {
	    			response.sendRedirect(redirect.getHOME_PAGE()+"?sessionIDspysphare="+sessionId);
	    		}else {
	    			response.getWriter().append("null");
					response.sendRedirect(redirect.getLOGIN_SERVLET());
				 }
	    		
	    	//System.out.println("redirection erro");
	    		
	    	}else if(sessionIdValue != null) {

	    		
	    		boolean sessionFound = true;
	    		try {
	    			session = Sessionmanager.sessionMap.get(sessionIdValue);
	    		}catch(Exception e) {
	    			sessionFound = false;
	    			session = request.getSession(); // cosi mi dara null visto che non coincide con una sessione
	    		}
	    		
	    		System.out.println(Sessionmanager.sessionMap.size());
				
				String nome = (String) session.getAttribute("nome");
				String cognome = (String) session.getAttribute("cognome");
				String email = (String) session.getAttribute("email");
//				System.out.println("localeee");
//				System.out.println(nome);
//				System.out.println(cognome);
				
				
				  
				    
				   
				    
				 
				 
				 if(nome != null) {
					 
					 JsonObject jsonObject = new JsonObject();
					 jsonObject.addProperty("nome", nome);
					 jsonObject.addProperty("cognome", cognome);
					 jsonObject.addProperty("email", email);
					 
					 // in base al flag gli restituisco gli eventi
					 
					 User user = new User(email, "");
					 User userLoggato = user.getUserFromdb();
					 
					 ArrayList<String> eventi = userLoggato.getEventiConcessi();
					 
					 jsonObject.addProperty("eventi_disponibili", new Gson().toJson(eventi));
					 
					 DbRegisterLogin db = new DbRegisterLogin();
					 ArrayList<String> sport = db.getSports();
					 
					 jsonObject.addProperty("sport_disponibili", new Gson().toJson(sport));
					 
					 String jsonResponse = new Gson().toJson(jsonObject);
					 
					 System.out.println("risposta data");
					 response.getWriter().append(jsonResponse);
					
				 }else {
					 response.getWriter().append("errore nei sessioni"); 
					 response.sendRedirect(redirect.getLOGIN_PAGE());
				 
				 }
	    	}else {
	    		response.sendRedirect(redirect.getLOGIN_PAGE());
	    	}
	    }catch(Exception e) {
	    	response.sendRedirect(redirect.getLOGIN_PAGE());
	    	e.printStackTrace();
	    }
	   
	   
		
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);

		    
	}

}
