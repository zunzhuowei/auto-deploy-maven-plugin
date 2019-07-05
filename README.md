## auto-deploy-maven-plugin
该项目是一个自动化部署 `java` 项目到远程服务器的 `maven` 插件

## 快速使用
1. 克隆项目代码到本地
2. `cd` 到 `plugins` 目录，执行 `mvn clean install -DskipTests`
3. `cd` 到 `deploy` 目录，进入到 `resources/happy_beard/acti/dev/` 目录,修改 `deploy-happy_beard-acti-dev.yml` 配置文件
4. 完成 `yml` 配置后， `cd` 到 `deploy` 目录， `mvn clean package -DskipTests`, 即可将项目部署到远程服务器中

参考如下，根据个人需求修改配置（详细模板参考 `plugins` 中的 `deploy-version-template.yml` ）：
```yaml
deploy:
  mavenHome: "D://soft/apache-maven-3.2.1"                   # 配置 maven home 目录
  gitConfig:
    gitDirectory: "E://aa"                                    # 配置 clone 下来的代码存放的位置
    gitUri: "git@github.com:zunzhuowei/zhuozun.git"              # 配置远程仓库地址
    gitRemote: "master"                                       # 配置 git 分支
    gitPassword: ""                             # git 仓库密码，如果已经是 ssh 免密登录了，则为空
    gitUsername: ""                             # git 仓库用户名，如果已经是 ssh 免密登录了，则为空

  # 执行的 maven 指令集；
  pomGoals:
    -
      mavenPomxmlPath: "E://aa/pom.xml"                       # 执行指令的 pom.xml 的地址
      mavenGoals: "clean package -DskipTest=true"                      # 执行的指令

  # 本地替换配置文件集列表
  moveFile2Dirs:
    -
      filePathName: "D://test/yml/bootstrap.yml"                 #  配置文件的地址
      destinationDir: "E://aa/zhuozun-gateway-server/src/main/resources/"                   #  移动配置文件的目的地

  # 远程 linux 服务器配置列表
  servers:
    -
      id: "server1"                                     # 服务器 id 应当唯一
      host: "192.168.1.188"                                   # 服务器 ip
      port:  "22"                                  #  服务器 端口
      userName: "user_01"                               #  服务器用户名
      passWord: "xxxxxxxxxxxxxxxx"                               # 服务器密码

  # 备份
  backups:
    -
      serverId: server1                                 # 配置的服务器 id
      backupCfgs:
        -
          remoteSrcFilePath: /home/user_01/nohup.out                  #  远程源文件目录；
          remoteBackupDestPaths:                # 远程服务备份目录；应该为列表，支持备份到多个目录
            - /home/user_01/qs/

          localPaths:                            # 本地备份目录；应该为列表，支持备份到多个目录
            - D://aa/ss/

  # 配置文件替换
  config:
    enable: remote                                  # 配置文件替换类型；remote/local；枚举类
    # 远程配置文件替换配置
    remote:
      remoteServers:
        -
          host: 192.168.1.188                                   # 配置文件服务器 ip
          port: 22                                   #  配置文件服务器 端口
          userName: user_01                                #  配置文件服务器用户名
          passWord: xxxxxxxxxxxxxxxx                               # 配置文件服务器密码
          configCfgs:                                # 配置详情列表
            -
              remoteCfgFilePath: /home/user_01/qs/bootstrap.yml                      # 远程配置文件服务器的配置文件目录
              localDestPath: E://aa/zhuozun-gateway-server/src/main/resources/                         # 存放获取到的配置文件的本地目录
            -
              remoteCfgFilePath: /home/user_01/qs/bootstrap222.yml                     # 远程配置文件服务器的配置文件目录
              localDestPath: E://aa/zhuozun-gateway-server/src/main/resources/                          # 存放获取到的配置文件的本地目录

    # 本地配置文件替换配置
    local:
      localCfgs:
        -
          filePathName: "D://test/yml/bootstrap.yml"                 #  配置文件的地址
          destinationDir: "E://aa/zhuozun-gateway-server/src/main/resources/"                    #  移动配置文件的目的地

  # 上传打好的包
  uploadPackages:
    -
      serverId: server1                                          # 远程服务器id
      localPackagePath: E://aa/zhuozun-gateway-server/target/zhuozun-gateway-server.jar                                      #  要上传应用包所在本地目录地址
      remoteDest: /home/user_01/                                        # 远程服务器目录
      packageName: gateway.jar                                      # 上传后，在远程服务器中的命名

  # 远程服务器执行 linux 指令集，完成部署任务
  deployInstructionSet:
    -
      serverId: server1                                          # 远程服务器id
      instructions:                                       # 执行的指令集合
        - "cd /home/user_01/"
        - "mkdir logs"
        - "nohup java -jar gateway.jar > ./logs/out.log 2>&1 &"
        - ps
```

## 详细配置介绍

* 激活的部署配置文件列表由 `<application>` 标签中 `<serviceName>` `<appName>` `<environment>` 的值以及
拼接字符串 `<delimiter>` 拼接。
如： `<active>happy_beard-acti-dev</active>`，为激活如下app配置
```xml
<application>
    <serviceName>happy_beard</serviceName>
    <appName>app</appName>
    <environment>dev</environment>
</application>
```

* 配置文件所在目录也由 `<application>` 标签中 `<serviceName>` `<appName>` `<environment>` 的值构成
如 `<application>` 中配置如下：
```xml
<application>
    <serviceName>happy_beard</serviceName>
    <appName>app</appName>
    <environment>dev</environment>
</application>
```
则配置文件所在目录为 `resources/happy_beard/acti/dev/` 中，文件名称为 `deploy-happy_beard-acti-dev.yml`

```xml
 <plugin>
    <groupId>com.game.qs.plugin</groupId>
    <artifactId>plugins</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <id>myid</id>
            <!-- 在打包时候触发 -->
            <phase>package</phase>
            <goals>
                <!-- 目标插件为 mojo = deploy 中 -->
                <goal>deploy</goal>
            </goals>
            <configuration>
                <actives>
                    <!--
                    激活的部署列表，列表命名规则
                      <application> 中 serviceName，appName，environment，拼接。
                      拼接字符串为 <delimiter> 定义；
                      
                    如：happy_beard-acti-dev，为激活如下app配置
                      <application>
                        <serviceName>happy_beard</serviceName>
                        <appName>app</appName>
                        <environment>dev</environment>
                    </application>
                     -->
                    <active>happy_beard-acti-dev</active>
                    <!--<active>happy_beard-acti-test</active> -->
                </actives>
    
                <!-- 是否为还原部署，默认:false 为更新部署 -->
                <isBackup>false</isBackup>
                <!-- yml 配置文件的前缀,默认: 'deploy-' -->
                <deployYmlPrefix>deploy-</deployYmlPrefix>
                <!-- yml 配置文件命名分隔符，默认：- ;如：deploy-happy_beard-acti-prod.yml -->
                <delimiter>-</delimiter>
    
                <applications>
                    <application>
                        <!-- 业务服务名称 -->
                        <serviceName>happy_beard</serviceName>
                        <!-- 应用名称 -->
                        <appName>app</appName>
                        <!-- 部署环境 -->
                        <environment>dev</environment>
                    </application>
                    <application>
                        <serviceName>happy_beard</serviceName>
                        <appName>app</appName>
                        <environment>test</environment>
                    </application>
                    <application>
                        <serviceName>happy_beard</serviceName>
                        <appName>acti</appName>
                        <environment>dev</environment>
                    </application>
                    <application>
                        <serviceName>happy_beard</serviceName>
                        <appName>acti</appName>
                        <environment>prod</environment>
                    </application>
                </applications>
            </configuration>
        </execution>
    </executions>
    </plugin>
```

## 插件打包流程

1. 备份远程服务器中正在执行的应用

2. 拉取 `git` 指定分支的代码

3. 替换 `git` 代码中的配置文件

4. 执行 `maven` 指令打包项目

5. 上传打好的包到远程服务器

6. 部署上传的包到远程服务器中

7. 回滚备份的原来的应用

## 部署执行流程

1. 备份远程正在执行的 `war`、`jar` 包、或者配置文件等到指定目录（远程服务器目录或者本机目录）

2. 根据配置好的 `git` 远程仓库地址 `clone` 指定分支的代码到本地目录；

3. 替换应用配置文件

    3.1  本地配置文件替换到指定应用目录中。
    
    3.2  远程 `linux` 服务器配置文件下载下来替换到指定应用目录中 
    
4. 执行 `maven` 配置的打包指令

5. 上传打好的包（`war`、`jar`）到配置的远程的全部服务器；

6. 根据配置的命令，`ssh` 执行命令给远程服务器，使其做相应的业务

7. 处理部署完成之后的相应事项。

## 回滚应用执行流程

1. 找到部署时的备份的应用包

2. 如果为远程服务器备份，则直接远程执行 `linux` 指令，完成备份还原操作；如果为本地备份，则需先上传应用包，再执行还原指令

3. 处理还原完成后的相应事项。

## 详细配置模板
```yaml
deploy:
  #mavenHome: "E://maven/apache-maven-3.5.4"                   # 配置 maven home 目录
  mavenHome: "D://soft/apache-maven-3.2.1"                   # 配置 maven home 目录
  gitConfig:
    gitDirectory: "E://aa"                                    # 配置 clone 下来的代码存放的位置
    gitUri: "git@github.com:zunzhuowei/base.git"              # 配置远程仓库地址
    gitRemote: "master"                                       # 配置 git 分支
    gitPassword: ""                             # git 仓库密码，如果已经是 ssh 免密登录了，则为空
    gitUsername: ""                             # git 仓库用户名，如果已经是 ssh 免密登录了，则为空

  # 执行的 maven 指令集；
  pomGoals:
    -
      mavenPomxmlPath: "E://aa/pom.xml"                       # 执行指令的 pom.xml 的地址
      mavenGoals: "clean package -DskipTest=true"                      # 执行的指令
  #    -
  #      mavenPomxmlPath: "E://aa/gateway/pom.xml"                # 执行指令的 pom.xml 的地址
  #      mavenGoals: "clean package -DskipTest=true"              # 执行的指令

  # 本地替换配置文件集列表
  moveFile2Dirs:
    -
      filePathName: "D://test/yml/bootstrap.yml"                 #  配置文件的地址
      destinationDir: "E://aa/gateway/src/main/resources/"                   #  移动配置文件的目的地

  #    -
  #      filePathName: "E://aa/pom.xml"                 #  配置文件的地址
  #      destinationDir: "E://haha2/"                   #  移动配置文件的目的地

  # 远程 linux 服务器配置列表
  servers:
    -
      id: "server1"                                     # 服务器 id 应当唯一
      host: "192.168.1.188"                                   # 服务器 ip
      port:  "22"                                  #  服务器 端口
      userName: "user_01"                               #  服务器用户名
      passWord: "xxxxxxxxxxxxxxxx"                               # 服务器密码
  #    -
  #      id: server1                                     # 服务器 id 应当唯一
  #      host: 192.168.1.188                                   # 服务器 ip
  #      port: 22                                   #  服务器 端口
  #      userName: user_01                               #  服务器用户名
  #      passWord: xxxxxxxxxxxxxxxx                               # 服务器密码

  # 备份
  backups:
    -
      serverId: server1                                 # 配置的服务器 id
      backupCfgs:
        -
          remoteSrcFilePath: /home/user_01/nohup.out                  #  远程源文件目录；
          remoteBackupDestPaths:                # 远程服务备份目录；应该为列表，支持备份到多个目录
            - /home/user_01/qs/

          localPaths:                            # 本地备份目录；应该为列表，支持备份到多个目录
            - D://aa/ss/
  #        -
  #          remoteSrcFilePath: /wwwroot1/java/tomcat/webapp/app.war                  #  远程源文件目录；
  #          remoteBackupDestPaths:                # 远程服务备份目录；应该为列表，支持备份到多个目录
  #            - /wwwroot1/java/backs/app/
  #            - /wwwroot1/java/backs/app2/
  #          localPaths:                             # 本地备份目录；应该为列表，支持备份到多个目录
  #            - D://aa1/ss/
  #            - D://aa1/ss2/
  #    -
  #      serverId: server2                                 # 配置的服务器 id
  #      backupCfgs:
  #        -
  #          remoteSrcFilePath: /wwwroot2/java/tomcat/webapp/app.war                  #  远程源文件目录；
  #          remoteBackupDestPaths:                # 远程服务备份目录；应该为列表，支持备份到多个目录
  #            - /wwwroot2/java/backs/app/
  #            - /wwwroot2/java/backs/app2/
  #          localPaths:                             # 本地备份目录；应该为列表，支持备份到多个目录
  #            - D://aa2/ss/
  #            - D://aa2/ss2/
  #        -
  #          remoteSrcFilePath: /wwwroot22/java/tomcat/webapp/app.war                  #  远程源文件目录；
  #          remoteBackupDestPaths:                # 远程服务备份目录；应该为列表，支持备份到多个目录
  #            - /wwwroot22/java/backs/app/
  #            - /wwwroot22/java/backs/app2/
  #          localPaths:                             # 本地备份目录；应该为列表，支持备份到多个目录
  #            - D://aa22/ss/
  #            - D://aa22/ss2/

  # 配置文件替换
  config:
    enable: remote                                  # 配置文件替换类型；remote/local；枚举类
    # 远程配置文件替换配置
    remote:
      remoteServers:
        -
          host: 192.168.1.188                                   # 配置文件服务器 ip
          port: 22                                   #  配置文件服务器 端口
          userName: user_01                                #  配置文件服务器用户名
          passWord: xxxxxxxxxxxxxxxx                               # 配置文件服务器密码
          configCfgs:                                # 配置详情列表
            -
              remoteCfgFilePath: /home/user_01/qs/bootstrap.yml                      # 远程配置文件服务器的配置文件目录
              localDestPath: E://aa/gateway/src/main/resources/                         # 存放获取到的配置文件的本地目录
            -
              remoteCfgFilePath: /home/user_01/qs/bootstrap222.yml                     # 远程配置文件服务器的配置文件目录
              localDestPath: E://aa/gateway/src/main/resources/                          # 存放获取到的配置文件的本地目录
    #        -
    #          host: 192.168.1.188                                   # 配置文件服务器 ip
    #          port: 22                                   #  配置文件服务器 端口
    #          userName: user_01                                #  配置文件服务器用户名
    #          passWord: xxxxxxxxxxxxxxxx                               # 配置文件服务器密码
    #          configCfgs:                                # 配置详情列表
    #            -
    #              remoteCfgFilePath: /wwwroot/java/cfgs/jdbc.properties                      # 远程配置文件服务器的配置文件目录
    #              localDestPath: D://aa/base/deploy/src/main/resources                          # 存放获取到的配置文件的本地目录
    #            -
    #              remoteCfgFilePath: /wwwroot/java/cfgs/log4j.properties                      # 远程配置文件服务器的配置文件目录
    #              localDestPath: D://aa/base/deploy/src/main/resources                          # 存放获取到的配置文件的本地目录

    # 本地配置文件替换配置
    local:
      localCfgs:
        -
          filePathName: "D://test/yml/bootstrap.yml"                 #  配置文件的地址
          destinationDir: "E://aa/gateway/src/main/resources/"                    #  移动配置文件的目的地

  #        -
  #          filePathName: "E://aa/pom.xml"                 #  配置文件的地址
  #          destinationDir: "E://haha2/"                   #  移动配置文件的目的地

  # 上传打好的包
  uploadPackages:
    -
      serverId: server1                                          # 远程服务器id
      localPackagePath: E://aa/gateway/target/gateway.jar                                      #  要上传应用包所在本地目录地址
      remoteDest: /home/user_01/                                        # 远程服务器目录
      packageName: gateway.jar                                      # 上传后，在远程服务器中的命名
  #    -
  #      serverId: server2                                          # 远程服务器id
  #      localPackagePath: D://aa/base/deploy2/target/acti.war                                      #  要上传应用包所在本地目录地址
  #      remoteDest: /wwwroot/java/deploy/                                         # 远程服务器目录
  #      packageName: myacti.war                                      # 上传后，在远程服务器中的命名

  # 远程服务器执行 linux 指令集，完成部署任务
  deployInstructionSet:
    -
      serverId: server1                                          # 远程服务器id
      instructions:                                       # 执行的指令集合
        - "cd /home/user_01/"
        - "mkdir logs"
        - "nohup java -jar gateway.jar > ./logs/out.log 2>&1 &"
        - ps
  #    -
  #      serverId: server2                                          # 远程服务器id
  #      instructions:                                       # 执行的指令集合
  #        - ls
  #        - pwd
  #        - ps

  # 还原部署时备份的应用
  reductionApps:
    -
      serverId: server1                                          # 远程服务器id
      localFilePath: D://aa/backup/app.war                                      # 备份的文件本地目录地址
      remoteDestPath: /wwwroot/java/backup/app.war                                      # 备份的文件远程目录地址
      reductionTempDir: /wwwroot/java/tempdir/                           #还原包临时存放目录
      instructions:                                       # 执行的指令集合
        - ls
        - pwd
        - ps
    -
      serverId: server2                                          # 远程服务器id
      localFilePath: D://aa/backup/app.war                                      # 备份的文件本地目录地址
      remoteDestPath:  /wwwroot/java/backup/app.war                                      # 备份的文件远程目录地址
      reductionTempDir: /wwwroot/java/tempdir/                           #还原包临时存放目录
      instructions:                                       # 执行的指令集合
        - ls
        - pwd
        - ps
```