package com.game.qs.process.impl;

import com.game.qs.enum0.ConfigType;
import com.game.qs.process.IConfigService;
import com.game.qs.sh.SSHClient;
import com.game.qs.sh.SftpConnect;
import com.game.qs.utils.DownLoadTools;
import com.game.qs.yaml.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by zun.wei on 2019/5/18 16:44.
 * Description:
 */
public class ConfigService implements IConfigService {


    @Override
    public void modifySrcCfgs(Deploy deploy) {
        Config config = deploy.getConfig();
        if (Objects.isNull(config)) {
            System.out.println("Configuration file not configured ");
            //如果没有配置，则沿用原来老的配置
            //TODO原来老的配置代码
            // 替换配置文件
            List<MoveFile2Dir> moveFile2Dirs = deploy.getMoveFile2Dirs();
            if (Objects.nonNull(moveFile2Dirs)) {
                for (MoveFile2Dir moveFile2Dir : moveFile2Dirs) {
                    String destinationDir = moveFile2Dir.getDestinationDir();
                    String filePathName = moveFile2Dir.getFilePathName();
                    if (StringUtils.isBlank(destinationDir) || StringUtils.isBlank(filePathName)) continue;
                    try {
                        FileUtils.copyFileToDirectory
                                (new File(filePathName), new File(destinationDir));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }
        ConfigType configType = config.getEnable();
        if (configType == ConfigType.remote)
        {
            RemoteConfig remoteConfig = config.getRemote();
            if (Objects.nonNull(remoteConfig))
            {
                List<RemoteServer> remoteServers = remoteConfig.getRemoteServers();
                if (Objects.nonNull(remoteServers))
                {
                    remoteServers.forEach(remoteServer -> {
                        String host = remoteServer.getHost();
                        String port = remoteServer.getPort();
                        String username = remoteServer.getUserName();
                        String password = remoteServer.getPassWord();

                        if (StringUtils.isBlank(host)
                                || (StringUtils.isBlank(username)
                                || (StringUtils.isBlank(password)))) {
                            System.out.println("server config error !");
                            return;
                        }

                        List<RemoteCfg> remoteCfgs = remoteServer.getConfigCfgs();
                        if (Objects.nonNull(remoteCfgs)) {
                            // 创建连接
                            SftpConnect sftpConnect = new SftpConnect(username, password, host, Integer.parseInt(port));
                            SSHClient sshClient = new SSHClient().setHost(host).setPort(Integer.parseInt(port))
                                    .setUsername(username).setPassword(password);

                            remoteCfgs.forEach(remoteCfg -> {
                                String localDestPath = remoteCfg.getLocalDestPath();
                                String remoteCfgFilePath = remoteCfg.getRemoteCfgFilePath();
                                if (StringUtils.isBlank(localDestPath) || StringUtils.isBlank(remoteCfgFilePath)) {
                                    return;
                                }
                                try {
                                    DownLoadTools.download2Local(sshClient, sftpConnect, remoteCfgFilePath, localDestPath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            // 关闭连接
                            sftpConnect.disconnect();
                            sshClient.logout();
                        }
                    });
                }
            }

        }
        else if (configType == ConfigType.local)
        {
            LocalConfig localConfig = config.getLocal();
            if (Objects.nonNull(localConfig)) {
                List<LocalCfg> localCfgs = localConfig.getLocalCfgs();
                if (Objects.nonNull(localCfgs)) {
                    localCfgs.forEach(localCfg -> {
                        String destinationDir = localCfg.getDestinationDir();
                        String filePathName = localCfg.getFilePathName();
                        if (StringUtils.isNotBlank(destinationDir)
                                && StringUtils.isNotBlank(filePathName)) {
                            try {
                                FileUtils.copyFileToDirectory
                                        (new File(filePathName), new File(destinationDir));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
        else {
            System.out.println("not have " + configType + " type");
            return;
        }
        System.out.println("modifySrcCfgs complete !");
    }

}
