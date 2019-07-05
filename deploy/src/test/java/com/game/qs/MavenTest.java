package com.game.qs;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by zun.wei on 2019/5/14.
 */
public class MavenTest {

    /*
    代码解析：new File( "pom.xml" )：你的pom.xml文件的路径

Collections.singletonList( "compile" )：这里的compile可以改成你想要的maven命令

invoker.setMavenHome(new File("D:/apache-maven-3.3.9"))：这里填你的maven_home，也就是maven安装路径

invoker.execute( request )：验证代码是否运行成功，可以获得它的ExitCode
     */

    public static void main(String[] args) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("pom.xml"));
//        request.setGoals(Collections.singletonList("clean package install -DskipTest=true"));
        request.setGoals(Collections.singletonList("clean -DskipTest=true"));
        //request.setGoals( Collections.singletonList( "compile" ) );

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("E:\\maven\\apache-maven-3.5.4"));

        // 日志级别为错误级别
        invoker.setLogger(new PrintStreamLogger(System.err, InvokerLogger.ERROR) {});

        //maven编译的提示信息，所以我们需要重写consumeLine()方法
//        invoker.setOutputHandler(new InvocationOutputHandler() {
//            @Override
//            public void consumeLine(String s) throws IOException {
//
//            }
//        });

        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }

    }


    /*
    2、java代码执行maven命令：通过控制台实现
    Runtime runtime=Runtime.getRuntime();
    Process process=null;
    try {
         process= runtime.exec("cmd /c   cd D:\\work\\MyWordSpace\\HotSwap && mvn compile");
         process.waitFor();
         process.destroy();
    } catch (IOException e) {
         e.printStackTrace();
    }
    解析runtime.exec("cmd /k cd D:\\work\\MyWordSpace\\HotSwap && mvn compile");

    cmd /c dir：是执行完dir命令后关闭命令窗口；

    cmd /k dir：是执行完dir命令后不关闭命令窗口。

    cd D:\\work\\MyWordSpace\\HotSwap && mvn compile：这里有两个命令，两个命令之间要用&&连接

    process.destroy();记得销毁，不然程序跑久了电脑会卡
     */
}
