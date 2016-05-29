package io.github.wwqgtxx.soilqualitymonitor.userlogin;

/**
 * Created by wwq on 2016/5/27.
 */
public class CommonConstants {
    public static final String SESSION_KEY_USER_NAME = "USER_NAME";
    public static final String COOKIE_KEY_REMEMBER_LOGIN = "MEMBER_LOGIN";
    public static final String SESSION_KEY_URL_BEFORE_LOGIN = "URL_BEFORE_LOGIN";
    // default cookie's age is -1, indicating the cookie will persist until browser shutdown.
    // so set cookie's age to 120 days: 120 * 24 * 60 * 60 * 60 seconds
    public static final int COOKIE_AGE = 120 * 24 * 60 * 60 * 60;
}
