package com.game.qs.yaml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/17 9:25.
 * Description:
 */
public class BackupCfg implements Serializable {

    private String remoteSrcFilePath;

    private List<String> remoteBackupDestPaths;

    private List<String> localPaths;

    @Override
    public String toString() {
        return "BackupCfg{" +
                "remoteSrcFilePath='" + remoteSrcFilePath + '\'' +
                ", remoteBackupDestPaths=" + remoteBackupDestPaths +
                ", localPaths=" + localPaths +
                '}';
    }

    public String getRemoteSrcFilePath() {
        return remoteSrcFilePath;
    }

    public void setRemoteSrcFilePath(String remoteSrcFilePath) {
        this.remoteSrcFilePath = remoteSrcFilePath;
    }

    public List<String> getRemoteBackupDestPaths() {
        return remoteBackupDestPaths;
    }

    public void setRemoteBackupDestPaths(List<String> remoteBackupDestPaths) {
        this.remoteBackupDestPaths = remoteBackupDestPaths;
    }

    public List<String> getLocalPaths() {
        return localPaths;
    }

    public void setLocalPaths(List<String> localPaths) {
        this.localPaths = localPaths;
    }
}
