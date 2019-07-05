package com.game.qs.test;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Created by zun.wei on 2019/5/14 14:25.
 * Description:
 */
//@Mojo(name = "car")
public class Car extends AbstractMojo {

    @Parameter(property = "car.url", defaultValue = "www.qq.com")
    private String url;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("hello maven plugin!");
        getLog().error("car url is ----------::" + url);

    }

}
