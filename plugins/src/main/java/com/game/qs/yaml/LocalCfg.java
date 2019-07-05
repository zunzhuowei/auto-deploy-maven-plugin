package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/17 9:52.
 * Description:
 */
public class LocalCfg implements Serializable {

    private String filePathName;

    private String destinationDir;

    @Override
    public String toString() {
        return "LocalCfg{" +
                "filePathName='" + filePathName + '\'' +
                ", destinationDir='" + destinationDir + '\'' +
                '}';
    }

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    public String getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
    }
}
