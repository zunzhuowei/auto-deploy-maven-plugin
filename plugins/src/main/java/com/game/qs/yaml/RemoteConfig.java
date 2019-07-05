package com.game.qs.yaml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/17 9:36.
 * Description:
 */
public class RemoteConfig implements Serializable {

    private List<RemoteServer> remoteServers;


    @Override
    public String toString() {
        return "RemoteConfig{" +
                "remoteServers=" + remoteServers +
                '}';
    }

    public List<RemoteServer> getRemoteServers() {
        return remoteServers;
    }

    public void setRemoteServers(List<RemoteServer> remoteServers) {
        this.remoteServers = remoteServers;
    }
}
