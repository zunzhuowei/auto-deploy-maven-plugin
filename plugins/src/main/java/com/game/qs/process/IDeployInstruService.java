package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/19.
 */
public interface IDeployInstruService {

    /**
     *  发布指令集
     * @param deploy 配置信息
     */
    void deployInstrutiionSet(Deploy deploy);

}
