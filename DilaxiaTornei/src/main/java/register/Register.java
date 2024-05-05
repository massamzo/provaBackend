package register;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databasePack.DbRegisterLogin;
import databasePack.User;
import mail.Mailer;
import sessions.Redirections;
/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private static final int code_length = 5;
    private static final String codes = "01A2BCD3456EFGH78IJK9";
    
    Redirections redirect = new Redirections();
    
    private String error;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    private String generateOTP() {
    	
    	char[] code = new char[code_length];
    	Random rn = new Random();
    	for(int i=0; i < code_length; i++) {
    		code[i] = codes.charAt(rn.nextInt(code.length));
    	}
    	
    	return new String(code);
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		

		response.setHeader("Access-Control-Allow-Origin", redirect.getCORS_ALLOWED()); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Session-ID");
	    response.setHeader("Access-Control-Allow-Credentials", "true"); 
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		HttpSession session = request.getSession();
		
		
		String session_nome = (String) session.getAttribute("nome");
		
		if(session_nome != null) {
			response.sendRedirect(redirect.getHOME_PAGE());  
		}else {
			response.sendRedirect(redirect.getREGISTRATION_PAGE());  // register page
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setHeader("Access-Control-Allow-Origin", redirect.getCORS_ALLOWED()); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Session-ID");
	    response.setHeader("Access-Control-Allow-Credentials", "true"); 
	     
	     
		
		HttpSession session = request.getSession();
		
//		String sessionId = session.getId();
//
//		String cookieValue = String.format("JSESSIONID=%s; Path=/DilaxiaTornei; Domain=127.0.0.1:5500; Secure; HttpOnly; SameSite=None", sessionId);
//
//		response.addHeader("Set-Cookie", cookieValue);

		
		String session_nome = (String) session.getAttribute("nome");
		
		if(session_nome == null) {
			
			String email = request.getParameter("email");
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String password = request.getParameter("password");
			String sesso = request.getParameter("sesso");
			String ddn = request.getParameter("ddn");
			System.out.println("sesso : "+sesso);
			User utente = null;
			
			email = email.toLowerCase();
			
			try {
				utente = new User(email, nome, cognome, password,sesso, ddn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			DbRegisterLogin database = null;
			try {
				
				
				database = new DbRegisterLogin();
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			try {
				if(utente.userExists()) {
					
					// restituisco con errore
					error="Utente esiste! Scegli un email diversa";
					response.sendRedirect(redirect.getREGISTRATION_PAGE()+"?error="+error+"&email="+email+"&nome="+nome+"&cognome="+cognome+"&ddn="+ddn); 
					
					
				}else {
					
					// salva dati in una tabella temporanea
					String otp = generateOTP();
					System.out.println(sesso);
					database.createTempAccount(utente, otp);
					
				
					// manda la mail 
					Mailer mail = new Mailer(email, nome, redirect.getCONFIRM_REGISTRATION_SERVLET()+"?email="+email+"&otp="+otp);
					boolean sent = mail.send();
					
					if(sent) {
						//reinderizza sulla pagina di conferma
						
						response.sendRedirect(redirect.getTO_CONFIRM_PAGE()+"?notice=Ti abbiamo inviato una mail di conferma");  
					}else {
						
						//reinderizza sulla pagina di registrazione
						error="errore nella mail, controlla che sia giusta";
						response.sendRedirect(redirect.getREGISTRATION_PAGE()+"error="+error);  
					}
					
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				
				error="Registrazione non è andata a buon fine";
				response.sendRedirect(redirect.getREGISTRATION_PAGE()+"?error="+error+"&email="+email+"&nome="+nome+"&cognome="+cognome+"&ddn="+ddn); 
				e.printStackTrace();
			}
			
			

		}else {
			
			response.sendRedirect(redirect.getHOME_PAGE());
		}
		
		// mettere link frontend
	}

}
