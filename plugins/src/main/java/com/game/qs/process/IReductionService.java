package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/20 15:27.
 * Description:
 */
public interface IReductionService {

    /**
     *  还原部署时备份的应用
     * @param deploy 配置信息
     */
    void reductionApps(Deploy deploy);


}
