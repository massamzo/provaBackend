package recupero_password;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databasePack.Database;
import databasePack.DbRegisterLogin;
import databasePack.QueryManager;
import databasePack.ResetPassDatabase;
import databasePack.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import mail.Mailer;
import sessions.Redirections;

/**
 * Servlet implementation class RecoverPassword
 */
@WebServlet("/RecoverPassword")
public class RecoverPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Redirections redirect = new Redirections();
	
	 private static final int code_length = 5;
	 private static final String codes = "01A2BCD3456EFGH78IJK9";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecoverPassword() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    private String generatePasskey() {
    	
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
		response.sendRedirect(redirect.getHOME_PAGE());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		
		//ottengo i paramteri 
		
		
		
		
		String associated_email = request.getParameter("email");
		String form_passkey = request.getParameter("passkey");
		String newPass = request.getParameter("newPassword");
		
		
		if((form_passkey == null && newPass == null) && associated_email != null) {   // se ha solo inserito la mail per ricevere il link
			
			
			// genero un pass key da mandare
			
			String temp_passkey = generatePasskey();
			
			Argon2 argon2 = Argon2Factory.create();
			
			// cripto la password
			
			String passkey = argon2.hash(10, 63312, 1, temp_passkey);
			
			
			
			// controllo se utente esiste
			User utente = null;
			
			try {
				
				utente = new User(associated_email, "");
				
				
				if(utente.userExists()) { // se esiste allora proseguo altrimenti mando un errore
					
					// salvo i dati nella tabella temp_pass_recover
					
					
					ResetPassDatabase database = new ResetPassDatabase(associated_email, passkey);
					database.insertPassKey();
					
					// invio la mail con key
					
					Mailer mail = new Mailer(associated_email, redirect.getRESET_PASSW_PAGE()+"?passkey="+passkey, true); // metto true per mandare la mail di reset password
					mail.send();
					
					// redirect to 
					
					response.sendRedirect(redirect.getTO_CONFIRM_PAGE());
				
				}else {
					throw new SQLException();
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				String error = "utente non esiste";
				response.sendRedirect(redirect.getRECUPERO_PASS_PAGE()+"?error="+error);
			}
			
			
		}else if((form_passkey != null && newPass != null) && associated_email == null) { // gestisco il reset della password 
			
			
			// get the associated email form the database
			
			try {
				
				System.out.println(form_passkey);
				ResetPassDatabase database = new ResetPassDatabase(null, form_passkey);
				associated_email = database.getAssEmail();
				
				database.setAssociated_email(associated_email);
				
				database.resetPassword(newPass);
				
				database.removeAllResetRequests();
				
				String notice = "PASSWORD aggiornato!";
				response.sendRedirect(redirect.getRECUPERO_PASS_PAGE()+"?notice="+notice);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				String error = "errore nel resettare la password";
				response.sendRedirect(redirect.getRECUPERO_PASS_PAGE()+"?error="+error);
			}
			
			
			
			
		}
			
			
		
	}

}
