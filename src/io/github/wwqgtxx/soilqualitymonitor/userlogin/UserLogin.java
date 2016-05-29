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
public class UserLogin extends ActionSupport implements ServletResponseAware, SessionAware {
    private static Logger logger = LogManager.getLogger(UserLogin.class);
    private static  UserLoginManager userLoginManager = UserLoginManager.getUserLoginManager();

    private String              name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        isLoginSucc = userLoginManager.checkLoginWithNamePassword(name,password);

        if (isLoginSucc) {

            //成功登录后记录session和cookie
            if (rememberMe) {
                String t = name + "," + password;

                Cookie cookie = new Cookie(CommonConstants.COOKIE_KEY_REMEMBER_LOGIN, t);

                cookie.setMaxAge(CommonConstants.COOKIE_AGE);//设置cookie存活时间
                servletResponse.addCookie(cookie);

            }

            //设置session中的登录用户信息
            session.put(CommonConstants.SESSION_KEY_USER_NAME, name);

            //从session中获取登陆前URL，获取后移除session中的这个值
            String goingToURL = (String) session.get(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN);
            setGoingToURL(goingToURL);
            session.remove(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN);

            logger.info("登录成功[" + name + "]");
            return SUCCESS;
        } else {
            logger.error("登录失败[" + name + "][" + password + "]");
            return INPUT;
        }
    }

}