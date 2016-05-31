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
public class UserLoginAction extends ActionSupport implements ServletResponseAware, SessionAware {
    private static final Logger logger = LogManager.getLogger(UserLoginAction.class);
    private static final UserLoginManager userLoginManager = UserLoginManager.getUserLoginManager();

    private String              username;
    private String              password;
    private boolean             rememberMe;

    private HttpServletResponse servletResponse;
    private Map<String, Object> session;

    private String              goingToURL;//登录前的URL

    public String getGoingToURL() {
        return goingToURL;
    }

    public void setGoingToURL(String goingToURL) {
        this.goingToURL = goingToURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

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

        boolean isLoginSucc = false;
        isLoginSucc = userLoginManager.checkLoginWithNamePassword(username,password);

        if (isLoginSucc) {

            //成功登录后记录session和cookie
            if (rememberMe) {
                String uuid=userLoginManager.MakeUUIDToUser(username);

                String t = username + "," + uuid;

                Cookie cookie = new Cookie(CommonConstants.COOKIE_KEY_REMEMBER_LOGIN, t);
                cookie.setPath("/");
                cookie.setMaxAge(CommonConstants.COOKIE_AGE);//设置cookie存活时间
                servletResponse.addCookie(cookie);

            }

            //设置session中的登录用户信息
            session.put(CommonConstants.SESSION_KEY_USER_NAME, username);

            //从session中获取登陆前URL，获取后移除session中的这个值
            String goingToURL = (String) session.get(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN);
            logger.debug(goingToURL);
            if (goingToURL==null)
                goingToURL = "/";
            setGoingToURL(goingToURL);
            session.remove(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN);

            logger.info("登录成功[" + username + "]");
            return SUCCESS;
        } else {
            logger.error("登录失败[" + username + "][" + password + "]");
            return INPUT;
        }
    }

}