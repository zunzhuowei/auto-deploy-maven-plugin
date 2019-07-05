package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/18 17:38.
 * Description:
 */
public interface IPackageService {

    /**
     *  打包应用
     * @param deploy 配置信息
     */
    void packageApp(Deploy deploy);

}
