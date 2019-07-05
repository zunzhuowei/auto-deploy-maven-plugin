package com.game.qs.yaml;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/16.
 */
public class PluginYaml implements Serializable {

    private Deploy deploy;

    @Override
    public String toString() {
        return "PluginYaml{" +
                "deploy=" + deploy +
                '}';
    }

    public Deploy getDeploy() {
        return deploy;
    }

    public void setDeploy(Deploy deploy) {
        this.deploy = deploy;
    }

    public static void main(String[] args) {
        PluginYaml pluginYaml = new PluginYaml();
        Deploy deploy = new Deploy();
        System.out.println("pluginYaml = " + pluginYaml);
    }
}
