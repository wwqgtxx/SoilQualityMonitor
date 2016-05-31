package io.github.wwqgtxx.soilqualitymonitor.userlogin;

import io.github.wwqgtxx.soilqualitymonitor.bean.UserBean;
import io.github.wwqgtxx.soilqualitymonitor.common.DataBaseConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * Created by wwq on 2016/5/27.
 */
public class UserLoginManager {
    private static final Logger logger = LogManager.getLogger(UserLoginManager.class);
    private static final DataBaseConnector dataBaseConnector = DataBaseConnector.getDataBaseConnecter();
    private UserLoginManager(){}
    private static final UserLoginManager userLoginManager = new UserLoginManager();

    public static UserLoginManager getUserLoginManager() {
        return userLoginManager;
    }

    public void init(){
        if(getUser("admin") == null)
            newUser("admin","admin",true);
        if(logger.isDebugEnabled()){
            for ( UserBean userBean : dataBaseConnector.getAllByClass(UserBean.class) ) {
                logger.debug( userBean.toString() );
            }
        }
    }

    private UserBean getUser(String username){
        return dataBaseConnector.getByNaturalId(UserBean.class,username);
    }


    public boolean checkLoginWithNamePassword(String username, String password){
        UserBean user = getUser(username);
        if (user!=null){
            return user.getPassword().equals(password);
        }
        return false;
    }
    public boolean checkLoginWithCookie(String username, String cookie){
        UserBean user = getUser(username);
        if (user!=null){
            return user.getCookie().equals(cookie);
        }
        return false;
    }
    public boolean newUser(String username, String password){
        return newUser(username, password,false);
    }
    private boolean newUser(String username, String password, Boolean isAdmin){
        UserBean user = getUser(username);
        if (user==null){
            user = new UserBean(username,password,isAdmin);
            logger.info("创建了一个用户"+user.toString());
            return dataBaseConnector.saveOrUpdate(user);
        }
        return false;
    }
    public boolean changeUserPassword(String username, String oldpassword, String newpassword){
        UserBean user = getUser(username);
        if (user!=null){
            if (user.getPassword().equals(oldpassword)){
                user.setPassword(newpassword);
                return dataBaseConnector.saveOrUpdate(user);
            }
        }
        return false;
    }
    public String MakeUUIDToUser(String username){
        String uuid= UUID.randomUUID().toString();
        UserBean user = getUser(username);
        if (user!=null){
            user.setCookie(uuid);
            logger.debug(()->"MakeUUIDToUser "+user.toString());
            if(dataBaseConnector.saveOrUpdate(user))
                return uuid;
        }
        return null;
    }
    public boolean DeleteUUIDToUser(String username){
        UserBean user = getUser(username);
        if (user!=null){
            user.setCookie("");
            logger.debug(()->"DeleteUUIDToUser "+user.toString());
            return dataBaseConnector.saveOrUpdate(user);
        }
        return false;
    }

}
