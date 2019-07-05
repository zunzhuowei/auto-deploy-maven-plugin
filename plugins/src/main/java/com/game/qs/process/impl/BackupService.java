package com.game.qs.process.impl;

import com.game.qs.process.IBackupService;
import com.game.qs.sh.SSHClient;
import com.game.qs.sh.SftpConnect;
import com.game.qs.utils.DownLoadTools;
import com.game.qs.yaml.BackupCfg;
import com.game.qs.yaml.Backups;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.Server;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by zun.wei on 2019/5/17 14:52.
 * Description: 备份实现
 */
public class BackupService implements IBackupService {

    @Override
    public void backup(Deploy deploy) {
        List<Backups> backups = deploy.getBackups();

        // 如果没有配置备份配置，则不往下执行
        if (Objects.isNull(backups) || backups.isEmpty()) return;

        List<Server> servers = deploy.getServers();
        if (Objects.isNull(servers) || servers.isEmpty()) {
            System.out.println("servers not config, can't backups remote server application!");
            return;
        }

        backups.forEach(backups1 -> {
            // 获取备份配置的 serverId
            String backups1ServerId = backups1.getServerId();
            List<BackupCfg> backupCfgs = backups1.getBackupCfgs();
            // 获取配置的与备份配置的serverId 匹配的server
            Optional<Server> optionalServer = servers.stream()
                    .filter(e -> StringUtils.equalsIgnoreCase(e.getId(), backups1ServerId)).findFirst();
            // 如果匹配服务，则再往下走
            optionalServer.ifPresent(server -> {
                String serverHost = server.getHost();
                int serverPort = server.getPort();
                String serverUserName = server.getUserName();
                String serverPassWord = server.getPassWord();

                if (StringUtils.isBlank(serverHost)
                        || (StringUtils.isBlank(serverUserName)
                        || (StringUtils.isBlank(serverPassWord)))) {
                    System.out.println("server config error !");
                    return;
                }

                // 创建连接
                SftpConnect sftpConnect = new SftpConnect(serverUserName, serverPassWord, serverHost, serverPort);
                SSHClient sshClient = new SSHClient().setHost(serverHost).setPort(serverPort)
                        .setUsername(serverUserName).setPassword(serverPassWord);

                backupCfgs.forEach(backupCfg -> {
                    // 远程备份文件目录地址
                    String remoteSrcFilePath = backupCfg.getRemoteSrcFilePath(); //  /wwwroot/java/tomcat/webapps/app.jar
                    if (StringUtils.isNotBlank(remoteSrcFilePath)) {
                        // 备份到远程服务中
                        List<String> remoteBackupDestPaths = backupCfg.getRemoteBackupDestPaths();
                        if (Objects.nonNull(remoteBackupDestPaths)) {
                            remoteBackupDestPaths.forEach(remoteBackupDestPath -> {
                                try {
                                    sshClient.sendCmd("cp -r " + remoteSrcFilePath + " " + remoteBackupDestPath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        // 备份到本地
                        List<String> localPaths = backupCfg.getLocalPaths();
                        if (Objects.nonNull(localPaths)) {
                            localPaths.forEach(localPath -> {
                                try {
                                    DownLoadTools.download2Local(sshClient, sftpConnect, remoteSrcFilePath, localPath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                });

                // 关闭连接
                sftpConnect.disconnect();
                sshClient.logout();
            });
            System.out.println("backup complete !");
        });

    }

}
