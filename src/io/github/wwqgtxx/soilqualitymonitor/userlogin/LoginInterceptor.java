package io.github.wwqgtxx.soilqualitymonitor.userlogin;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wwq on 2016/5/27.
 */
public class LoginInterceptor extends AbstractInterceptor {
    private static Logger logger = LogManager.getLogger(LoginInterceptor.class);
    UserLoginManager userLoginManager = UserLoginManager.getUserLoginManager();

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        ActionContext actionContext = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext
                .get(StrutsStatics.HTTP_REQUEST);
        Map<String, Object> session = actionContext.getSession();

        //首先判断session，查找是否登录成功，通过拦截器
        if (session != null && session.get(CommonConstants.SESSION_KEY_USER_NAME) != null) {
            logger.info("通过拦截器,session中有记录[" + session.get(CommonConstants.SESSION_KEY_USER_NAME)
                    + "]");
            return invocation.invoke();
        }

        //其次cookie验证，是否有记住的登录状态
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.debug("读取cookie项[" + cookie.getName() + "]");

                if (CommonConstants.COOKIE_KEY_REMEMBER_LOGIN.equals(cookie.getName())) {
                    String value = cookie.getValue();
                    if (StringUtils.isNotBlank(value)) {
                        String[] split = value.split(",");
                        String name = split[0];
                        String password = split[1];


                        if (userLoginManager.checkLoginWithNamePassword(name, password)) {
                            //check name/password from cookie success
                            logger.info("通过拦截器,cookie中有记录[" + name + "]");
                            session.put(CommonConstants.SESSION_KEY_USER_NAME, name);
                            return invocation.invoke();
                        } else {
                            //check name/password from cookie failure
                            setGoingToURL(session, invocation);
                            return Action.LOGIN;
                        }
                    } else {
                        setGoingToURL(session, invocation);
                        return Action.LOGIN;
                    }
                }
            }
        }

        setGoingToURL(session, invocation);
        return Action.LOGIN;
    }

    private void setGoingToURL(Map<String, Object> session, ActionInvocation invocation) {
        String url = "";
        String namespace = invocation.getProxy().getNamespace();

        if (StringUtils.isNotBlank(namespace) && !namespace.equals("/")) {
            url = url + namespace;
        }

        String actionName = invocation.getProxy().getActionName();
        if (StringUtils.isNotBlank(actionName)) {
            url = url + "/" + actionName + ".action";
        }

        logger.debug("拼接登录前URL，结果:" + CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN + "[" + url
                    + "]");
        session.put(CommonConstants.SESSION_KEY_URL_BEFORE_LOGIN, url);
    }

    //... getter & setter methods
}