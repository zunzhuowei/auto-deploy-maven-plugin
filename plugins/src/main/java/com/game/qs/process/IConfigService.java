package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/18 16:44.
 * Description:
 */
public interface IConfigService {

    /**
     *  修改源码中国的配置文件
     * @param deploy 配置信息
     */
    void modifySrcCfgs(Deploy deploy);

}
