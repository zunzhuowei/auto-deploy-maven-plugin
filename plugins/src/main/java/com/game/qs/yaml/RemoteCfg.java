package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/17 9:40.
 * Description:
 */
public class RemoteCfg implements Serializable {

    private String remoteCfgFilePath;

    private String localDestPath;


    public String getRemoteCfgFilePath() {
        return remoteCfgFilePath;
    }

    public void setRemoteCfgFilePath(String remoteCfgFilePath) {
        this.remoteCfgFilePath = remoteCfgFilePath;
    }

    public String getLocalDestPath() {
        return localDestPath;
    }

    public void setLocalDestPath(String localDestPath) {
        this.localDestPath = localDestPath;
    }
}
