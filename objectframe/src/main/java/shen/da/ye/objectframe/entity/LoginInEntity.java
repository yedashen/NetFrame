package shen.da.ye.objectframe.entity;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 10:39
 **/

public class LoginInEntity {
    private String userName;
    private String password;
    private String sessionId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
