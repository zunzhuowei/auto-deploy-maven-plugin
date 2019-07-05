package com.game.qs.sh;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by zun.wei on 2019/5/15 15:56.
 * Description:
 */
public class SftpConnect {

    private String user;
    private String password;
    private String host;
    private int port;
    private ChannelSftp channelSftp;
    private Session session;
    //private Logger logger = LoggerFactory.getLogger(SftpConnect.class);
    private final String NO_SUCH_FILE = "No such file";

    public SftpConnect(String user, String password, String host, int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }


    private ChannelSftp connect() {
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(user, host, port);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.setConfig(sshConfig);
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        }
        return channelSftp;
    }

    /**
     *  用完之后断开连接
     * 断开连接
     */
    public void disconnect() {
        if (Objects.nonNull(channelSftp)) {
            channelSftp.disconnect();
            System.out.println("channelSftp = " + "断开连接");
        }
        if (Objects.nonNull(session)) {
            session.disconnect();
            System.out.println("disconnect = " + "断开连接");
        }
    }

    /**
     * 上传文件
     *
     * @param path     上传到服务器的目录  "/home/user_01"
     * @param filename 上传到服务器的文件名称  "member_visitor.txt"
     * @param is       输入流 如：FileInputStream fileInputStream = new FileInputStream(new File("D:\\member_visitor.sql"));
     * @return 上传是否成功
     */
    public boolean upLoadFile(String path, String filename, InputStream is) {
        if (channelSftp == null) {
            System.out.println("初始化sftp连接：连接地址：" + host);
            connect();
            System.out.println("sftp连接初始化完成：" + host);
        }
        try {
            validatePath(path);
            channelSftp.put(is, filename);
            //disconnect();
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("文件上传失败:\n" + e.toString());
            return false;
        }
        return true;
    }


    /**
     * 验证服务器文件夹路径，如不存在则新建
     *
     * @param path 目录
     */
    private void validatePath(String path) throws SftpException {
        try {
            channelSftp.lstat(path);
            channelSftp.cd(path);
        } catch (SftpException e) {
            if (NO_SUCH_FILE.equals(e.getMessage())) {
                System.out.println(path + " 不存在，创建该路径");
                String[] paths = path.split("/");
                for (String p : paths) {
                    try {
                        channelSftp.cd(p);
                    } catch (SftpException e1) {
                        channelSftp.mkdir(p);
                        channelSftp.cd(p);
                    }
                }
            } else {
                throw e;
            }
        }
    }

    /**
     * 下载文件
     *
     * @param path     文件所在服务器的目录  "/home/user_01"
     * @param filename 文件所在服务器的文件名  "member_visitor.txt"
     * @return 输入流
     */
    public InputStream downFile(String path, String filename) {
        if (channelSftp == null) {
            System.out.println("初始化sftp连接：连接地址：" + host);
            connect();
            System.out.println("sftp连接初始化完成：" + host);
        }
        try {
            channelSftp.cd(path);
            InputStream is = channelSftp.get(filename);
            //disconnect();
            return is;
        } catch (SftpException e) {
            return null;
        }
    }

}
