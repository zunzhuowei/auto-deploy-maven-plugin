package com.game.qs.plugins;

import com.alibaba.fastjson.JSON;
import com.game.qs.model.Application;
import com.game.qs.process.*;
import com.game.qs.process.impl.*;
import com.game.qs.utils.FileTools;
import com.game.qs.yaml.Deploy;
import com.game.qs.yaml.PluginYaml;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by zun.wei on 2019/5/16 19:15.
 * Description:
 */
@Mojo(name = "deploy", threadSafe = true)
public class DeployPlugin extends AbstractMojo {

    // resources 资源路径
    @Parameter(defaultValue = "${project.build.resources}", readonly = true, required = true)
    private List<Resource> resources;

    // 发布的应用
    @Parameter(required = true)
    private List<Application> applications;

    // 激活那个
    @Parameter(required = true)
    private List<String> actives;

    // 是否为回滚应用，默认fase表示部署；true为执行的是回滚应用操作；
    @Parameter(defaultValue = "false")
    private boolean isBackup;

    // 配置文件前缀
    @Parameter(defaultValue = "deploy-")
    public String deployYmlPrefix;

    // 配置文件分隔符
    @Parameter(defaultValue = "-")
    public String delimiter;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.deploy(resources, applications, actives, isBackup, deployYmlPrefix, delimiter);
    }

    public void deploy(List<Resource> resources, List<Application> applications,
                        List<String> actives, boolean isBackup,
                        String deployYmlPrefix, String delimiter) {

        List<PluginYaml> pluginYamls = new LinkedList<>();

        actives.forEach(active -> {
            List<Application> apps = applications.stream()
                    .filter(application -> {
                        String expectActive =
                                application.getServiceName() + delimiter
                                + application.getAppName() + delimiter
                                + application.getEnvironment();
                        return StringUtils.equals(expectActive, active);
                    }).collect(toList());

            if (apps.isEmpty()) {
                throw new RuntimeException("please config [environment],[serviceName],[appName],[" + active + "]");
            }
            if (apps.size() > 1) {
                throw new RuntimeException("[environment],[serviceName],[appName] must be unique!");
            }

            Application application = apps.get(0);
            String appName = application.getAppName();
            String environment = application.getEnvironment();
            String serviceName = application.getServiceName();

            // 获取第一个资源目录
            Resource resource = resources.get(0);
            // 配置文件的名称
            String fileName = deployYmlPrefix + serviceName + delimiter + appName + delimiter + environment + ".yml";
            // 资源目录
            String directory = resource.getDirectory() + "/" + serviceName + "/" + appName + "/" + environment;
            // 获取配置文件里的配置
            Optional<PluginYaml> pluginYamlOptional = FileTools.loadYaml(directory, fileName, PluginYaml.class);

            if (!pluginYamlOptional.isPresent()) {
                throw new RuntimeException("deploy application [" + active + "] ,but not fund "
                        + "[" + fileName + "]"
                        + " in resources package [" + directory + "]");
            }

            PluginYaml pluginYaml = pluginYamlOptional.get();
            Deploy deploy = pluginYaml.getDeploy();
            if (Objects.isNull(deploy)) {
                throw new RuntimeException("read deploy object is null !");
            }
            System.out.println("pluginYaml json = " + JSON.toJSONString(pluginYaml, true));

            pluginYamls.add(pluginYaml);
        });

        System.out.println("66666666666666 pluginYamls = " + pluginYamls);
        pluginYamls.forEach(pluginYaml -> {
            Deploy deploy = pluginYaml.getDeploy();

            // 备份应用
            IBackupService backupService = new BackupService();
            // 克隆代码
            ICloneSrcService cloneSrcService = new CloneSrcService();
            // 替换配置文件
            IConfigService configService = new ConfigService();
            // 打包应用
            IPackageService packageService = new PackageService();
            // 上传应用
            IUploadPackageService uploadPackageService = new UploadPackageService();
            // 执行发布指令
            IDeployInstruService deployInstruService = new DeployInstruService();
            // 执行还原应用
            IReductionService reductionService = new ReductionService();

            if (isBackup) {
                reductionService.reductionApps(deploy);
            } else {
//                backupService.backup(deploy);
                cloneSrcService.cloneOrPull(deploy);
                configService.modifySrcCfgs(deploy);
//                packageService.packageApp(deploy);
//                uploadPackageService.uploadApps(deploy);
//                deployInstruService.deployInstrutiionSet(deploy);
            }
        });
    }


}
