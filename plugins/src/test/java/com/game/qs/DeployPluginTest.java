package com.game.qs;

import com.game.qs.model.Application;
import com.game.qs.plugins.DeployPlugin;
import org.apache.maven.model.Resource;
import org.apache.maven.plugins.annotations.Parameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/20 17:48.
 * Description:
 */
public class DeployPluginTest {

    private DeployPlugin deployPlugin = new DeployPlugin();

    // resources 资源路径
    private List<Resource> resources;

    // 发布的应用
    private List<Application> applications;

    // 激活那个
    private List<String> actives;

    // 是否为回滚应用，默认fase表示部署；true为执行的是回滚应用操作；
    private boolean isBackup;

    // 配置文件前缀
    private String deployYmlPrefix;

    // 配置文件分隔符
    private String delimiter;

    @Before
    public void setUp() {
        resources = new ArrayList<>();
        Resource resource = new Resource();
        // 部署的 yml 配置文件目录
        resource.setDirectory("D:\\idea_poject\\base\\deploy\\src\\main\\resources");
        resources.add(resource);

        applications = new ArrayList<>();
        Application application = new Application();
        application.setAppName("acti");
        application.setEnvironment("dev");
        application.setServiceName("happy_beard");
        applications.add(application);

        actives = new ArrayList<>();
        actives.add("happy_beard-acti-dev");

        isBackup = false;
        deployYmlPrefix = "deploy-";
        delimiter = "-";

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDeploy() {
        deployPlugin.deploy(resources, applications, actives, isBackup, deployYmlPrefix, delimiter);

    }
}
