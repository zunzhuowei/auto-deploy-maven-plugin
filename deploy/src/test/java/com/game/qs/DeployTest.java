package com.game.qs;

import com.game.qs.model.MoveFile2Dir;
import com.game.qs.model.PomGoals;
import com.game.qs.ssh.SSHClient;
import com.game.qs.ssh.SftpConnect;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by zun.wei on 2019/5/14.
 */
public class DeployTest {

    private String gitDirectory = null;

    private String gitUri = null;

    private String gitPassword = null;

    private String gitUsername = null;

    private String gitRemote = null;

    private String mavenHomePath = null;

    private String serverHost = null;
    private int serverPort = 22;
    private String serverUserName = null;
    private String serverPassword = null;

    List<PomGoals> pomGoals = null;

    private List<MoveFile2Dir> moveFile2Dirs = null;


    private SSHClient sshClient = new SSHClient();
    private SftpConnect sftpConnect = null;

    @Before
    public void setUp() {
        gitDirectory = "F://base";
        gitUri = "git@github.com:zunzhuowei/base.git";
        gitRemote = "master";
        gitPassword = "";
        gitUsername = "";

        serverHost = "192.168.1.128";
        serverPort = 22;
        serverUserName = "user_01";
        serverPassword = "tu9AVc7syYyDzP6T";

        mavenHomePath = "E:\\maven\\apache-maven-3.5.4";

        pomGoals = new LinkedList<>();
        pomGoals.add(new PomGoals()
                .setMavenPomxmlPath("F://base/pom.xml")
                .setMavenGoals("clean -DskipTest=true"));
        pomGoals.add(new PomGoals()
                .setMavenPomxmlPath("F://base/deploy/pom.xml")
                .setMavenGoals("clean package -DskipTest=true"));

        moveFile2Dirs = new LinkedList<>();
        moveFile2Dirs.add(new MoveFile2Dir()
                .setFilePathName("F://test/aabbcc.properties")
                .setDestinationDir("F://haha/"));

        moveFile2Dirs.add(new MoveFile2Dir()
                .setFilePathName("F://test/kkdd.xml")
                .setDestinationDir("F://haha1/"));


        sshClient.setHost(serverHost).setPort(serverPort).setUsername(serverUserName).setPassword(serverPassword);
        sftpConnect = new SftpConnect(serverUserName, serverPassword, serverHost, serverPort);

    }

    @After
    public void tearDown() throws Exception {
        sftpConnect.disconnect();
        sshClient.logout();
    }


    @Test
    public void cloneCodeFromGit() throws IOException, GitAPIException {
        // 判断代码是否已经存在
        String localGit = gitDirectory + "/.git";
        File localGitFile = new File(localGit);

        Git git = null;
        // 代码已存在
        if (localGitFile.isDirectory()) {
            git = Git.open(localGitFile);
        } else {
            git = Git.cloneRepository()
                    .setDirectory(new File(gitDirectory))
                    .setURI(gitUri)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitPassword, gitUsername))
                    .call();
        }

        boolean pull = git.pull().setRemoteBranchName(gitRemote).call().isSuccessful();
        System.out.println("pull = " + pull);
    }

    @Test
    public void mavenPackage() throws IOException, MavenInvocationException {
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHomePath));
        // 日志级别为错误级别
        invoker.setLogger(new PrintStreamLogger(System.err, InvokerLogger.ERROR) {
        });

        // 替换配置文件
        for (MoveFile2Dir moveFile2Dir : moveFile2Dirs) {
            FileUtils.copyFileToDirectory
                    (new File(moveFile2Dir.getFilePathName()), new File(moveFile2Dir.getDestinationDir()));
        }

        // 执行maven 命令
        for (PomGoals pomGoal : pomGoals) {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File(pomGoal.getMavenPomxmlPath()));
            request.setGoals(Collections.singletonList(pomGoal.getMavenGoals()));

            invoker.execute(request);
        }
    }

    @Test
    public void sendCmd() throws Exception {
        String ret = sshClient.sendCmd("pwd");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);

        ret = sshClient.sendCmd("vmstat");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        ret = sshClient.sendCmd("ls");
        ret = sshClient.sendCmd("tail -f nohup.out");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);

//        SftpConnect sftpConnect = new SftpConnect("user_01", "tu9AVc7syYyDzP6T", "192.168.1.128", 22);
//
//        FileInputStream fileInputStream = new FileInputStream(new File("D:\\member_visitor.sql"));
//        sftpConnect.upLoadFile("/home/user_01", "member_visitor.txt", fileInputStream);
//
//        InputStream inputStream = sftpConnect.downFile("/home/user_01", "member_visitor.txt");
//        FileUtils.copyInputStreamToFile(inputStream, new File("D:\\aa.log"));
//
//        sftpConnect.disconnect();

    }

    @Test
    public void testConnect() throws Exception {
        //String aa = sshClient.sendCmd("tail -f /home/user_01/tomcat-sc/logs/catalina.out");
        //String aa = sshClient.sendCmd("cat /home/user_01/tomcat-sc/logs/catalina.out | grep '2019-05-17'");
        String ls = sshClient.sendShellCmd("ls", "pwd");
        String dd = sshClient.sendShellCmd("ps", "netstat -anp");
    }

    @Test
    public void testScpDownload() throws Exception {
        String commandStr = "/home/user_01/tomcat-sc/logs/*.log";
        String ls = sshClient.sendCmd("ls " + commandStr + " | grep '2019-05-17'");
        String[] fileNamePaths = ls.split("\n");

        String localPathPrefix = "D://aa/cc/";
        for (String fileNamePath : fileNamePaths) {
            if (!fileNamePath.contains("/")) continue;
            String fileName = fileNamePath.substring(fileNamePath.lastIndexOf("/") + 1); //  jdbc.properties
            InputStream inputStream = sftpConnect.downFile(fileNamePath.replace(fileName, ""), fileName);
            FileUtils.copyInputStreamToFile(inputStream, new File(localPathPrefix + fileName));
        }
    }

    // 可用于获取抓取文件、图片、静态网页
    @Test
    public void test() throws IOException {
//        String url = "https://github.com/zunzhuowei/qs-hadoop/blob/master/pom.xml";
        String url = "http://pic37.nipic.com/20140113/8800276_184927469000_2.png";
        URL source = new URL(url);

        File dest = new File("F://aa/index.jpg");

        FileUtils.copyURLToFile(source, dest);

    }


}
