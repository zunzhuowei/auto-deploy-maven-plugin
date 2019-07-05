package com.game.qs.model;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/15.
 */
public class MoveFile2Dir implements Serializable {

    // 要移动的文件目录名称，如：D://aa/log4j.properties
    private String filePathName;

    // 移动到的目的地目录，如：移动到 F://cc/
    private String destinationDir;


    /**
     * @param filePathName 要移动的文件目录名称，如：D://aa/log4j.properties
     */
    public MoveFile2Dir setFilePathName(String filePathName) {
        this.filePathName = filePathName;
        return this;
    }

    /**
     * @param destinationDir 移动到的目的地目录，如：移动到 F://cc/
     */
    public MoveFile2Dir setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
        return this;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public String getDestinationDir() {
        return destinationDir;
    }

    @Override
    public String toString() {
        return "MoveFile2Dir{" +
                "filePathName='" + filePathName + '\'' +
                ", destinationDir='" + destinationDir + '\'' +
                '}';
    }
}
