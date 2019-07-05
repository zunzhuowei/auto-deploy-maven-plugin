package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/16.
 */
public class MoveFile2Dir implements Serializable {

    /*
     <filePathName>E://aa/pom.xml</filePathName>
     <destinationDir>E://haha/</destinationDir>
     */

    // 要移动的文件目录名称，如：D://aa/log4j.properties
    private String filePathName;

    // 移动到的目的地目录，如：移动到 F://cc/
    private String destinationDir;

    public String getFilePathName() {
        return filePathName;
    }

    /**
     * @param filePathName 要移动的文件目录名称，如：D://aa/log4j.properties
     */
    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    public String getDestinationDir() {
        return destinationDir;
    }

    /**
     * @param destinationDir 移动到的目的地目录，如：移动到 F://cc/
     */
    public void setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
    }

    @Override
    public String toString() {
        return "MoveFile2Dir{" +
                "filePathName='" + filePathName + '\'' +
                ", destinationDir='" + destinationDir + '\'' +
                '}';
    }
}
