package com.game.qs.utils;

import com.game.qs.sh.SSHClient;
import com.game.qs.sh.SftpConnect;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/18 15:29.
 * Description:
 */
public class DownLoadTools implements Serializable {


    /**
     *  下载远程文件到本地
     * @param sshClient com.game.qs.sh.SSHClient
     * @param sftpConnect com.game.qs.sh.SftpConnect
     * @param remoteFilePath  /wwwroot/java/tomcat/webapps/app.war  || /wwwroot/java/cfgs/*.properties
     * @param localPathPrefix D://aa/bb/
     * @throws Exception
     */
    public static void download2Local(SSHClient sshClient, SftpConnect sftpConnect,
                                      String remoteFilePath, String localPathPrefix) throws Exception {
        String ls = sshClient.sendCmd("ls " + remoteFilePath);
        String[] fileNamePaths = ls.split("\n");
        for (String fileNamePath : fileNamePaths) {
            if (!fileNamePath.contains("/")) continue;
            String fileName = fileNamePath.substring(fileNamePath.lastIndexOf("/") + 1); //  jdbc.properties
            InputStream inputStream = sftpConnect.downFile(fileNamePath.replace(fileName, ""), fileName);
            localPathPrefix = "/".equals(localPathPrefix.substring(localPathPrefix.length() - 1)) ? localPathPrefix : localPathPrefix + "/";
            FileUtils.copyInputStreamToFile(inputStream, new File(localPathPrefix + fileName));
        }
    }

}
