package io.github.wwqgtxx.soilqualitymonitor.userlogin;

import io.github.wwqgtxx.soilqualitymonitor.bean.UserBean;
import io.github.wwqgtxx.soilqualitymonitor.common.DataBaseConnector;

/**
 * Created by wwq on 2016/5/27.
 */
public class UserLoginManager {
    private static final DataBaseConnector dataBaseConnector = DataBaseConnector.getDataBaseConnecter();
    private UserLoginManager(){}
    private static final UserLoginManager userLoginManager = new UserLoginManager();

    public static UserLoginManager getUserLoginManager() {
        return userLoginManager;
    }

    public boolean checkLoginWithNamePassword(String name, String password){
        UserBean user = dataBaseConnector.getByNaturalId(UserBean.class,name);
        if (user!=null){
            if (user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
    //public boolean checkLoginWithCookie(String cookie){return true;}
}
