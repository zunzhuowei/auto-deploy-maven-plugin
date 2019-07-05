package com.game.qs.sh;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.Objects;

/**
 * Created by zun.wei on 2019/5/14 17:37.
 * Description:
 */
public class SSHClient {

    /**
     * Server Host IP Address，default value is localhost
     */
    private String host = "localhost";

    /**
     * Server SSH Port，default value is 22
     */
    private Integer port = 22;

    /**
     * SSH Login Username
     */
    private String username = "";

    /**
     * SSH Login Password
     */
    private String password = "";

    /**
     * JSch
     */
    private JSch jsch = null;

    /**
     * ssh session
     */
    private Session session = null;

    /**
     * ssh channel
     */
    private Channel channel = null;

    /**
     * timeout for session connection
     */
    private final Integer SESSION_TIMEOUT = 30000;

    /**
     * timeout for channel connection
     */
    private final Integer CHANNEL_TIMEOUT = 30000;

    /**
     * the interval for acquiring ret
     */
    private final Integer CYCLE_TIME = 250;

    public SSHClient() {
        // initialize
        jsch = new JSch();
    }

    public SSHClient setHost(String host) {
        this.host = host;
        return this;
    }

    public SSHClient setPort(Integer port) {
        this.port = port;
        return this;
    }

    public SSHClient setUsername(String username) {
        this.username = username;
        return this;
    }

    public SSHClient setPassword(String password) {
        this.password = password;
        return this;
    }


    /**
     * login to server
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {

        this.username = username;
        this.password = password;

        try {
            if (null == session) {

                session = jsch.getSession(this.username, this.host, this.port);
                session.setPassword(this.password);
                session.setUserInfo(new MyUserInfo());

                // It must not be recommended, but if you want to skip host-key check,
                // invoke following,
                session.setConfig("StrictHostKeyChecking", "no");
            }

            session.connect(SESSION_TIMEOUT);
        } catch (JSchException e) {
            e.printStackTrace();
            this.logout();
        }
    }

    /**
     * login to server
     */
    public void login() {
        this.login(this.username, this.password);
    }

    /**
     * logout of server
     */
    public void logout() {
        if (Objects.nonNull(this.channel)) {
            this.channel.disconnect();
            System.out.println("channelSftp = " + "断开连接");
        }
        if (Objects.nonNull(this.session)) {
            this.session.disconnect();
            System.out.println("disconnect = " + "断开连接");
        }
    }


    public String sendCmd(String command, String... cmdExt) throws Exception {
        StringBuilder cmd = new StringBuilder(command);
        for (String ext : cmdExt) {
            cmd.append(" ").append("&&").append(" ").append(ext);
        }
        return this.sendCmd(cmd.toString());
    }

    /**
     * send command through the ssh session,return the ret of the channel
     *
     * @return
     */
    public String sendCmd(String command) throws Exception {
        // judge whether the session or channel is connected
        ChannelExec channelExec = this.getChannelExec(command);

        InputStream in = channelExec.getInputStream();

        String ret = this.readInputStream(in);

        // close channel
        if (Objects.nonNull(this.channel)) {
            this.channel.disconnect();
        }
        in.close();
        return ret;
    }

    /**
     * 使用完该方法后，需要关闭输入流。
     *
     * @param command 命令
     * @return java.io.InputStream
     */
    public InputStream sendCommand(String command) throws Exception {
        ChannelExec channelExec = this.getChannelExec(command);
        return channelExec.getInputStream();
    }

    private ChannelExec getChannelExec(String command) throws JSchException {
        if (Objects.isNull(session) || !session.isConnected()) {
            this.login();
        }

        ChannelExec channelExec = this.getChannelExec();
        channelExec.setCommand(command);
        channelExec.connect(CHANNEL_TIMEOUT);

        // no output stream
        channelExec.setInputStream(null);

        channelExec.setErrStream(System.err);
        return channelExec;
    }

    private ChannelExec getChannelExec() throws JSchException {
        // open channel for sending command
        this.channel = session.openChannel("exec");
        return ((ChannelExec) this.channel);
    }


    private String readInputStream(InputStream in) throws IOException, InterruptedException {
        String ret = "";
        // acquire for ret
        byte[] tmp = new byte[10240];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 10240);
                if (i < 0) break;

                ret = new String(tmp, 0, i);

                System.out.println(ret);
            }

            // quit the process of waiting for ret
            if (this.channel.isClosed()) {
                if (in.available() > 0) continue;
                System.out.println("exit-status: " + this.channel.getExitStatus());
                break;
            }

            // wait every 250ms
            Thread.sleep(CYCLE_TIME);
        }
        return ret;
    }


    public String sendShellCmd(String... commands) throws Exception {
        // judge whether the session or channel is connected
        ChannelShell channelShell = this.getChannelShell();

        //写入该流的所有数据都将发送到远程端。
        OutputStream outputStream = channel.getOutputStream();
        //使用PrintWriter流的目的就是为了使用println这个方法
        //好处就是不需要每次手动给字符串加\n
//        PrintWriter printWriter = new PrintWriter(outputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bufferedOutputStream);

        for (String command : commands) {
            outputStreamWriter.write(command);
            outputStreamWriter.write("\n");
//            printWriter.println(command);
        }
        outputStreamWriter.write("exit");
        outputStreamWriter.write("\n");
        outputStreamWriter.flush();
        outputStreamWriter.close();
//        printWriter.println("exit");//加上个就是为了，结束本次交互
//        printWriter.flush();
//        printWriter.close();
        outputStream.close();



        //从远程端到达的所有数据都能从这个流中读取到
        InputStream inputStream = channel.getInputStream();
        String ret = this.readInputStream(inputStream);

        // close channel
        if (Objects.nonNull(this.channel)) {
            this.channel.disconnect();
        }
        inputStream.close();
        return ret;
    }

    private ChannelShell getChannelShell() throws JSchException {
        if (Objects.isNull(session) || !session.isConnected()) {
            this.login();
        }
        this.channel = session.openChannel("shell");
        ChannelShell channelShell =  ((ChannelShell) this.channel);
        channelShell.connect();
        channelShell.setInputStream(null);
        return channelShell;
    }

    /**
     * customized userinfo
     */
    private static class MyUserInfo implements UserInfo {
        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String s) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return false;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:" + s);
        }
    }

}
