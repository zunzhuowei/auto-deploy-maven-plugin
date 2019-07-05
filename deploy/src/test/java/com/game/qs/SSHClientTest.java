package com.game.qs;

import com.game.qs.ssh.SSHClient;
import com.game.qs.ssh.SftpConnect;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by zun.wei on 2019/5/14 17:38.
 * Description:
 */
public class SSHClientTest {

    private SSHClient sshClient = new SSHClient();

    @Before
    public void setUp() throws Exception {
        //sshClient.setHost("localServer").setPort(22).setUsername(null).setPassword(null);
        sshClient.setHost("192.168.1.128").setPort(22).setUsername("user_01").setPassword("tu9AVc7syYyDzP6T");
        sshClient.login();
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
//        ret = sshClient.sendCmd("tail -f nohup.out");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);

        SftpConnect sftpConnect = new SftpConnect("user_01", "tu9AVc7syYyDzP6T", "192.168.1.128", 22);

        FileInputStream fileInputStream = new FileInputStream(new File("D:\\member_visitor.sql"));
        sftpConnect.upLoadFile("/home/user_01", "member_visitor.txt", fileInputStream);

        InputStream inputStream = sftpConnect.downFile("/home/user_01", "member_visitor.txt");
        FileUtils.copyInputStreamToFile(inputStream, new File("D:\\aa.log"));

        sftpConnect.disconnect();

    }

    @After
    public void tearDown() throws Exception {
        sshClient.logout();
    }

}
