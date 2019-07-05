package com.game.qs.plugins;

import com.game.qs.model.PomGoals;
import com.game.qs.yaml.MoveFile2Dir;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/15 19:58.
 * Description:
 */
@Mojo(name = "packageApp",threadSafe = true)
public class PackageApp extends AbstractMojo {

    @Parameter(required = true)
    private List<PomGoals> pomGoals;

    @Parameter(required = true)
    private List<MoveFile2Dir> moveFile2Dirs;

    @Parameter(required = true)
    private String mavenHomePath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("pomGoals = " + pomGoals);
        System.out.println("moveFile2Dirs = " + moveFile2Dirs);
        System.out.println("mavenHomePath = " + mavenHomePath);

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHomePath));

        // 日志级别为错误级别
        invoker.setLogger(new PrintStreamLogger(System.err, InvokerLogger.ERROR) {});

        // 替换配置文件
        for (MoveFile2Dir moveFile2Dir : moveFile2Dirs) {
            try {
                FileUtils.copyFileToDirectory
                        (new File(moveFile2Dir.getFilePathName()), new File(moveFile2Dir.getDestinationDir()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 执行maven 命令
        for (PomGoals pomGoal : pomGoals) {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File(pomGoal.getMavenPomxmlPath()));
            request.setGoals(Collections.singletonList(pomGoal.getMavenGoals()));

            try {
                invoker.execute(request);
            } catch (MavenInvocationException e) {
                e.printStackTrace();
            }
        }
    }

}
