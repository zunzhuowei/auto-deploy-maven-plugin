package com.game.qs.process.impl;

import com.game.qs.process.IReductionService;
import com.game.qs.sh.SSHClient;
import com.game.qs.sh.SftpConnect;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.ReductionApp;
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
 * Created by zun.wei on 2019/5/20 15:28.
 * Description:
 */
public class ReductionService implements IReductionService {


    @Override
    public void reductionApps(Deploy deploy) {
        List<ReductionApp> reductionApps = deploy.getReductionApps();
        if (Objects.isNull(reductionApps)) {
            System.out.println("reductionApps is not config!");
            return;
        }

        List<Server> servers = deploy.getServers();
        if (Objects.isNull(servers) || servers.isEmpty()) {
            System.out.println("servers not config, can't backups remote server application!");
            return;
        }

        reductionApps.forEach(reductionApp -> {
            String serverId = reductionApp.getServerId();

            String localFilePath = reductionApp.getLocalFilePath();
            String remoteDestPath = reductionApp.getRemoteDestPath();
            String reductionTempDir = reductionApp.getReductionTempDir();
            String[] instructions = reductionApp.getInstructions();

            if (StringUtils.isNotBlank(serverId)
                    && Objects.nonNull(instructions)
                    && instructions.length > 0
                    && (StringUtils.isNotBlank(localFilePath)
                    || StringUtils.isNotBlank(remoteDestPath))) {

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
                        System.out.println("server config error4 !");
                        return;
                    }

                    // 创建连接
                    SSHClient sshClient = new SSHClient().setHost(serverHost).setPort(serverPort)
                            .setUsername(serverUserName).setPassword(serverPassWord);

                    // 把
                    if (StringUtils.isBlank(remoteDestPath)) {
                        if (StringUtils.isBlank(localFilePath)) {
                            System.out.println("rmoteDestPath and localFilePath is null");
                            return;
                        } else {
                            // 创建连接
                            SftpConnect sftpConnect = new SftpConnect(serverUserName, serverPassWord, serverHost, serverPort);
                            FileInputStream fileInputStream = null;
                            try {
                                fileInputStream = new FileInputStream(new File(localFilePath));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            // 上传文件
                            String packageName = localFilePath.substring(localFilePath.lastIndexOf("/") + 1);
                            sftpConnect.upLoadFile(reductionTempDir, packageName, fileInputStream);

                            if (Objects.nonNull(fileInputStream)) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            sftpConnect.disconnect();
                        }
                    } else {
                        try {
                            sshClient.sendShellCmd("cp -r " + remoteDestPath + " " + reductionTempDir);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        sshClient.sendShellCmd(instructions);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sshClient.logout();
                    System.out.println("reductionApps complete !");
                });
            }

        });
    }
}
