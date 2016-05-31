package io.github.wwqgtxx.soilqualitymonitor.bean;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by wwq on 2016/5/29.
 */
@Entity
@Table(name = "User")
public class UserBean {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE,generator="tableGenerator")
    @TableGenerator(name="tableGenerator",initialValue=0,allocationSize=1)
    @Column(name="id")
    private long id;

    @NaturalId
    private String username;

    private String password;
    private boolean isAdmin;
    private String cookie;

    public UserBean(){
        this("","");
    }
    public UserBean(String username,String password){
        this(username, password, false);
    }
    public UserBean(String username,String password,boolean isAdmin){
        this(username, password, isAdmin,"");
    }
    public UserBean(String username,String password,boolean isAdmin,String cookie){
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserBean{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", isAdmin=").append(isAdmin);
        sb.append(", cookie='").append(cookie).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
