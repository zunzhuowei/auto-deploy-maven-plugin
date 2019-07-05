package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/18 16:14.
 * Description:
 */
public interface ICloneSrcService {

    /**
     *  克隆代码或者拉取最新代码
     * @param deploy 配置信息
     */
    void cloneOrPull(Deploy deploy);

}
