package com.game.qs.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by zun.wei on 2019/5/16 19:12.
 * Description:
 */
public class Application implements Serializable {

    private String environment;

    private String serviceName;

    private String appName;


    @Override
    public String toString() {
        return "Application{" +
                "environment='" + environment + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }


    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
