package com.game.qs.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/14 17:21.
 * Description:
 */
public class GitTest {

    public static void main2(String[] args) throws GitAPIException, IOException, URISyntaxException {
        Git git = Git.cloneRepository()
                .setDirectory(new File("F://qs/"))
                .setURI("git@github.com:zunzhuowei/autojs.git")
                .setCredentialsProvider(provide())
                .call();

        // 检出并且创建新分支
//        git.checkout()
//                .setCreateBranch(true)
//                .setName("your-branch")
//                .call();

        File file = git.getRepository().getDirectory();
        String projectPath = file.getParent();
        System.out.println("projectPath = " + projectPath);
        System.out.println("getPath = " + file.getPath());
        pushFiles2(file);
    }

    private static UsernamePasswordCredentialsProvider provide() {
        return new UsernamePasswordCredentialsProvider("", "");
    }


    private void pushFiles(File file) throws GitAPIException, URISyntaxException {
        Git git = Git.init().setDirectory(file).call();
        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish("https://github.com/zunzhuowei/interface.git"));
        remoteAddCommand.call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("init").call();
        git.push().setCredentialsProvider(provide()).call();
    }


    public static void main(String[] args) throws IOException, GitAPIException, URISyntaxException {
        File file = new File("autojs\\.git");
        boolean b = file.isDirectory();
        System.out.println("b = " + b);

        //pushFiles2(file);
    }

    private static void pushFiles2(File file) throws GitAPIException, URISyntaxException, IOException {
        Git git = Git.open(file);
        List<RemoteConfig> remoteConfigs = git.remoteList().call();
        remoteConfigs.forEach(System.out::println);

        Iterable<RevCommit> revCommits = git.log().all().call();
        revCommits.forEach(e -> System.out.println("e.getFullMessage() = " + e.getFullMessage()));
    }

}
