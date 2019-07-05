package com.game.qs.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by zun.wei on 2019/5/15 18:00.
 * Description: 克隆代码
 */
@Mojo(name = "cloneSource", threadSafe = true)
public class CloneCodeFromGit extends AbstractMojo {

    @Parameter(property = "git.directory", required = true)
    private String gitDirectory;

    @Parameter(property = "git.remote", required = true, defaultValue = "master")
    private String gitRemote;

    @Parameter(property = "git.uri", required = true)
    private String gitUri;

    @Parameter(property = "git.password", defaultValue = "")
    private String gitPassword;

    @Parameter(property = "git.username", defaultValue = "")
    private String gitUsername;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // 判断代码是否已经存在
        String localGit = gitDirectory + "/.git";
        File localGitFile = new File(localGit);

        Git git = null;
        // 代码已存在
        if (localGitFile.isDirectory()) {
            try {
                git = Git.open(localGitFile);
            } catch (IOException e) {
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
                e.printStackTrace();
            }
        }

        boolean pull = false;
        try {
            pull = git.pull().setRemoteBranchName(gitRemote).call().isSuccessful();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        System.out.println("pull = " + pull);
    }
}
