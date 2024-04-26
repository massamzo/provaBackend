package sessions;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class Sessionmanager {
	public static Map<String, HttpSession> sessionMap = new HashMap<>();
}
