package io.github.wwqgtxx.soilqualitymonitor.userlogin;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by wwq on 2016/5/27.
 */
public class UserLogoutAction extends ActionSupport implements ServletResponseAware, SessionAware {
    private static Logger logger = LogManager.getLogger(UserLogoutAction.class);
    private static  UserLoginManager userLoginManager = UserLoginManager.getUserLoginManager();

    private HttpServletResponse servletResponse;
    private Map<String, Object> session;

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    @Override
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


    public String execute() throws Exception {

        String username = (String) session.get(CommonConstants.SESSION_KEY_USER_NAME);
        userLoginManager.DeleteUUIDToUser(username);
        Cookie jidCookie = new Cookie(CommonConstants.COOKIE_KEY_REMEMBER_LOGIN, null);
        jidCookie.setMaxAge(0);
        jidCookie.setPath("/");
        servletResponse.addCookie(jidCookie);
        session.remove(CommonConstants.SESSION_KEY_USER_NAME);
        session.remove(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN);
        session.put(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN,"/");
        logger.info("登出成功[" + username + "]");
        return SUCCESS;
    }

}