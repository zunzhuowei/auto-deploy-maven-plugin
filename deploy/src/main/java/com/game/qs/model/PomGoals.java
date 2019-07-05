package com.game.qs.model;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/15.
 */
public class PomGoals implements Serializable {

    // pom 所在目录；如："F://base/deploy/pom.xml";
    private String mavenPomxmlPath;

    // maven 执行的命令；如："clean package -DskipTest=true"
    private String mavenGoals;


    public String getMavenPomxmlPath() {
        return mavenPomxmlPath;
    }

    public PomGoals setMavenPomxmlPath(String mavenPomxmlPath) {
        this.mavenPomxmlPath = mavenPomxmlPath;
        return this;
    }

    public String getMavenGoals() {
        return mavenGoals;
    }

    public PomGoals setMavenGoals(String mavenGoals) {
        this.mavenGoals = mavenGoals;
        return this;
    }

    @Override
    public String toString() {
        return "PomGoals{" +
                "mavenPomxmlPath='" + mavenPomxmlPath + '\'' +
                ", mavenGoals='" + mavenGoals + '\'' +
                '}';
    }
}
