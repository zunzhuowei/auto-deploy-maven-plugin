package com.game.qs.yaml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zun.wei on 2019/5/17 9:36.
 * Description:
 */
public class LocalConfig implements Serializable {

    private List<LocalCfg> localCfgs;

    @Override
    public String toString() {
        return "LocalConfig{" +
                "localCfgs=" + localCfgs +
                '}';
    }

    public List<LocalCfg> getLocalCfgs() {
        return localCfgs;
    }

    public void setLocalCfgs(List<LocalCfg> localCfgs) {
        this.localCfgs = localCfgs;
    }
}
