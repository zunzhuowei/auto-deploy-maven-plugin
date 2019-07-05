package com.game.qs.process.impl;

import com.game.qs.process.IDeployInstruService;
import com.game.qs.sh.SSHClient;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.DeployInstructionSet;
import com.game.qs.yaml.Server;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by zun.wei on 2019/5/19.
 */
public class DeployInstruService implements IDeployInstruService {


    @Override
    public void deployInstrutiionSet(Deploy deploy) {
        List<DeployInstructionSet> deployInstructionSets = deploy.getDeployInstructionSet();
        if (Objects.isNull(deployInstructionSets)) {
            System.out.println("deployInstructionSet is not config!");
            return;
        }

        List<Server> servers = deploy.getServers();
        if (Objects.isNull(servers) || servers.isEmpty()) {
            System.out.println("servers not config, can't backups remote server application!");
            return;
        }

        deployInstructionSets.forEach(deployInstructionSet -> {
            String serverId = deployInstructionSet.getServerId();
            String[] instructions = deployInstructionSet.getInstructions();
            if (StringUtils.isNotBlank(serverId)
                    && Objects.nonNull(instructions)
                    && (instructions.length > 0)) {

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
                        System.out.println("server config error3 !");
                        return;
                    }

                    // 创建连接
                    SSHClient sshClient = new SSHClient().setHost(serverHost).setPort(serverPort)
                            .setUsername(serverUserName).setPassword(serverPassWord);
                    try {
                        sshClient.sendShellCmd(instructions);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sshClient.logout();
                    System.out.println("deployInstrutiionSet complete !");
                });
            }
        });

    }

}
