package login;

import java.io.IOException;


import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databasePack.DbRegisterLogin;
import databasePack.User;
import sessions.Redirections;
import sessions.Sessionmanager;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setHeader("Access-Control-Allow-Origin", redirect.getCORS_ALLOWED()); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Session-ID");
	    response.setHeader("Access-Control-Allow-Credentials", "true"); 
	    
	    HttpSession session = request.getSession();
		
		
		String session_nome = (String) session.getAttribute("nome");
		
		if(session_nome != null) {
			response.sendRedirect(redirect.getHOME_PAGE());  
		}else {
			response.sendRedirect(redirect.getLOGIN_PAGE());  
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request,response);
		
		//login methods
		response.setHeader("Access-Control-Allow-Origin", redirect.getCORS_ALLOWED()); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Session-ID");
	    response.setHeader("Access-Control-Allow-Credentials", "true"); //
		
		
		HttpSession session = request.getSession();

		
		String session_nome = (String) session.getAttribute("nome");
		
		if(session_nome == null) {
			
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			email = email.toLowerCase();
			
			User utente = null;
			DbRegisterLogin database = null;
			try {
				utente = new User(email, password);
				database = new DbRegisterLogin();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			try {
				
				if(database.verifyUser(utente)) { // if the user has been authenticated
					
					// create the sessions and redirect him to home page
					
					session.setAttribute("nome", utente.getNome());
					session.setAttribute("cognome", utente.getCognome());
					session.setAttribute("email", utente.getEmail());
					
					String sessionId = session.getId();
//					
//					System.out.println("login key : ");
//					System.out.println(sessionId);
					// send session information to the sessioninfo servlet
				
					System.out.println("sono entrato qui");
					// cambiare con link vero frontend
					
					// saving the session
					
					Sessionmanager.sessionMap.put(sessionId, session);

					// Check if redirect attribute is set
					//response.getWriter().append("Session created");
					response.sendRedirect(redirect.getSESSION_INFO_SERVLET()+"?sessionID=retrieve"); 
					
					
					
				}else {
					String error="Username o password sbagliata ";
					response.sendRedirect(redirect.getLOGIN_PAGE()+"?error="+error);
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else {
			
			// cambiare con link vero frontend
			
			response.sendRedirect(redirect.getHOME_PAGE());  
			
		}
	}

}
