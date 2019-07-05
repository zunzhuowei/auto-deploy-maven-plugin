package com.game.qs.process.impl;

import com.game.qs.process.IUploadPackageService;
import com.game.qs.sh.SftpConnect;
import com.game.qs.yaml.AppPackage;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.Server;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by zun.wei on 2019/5/18 17:53.
 * Description:
 */
public class UploadPackageService implements IUploadPackageService {


    @Override
    public void uploadApps(Deploy deploy) {
        List<AppPackage> appPackages = deploy.getUploadPackages();
        if (Objects.isNull(appPackages)) {
            System.out.println("upload application cfg not config!");
            return;
        }

        List<Server> servers = deploy.getServers();
        if (Objects.isNull(servers) || servers.isEmpty()) {
            System.out.println("servers not config, can't backups remote server application!");
            return;
        }

        appPackages.forEach(appPackage -> {
            String serverId = appPackage.getServerId();

            String localPackagePath = appPackage.getLocalPackagePath();
            String remoteDest = appPackage.getRemoteDest();
            String packageName = appPackage.getPackageName();
            if (StringUtils.isNotBlank(serverId)
                    && StringUtils.isNotBlank(localPackagePath)
                    && StringUtils.isNotBlank(packageName)
                    && StringUtils.isNotBlank(remoteDest)) {

                // 获取配置的与备份配置的serverId 匹配的server
                Optional<Server> optionalServer = servers.stream()
                        .filter(e -> StringUtils.equalsIgnoreCase(e.getId(), serverId)).findFirst();

                optionalServer.ifPresent(server -> {
                    String serverHost = server.getHost();
                    int serverPort = server.getPort();
                    String serverUserName = server.getUserName();
                    String serverPassWord = server.getPassWord();

                    if (StringUtils.isBlank(serverHost)
                            || (StringUtils.isBlank(serverUserName)
                            || (StringUtils.isBlank(serverPassWord)))) {
                        System.out.println("server config error2 !");
                        return;
                    }

                    // 创建连接
                    SftpConnect sftpConnect = new SftpConnect(serverUserName, serverPassWord, serverHost, serverPort);
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(new File(localPackagePath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 上传文件
                    sftpConnect.upLoadFile(remoteDest, packageName, fileInputStream);

                    if (Objects.nonNull(fileInputStream)) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    sftpConnect.disconnect();
                    System.out.println("uploadApps complete !");
                });
            }
        });

    }

}
