package com.game.qs.process.impl;

import com.game.qs.process.IPackageService;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.PomGoal;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by zun.wei on 2019/5/18 17:39.
 * Description:
 */
public class PackageService implements IPackageService {


    @Override
    public void packageApp(Deploy deploy) {
        List<PomGoal> pomGoals = deploy.getPomGoals();
        if (Objects.isNull(pomGoals)) {
            System.out.println("pomGoals is not config!");
            return;
        }
        String mavenHome = deploy.getMavenHome();
        if (StringUtils.isBlank(mavenHome)) {
            System.out.println("MAVEN_HOME is not config!");
            return;
        }

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHome));

        // 日志级别为错误级别
        invoker.setLogger(new PrintStreamLogger(System.err, InvokerLogger.ERROR) {});

        // 执行maven 命令
        pomGoals.forEach(pomGoal -> {
            String mavenGoals = pomGoal.getMavenGoals();
            String mavenPomxmlPath = pomGoal.getMavenPomxmlPath();

            if (StringUtils.isNotBlank(mavenGoals)
                    && StringUtils.isNotBlank(mavenPomxmlPath)) {

                InvocationRequest request = new DefaultInvocationRequest();
                request.setPomFile(new File(mavenPomxmlPath));
                request.setGoals(Collections.singletonList(mavenGoals));

                try {
                    invoker.execute(request);
                } catch (MavenInvocationException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("packageApp complete !");
        });

    }


}
