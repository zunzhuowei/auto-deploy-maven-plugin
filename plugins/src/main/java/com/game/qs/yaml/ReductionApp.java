package com.game.qs.yaml;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by zun.wei on 2019/5/17 11:42.
 * Description:
 */
public class ReductionApp implements Serializable {

    private String serverId;

    private String[] instructions;

    private String localFilePath;

    private String remoteDestPath;

    private String reductionTempDir;

    @Override
    public String toString() {
        return "ReductionApp{" +
                "serverId='" + serverId + '\'' +
                ", instructions=" + Arrays.toString(instructions) +
                ", localFilePath='" + localFilePath + '\'' +
                ", remoteDestPath='" + remoteDestPath + '\'' +
                ", reductionTempDir='" + reductionTempDir + '\'' +
                '}';
    }

    public String getReductionTempDir() {
        return reductionTempDir;
    }

    public void setReductionTempDir(String reductionTempDir) {
        this.reductionTempDir = reductionTempDir;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getRemoteDestPath() {
        return remoteDestPath;
    }

    public void setRemoteDestPath(String remoteDestPath) {
        this.remoteDestPath = remoteDestPath;
    }
}
