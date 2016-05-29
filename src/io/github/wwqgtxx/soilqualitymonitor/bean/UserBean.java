package io.github.wwqgtxx.soilqualitymonitor.bean;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by wwq on 2016/5/29.
 */
@Entity
@Table(name = "User")
public class UserBean {
    @Id @GeneratedValue @Column(name="id") private long id;
    @NaturalId private String name;
    private String password;
    private boolean isAdmin;
    private String cookie;

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
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", isAdmin=").append(isAdmin);
        sb.append(", cookie='").append(cookie).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
