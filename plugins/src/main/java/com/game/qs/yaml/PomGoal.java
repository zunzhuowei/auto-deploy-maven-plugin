package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/16.
 */
public class PomGoal implements Serializable {

/*
  <mavenPomxmlPath>E://aa/pom.xml</mavenPomxmlPath>
   <mavenGoals>clean -DskipTest=true</mavenGoals>
 */

    private String mavenPomxmlPath;
    private String mavenGoals;

    @Override
    public String toString() {
        return "PomGoal{" +
                "mavenPomxmlPath='" + mavenPomxmlPath + '\'' +
                ", mavenGoals='" + mavenGoals + '\'' +
                '}';
    }

    public String getMavenPomxmlPath() {
        return mavenPomxmlPath;
    }

    public void setMavenPomxmlPath(String mavenPomxmlPath) {
        this.mavenPomxmlPath = mavenPomxmlPath;
    }

    public String getMavenGoals() {
        return mavenGoals;
    }

    public void setMavenGoals(String mavenGoals) {
        this.mavenGoals = mavenGoals;
    }
}
