package sessions;

public class Redirections {

	private final String HOME_PAGE = "http://192.168.1.115:5500/homepage.html";
	private final String LOGIN_PAGE = "http://192.168.1.115:5500/login.html";
	private final String TO_CONFIRM_PAGE = "http://192.168.1.115:5500/toconfirm.html";
	private final String REGISTRATION_PAGE = "http://192.168.1.115:5500/index.html";
	private final String CONFIRMATION_PAGE = "http://192.168.1.115:5500/confirm.html";
	private final String RECUPERO_PASS_PAGE = "http://192.168.1.115:5500/passwordRecover.html";
	private final String RESET_PASSW_PAGE = "http://192.168.1.115:5500/resetPassword.html";
	
	public String getRESET_PASSW_PAGE() {
		return RESET_PASSW_PAGE;
	}


	private final String SESSION_INFO_SERVLET = "http://192.168.1.115:8080/DilaxiaTornei/Sessioninfo";
	private final String CONFIRM_REGISTRATION_SERVLET = "http://192.168.1.115:8080/DilaxiaTornei/ConfirmRegistration";
	private final String LOGIN_SERVLET = "http://192.168.1.115:8080/DilaxiaTornei/Login";
	
	
	private final String CORS_ALLOWED = "http://192.168.1.115:5500";


	public String getHOME_PAGE() {
		return HOME_PAGE;
	}


	public String getLOGIN_PAGE() {
		return LOGIN_PAGE;
	}


	public String getTO_CONFIRM_PAGE() {
		return TO_CONFIRM_PAGE;
	}


	public String getREGISTRATION_PAGE() {
		return REGISTRATION_PAGE;
	}


	public String getCONFIRMATION_PAGE() {
		return CONFIRMATION_PAGE;
	}


	public String getSESSION_INFO_SERVLET() {
		return SESSION_INFO_SERVLET;
	}


	public String getCONFIRM_REGISTRATION_SERVLET() {
		return CONFIRM_REGISTRATION_SERVLET;
	}


	public String getLOGIN_SERVLET() {
		return LOGIN_SERVLET;
	}


	public String getRECUPERO_PASS_PAGE() {
		return RECUPERO_PASS_PAGE;
	}


	public String getCORS_ALLOWED() {
		return CORS_ALLOWED;
	}
	
	
	
}