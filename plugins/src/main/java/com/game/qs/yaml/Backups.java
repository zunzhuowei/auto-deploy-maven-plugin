package com.game.qs.yaml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/17 9:21.
 * Description:
 */
public class Backups implements Serializable {

    private String serverId;

    private List<BackupCfg> backupCfgs;



    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public List<BackupCfg> getBackupCfgs() {
        return backupCfgs;
    }

    public void setBackupCfgs(List<BackupCfg> backupCfgs) {
        this.backupCfgs = backupCfgs;
    }

    @Override
    public String toString() {
        return "Backups{" +
                "serverId='" + serverId + '\'' +
                ", backupCfgs=" + backupCfgs +
                '}';
    }
}
