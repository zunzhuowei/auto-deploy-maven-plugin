package com.game.qs.yaml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/17 10:03.
 * Description:
 */
public class RemoteServer implements Serializable {

    private String host;

    private String port;

    private String userName;

    private String passWord;

    private List<RemoteCfg> configCfgs;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public List<RemoteCfg> getConfigCfgs() {
        return configCfgs;
    }

    public void setConfigCfgs(List<RemoteCfg> configCfgs) {
        this.configCfgs = configCfgs;
    }

    @Override
    public String toString() {
        return "RemoteServer{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", configCfgs=" + configCfgs +
                '}';
    }
}
