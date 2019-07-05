package com.game.qs.test;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/14 15:34.
 * Description:
 */
//@Mojo(name = "count")
public class CountMojo extends AbstractMojo {

    /*
    maven提供三个隐式变量，用来访问系统环境变量、POM信息和maven的setting

    ${basedir} 项目根目录
    ${project.build.directory} 构建目录，缺省为target
    ${project.build.outputDirectory} 构建过程输出目录，缺省为target/classes
    project.build.finalName产出物名称，缺省为 {project.build.finalName} 产出物名称，
    缺省为project.build.finalName产出物名称，缺省为{project.artifactId}-${project.version}
    ${project.packaging} 打包类型，缺省为jar
    ${project.xxx} 当前pom文件的任意节点的内容
     */

    private static final String[] INCLUDES_DEFAULT = {"properties", "xml", "java", "yml"};

    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    @Parameter(defaultValue = "${project.build.resources}", readonly = true, required = true)
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDir;

    @Parameter(defaultValue = "${project.build.testResources}", readonly = true, required = true)
    private List<Resource> testResources;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", readonly = true, required = true)
    private File testSourceDir;

    @Parameter(property = "count.include")
    private String[] includes;

    @Parameter(property = "test.param")
    private String testParam;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("baseDir目录" + baseDir);
        getLog().info("testParam" + testParam);
        if (includes.length == 0 || includes == null) {
            includes = INCLUDES_DEFAULT;
        }

        try {
            countDir(sourceDir);

            countDir(testSourceDir);

            for (Resource resource : resources) {
                countDir(new File(resource.getDirectory()));
            }

            for (Resource testResource : testResources) {
                countDir(new File(testResource.getDirectory()));
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void showInclude() {
        getLog().info("include包括" + Arrays.asList(includes));
    }

    public void countDir(File file) throws IOException {

        for (String fileType : includes) {
            getLog().info(file.getAbsolutePath()
                    .substring(baseDir.getName().length())
                    + "目录：" + fileType + "文件共计" + countFile(file, fileType));
            getLog().info(file.getAbsolutePath()
                    .substring(baseDir.getName().length())
                    + "目录" + fileType + "文件代码共计行数：" + countLine(file, fileType));
        }


    }

    public int countFile(File file, String fileType) {
        int num = 0;
        if (file.isFile() && file.getName().endsWith("." + fileType)) {
            return num++;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile() && f.getName().endsWith("." + fileType)) {
                    num++;
                } else {
                    num += countFile(f, fileType);
                }
            }
        }
        return num;
    }

    public int countLine(File file, String fileType) throws IOException {
        int countline = 0;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile() && f.getName().endsWith("." + fileType)) {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    while (br.readLine() != null) {
                        countline++;
                    }
                } else {
                    countline += countLine(f, fileType);
                }
            }
        }
        return countline;
    }

}
