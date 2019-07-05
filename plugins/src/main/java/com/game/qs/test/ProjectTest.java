package com.game.qs.test;

import com.game.qs.utils.FileTools;
import com.game.qs.yaml.PluginYaml;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Created by zun.wei on 2019/5/15.
 */
@Mojo(name = "test")
public class ProjectTest extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.resources}", readonly = true, required = true)
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDir;

    @Parameter(defaultValue = "${project.build.outputDirectory}", required = true, readonly = true)
    private File outputDirectory;

    // deploy-${serviceName}-${appName}-${environment}.yml
    // deploy-happy_beard-app-dev.yml

    // 发布环境
    @Parameter(required = true)
    private String environment;

    // 服务名称
    @Parameter(required = true)
    private String serviceName;

    // 应用名称
    @Parameter(required = true)
    private String appName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String path = sourceDir.getPath();
        String abPath = sourceDir.getAbsolutePath();
        System.out.println("abPath = " + abPath);
        System.out.println("path = " + path);
        System.out.println("\"--------------------\" = " + "--------------------");

        String outputDirectoryPath = outputDirectory.getPath();
        String absolutePath = outputDirectory.getAbsolutePath();
        System.out.println("outputDirectoryPath = " + outputDirectoryPath);
        System.out.println("absolutePath = " + absolutePath);
        System.out.println("\"--------------------\" = " + "--------------------");

        Resource resource = resources.get(0);
        String directory = resource.getDirectory() + "/" + serviceName + "/" + appName + "/" + environment;
        String fileName = "deploy-" + serviceName + "-" + appName + "-" + environment + ".yml";
        Optional<PluginYaml> optional = FileTools.loadYaml(directory, fileName, PluginYaml.class);
        optional.ifPresent(dd -> System.out.println("dd = " + dd));

    }

}
