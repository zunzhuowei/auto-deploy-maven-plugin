package com.game.qs.process.impl;

import com.game.qs.process.ICloneSrcService;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.GitConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by zun.wei on 2019/5/18 16:14.
 * Description:
 */
public class CloneSrcService implements ICloneSrcService {


    @Override
    public void cloneOrPull(Deploy deploy) {
        GitConfig gitConfig = deploy.getGitConfig();
        if (Objects.isNull(gitConfig)) {
            System.out.println("gitConfig is null, maybe not config!");
            return;
        }
        String gitDirectory = gitConfig.getGitDirectory();
        String gitUri = gitConfig.getGitUri();
        String gitRemote = gitConfig.getGitRemote();
        String gitUsername = gitConfig.getGitUsername();
        String gitPassword = gitConfig.getGitPassword();
        if (StringUtils.isBlank(gitDirectory)
                || StringUtils.isBlank(gitUri)) {
            System.out.println("gitDirectory or gitUri is null, maybe not config!");
            return;
        }
        gitRemote = StringUtils.isBlank(gitRemote) ? "master" : gitRemote;

        // 判断代码是否已经存在
        String localGit = gitDirectory + "/.git";
        File localGitFile = new File(localGit);

        Git git = null;
        // 代码已存在
        if (localGitFile.isDirectory()) {
            try {
                git = Git.open(localGitFile);
            } catch (IOException e) {
                System.out.println("e = " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                git = Git.cloneRepository()
                        .setDirectory(new File(gitDirectory))
                        .setURI(gitUri)
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitPassword, gitUsername))
                        .call();
            } catch (GitAPIException e) {
                System.out.println("e = " + e.getMessage());
                e.printStackTrace();
            }
        }

        boolean pull = false;
        try {
            if (Objects.nonNull(git))
                pull = git.pull().setRemoteBranchName(gitRemote).call().isSuccessful();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        System.out.println("pull complete, " + pull);

    }


}
