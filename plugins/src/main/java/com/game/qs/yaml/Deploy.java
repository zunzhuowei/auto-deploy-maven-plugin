package com.game.qs.yaml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/16.
 */

public class Deploy implements Serializable {

    private GitConfig gitConfig;

    private String mavenHome;

    private List<PomGoal> pomGoals;

    private List<MoveFile2Dir> moveFile2Dirs;

    private List<Server> servers;

    private Config config;

    private List<Backups> backups;

    private List<AppPackage> uploadPackages;

    private List<DeployInstructionSet> deployInstructionSet;

    private List<ReductionApp> reductionApps;

    @Override
    public String toString() {
        return "Deploy{" +
                "gitConfig=" + gitConfig +
                ", mavenHome='" + mavenHome + '\'' +
                ", pomGoals=" + pomGoals +
                ", moveFile2Dirs=" + moveFile2Dirs +
                ", servers=" + servers +
                ", config=" + config +
                ", backups=" + backups +
                ", uploadPackages=" + uploadPackages +
                ", deployInstructionSet=" + deployInstructionSet +
                ", reductionApps=" + reductionApps +
                '}';
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public List<ReductionApp> getReductionApps() {
        return reductionApps;
    }

    public void setReductionApps(List<ReductionApp> reductionApps) {
        this.reductionApps = reductionApps;
    }

    public List<DeployInstructionSet> getDeployInstructionSet() {
        return deployInstructionSet;
    }

    public void setDeployInstructionSet(List<DeployInstructionSet> deployInstructionSet) {
        this.deployInstructionSet = deployInstructionSet;
    }

    public List<AppPackage> getUploadPackages() {
        return uploadPackages;
    }

    public void setUploadPackages(List<AppPackage> uploadPackages) {
        this.uploadPackages = uploadPackages;
    }

    public List<Backups> getBackups() {
        return backups;
    }

    public void setBackups(List<Backups> backups) {
        this.backups = backups;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public GitConfig getGitConfig() {
        return gitConfig;
    }

    public void setGitConfig(GitConfig gitConfig) {
        this.gitConfig = gitConfig;
    }

    public String getMavenHome() {
        return mavenHome;
    }

    public void setMavenHome(String mavenHome) {
        this.mavenHome = mavenHome;
    }

    public List<PomGoal> getPomGoals() {
        return pomGoals;
    }

    public void setPomGoals(List<PomGoal> pomGoals) {
        this.pomGoals = pomGoals;
    }

    public List<MoveFile2Dir> getMoveFile2Dirs() {
        return moveFile2Dirs;
    }

    public void setMoveFile2Dirs(List<MoveFile2Dir> moveFile2Dirs) {
        this.moveFile2Dirs = moveFile2Dirs;
    }


}
