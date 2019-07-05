package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/16.
 */
public class GitConfig implements Serializable {

/*
<gitDirectory>E://aa</gitDirectory>
<gitUri>git@github.com:zunzhuowei/base.git</gitUri>
<gitRemote>master</gitRemote>
<gitPassword>""</gitPassword>
<gitUsername>""</gitUsername>
 */

    private String gitDirectory;

    private String gitUri;

    private String gitRemote;

    private String gitPassword;

    private String gitUsername;

    public String getGitDirectory() {
        return gitDirectory;
    }

    public void setGitDirectory(String gitDirectory) {
        this.gitDirectory = gitDirectory;
    }

    public String getGitUri() {
        return gitUri;
    }

    public void setGitUri(String gitUri) {
        this.gitUri = gitUri;
    }

    public String getGitRemote() {
        return gitRemote;
    }

    public void setGitRemote(String gitRemote) {
        this.gitRemote = gitRemote;
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    @Override
    public String toString() {
        return "GitConfig{" +
                "gitDirectory='" + gitDirectory + '\'' +
                ", gitUri='" + gitUri + '\'' +
                ", gitRemote='" + gitRemote + '\'' +
                ", gitPassword='" + gitPassword + '\'' +
                ", gitUsername='" + gitUsername + '\'' +
                '}';
    }
}
