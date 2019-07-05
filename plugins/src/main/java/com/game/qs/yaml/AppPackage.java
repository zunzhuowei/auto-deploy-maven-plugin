package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/17 11:20.
 * Description:
 */
public class AppPackage implements Serializable {

    // 远程服务器id
    private String serverId;

    // 应用包所在目录地址
    private String localPackagePath;

    // 远程服务器目录
    private String remoteDest;

    // 包名
    private String packageName;

    @Override
    public String toString() {
        return "AppPackage{" +
                "serverId='" + serverId + '\'' +
                ", localPackagePath='" + localPackagePath + '\'' +
                ", remoteDest='" + remoteDest + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getLocalPackagePath() {
        return localPackagePath;
    }

    public void setLocalPackagePath(String localPackagePath) {
        this.localPackagePath = localPackagePath;
    }

    public String getRemoteDest() {
        return remoteDest;
    }

    public void setRemoteDest(String remoteDest) {
        this.remoteDest = remoteDest;
    }
}
