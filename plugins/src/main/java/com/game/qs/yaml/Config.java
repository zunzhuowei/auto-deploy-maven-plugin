package com.game.qs.yaml;

import com.game.qs.enum0.ConfigType;

import java.io.Serializable;

/**
 * Created by zun.wei on 2019/5/17 9:31.
 * Description:
 */
public class Config implements Serializable {

    private ConfigType enable;

    private RemoteConfig remote;

    private LocalConfig local;

    public ConfigType getEnable() {
        return enable;
    }

    public void setEnable(ConfigType enable) {
        this.enable = enable;
    }

    public RemoteConfig getRemote() {
        return remote;
    }

    public void setRemote(RemoteConfig remote) {
        this.remote = remote;
    }

    public LocalConfig getLocal() {
        return local;
    }

    public void setLocal(LocalConfig local) {
        this.local = local;
    }
}
