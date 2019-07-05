package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/17 14:51.
 * Description: 备份业务层接口
 */
public interface IBackupService {

    /**
     *  备份远程服务器的应用
     * @param deploy 配置信息
     */
    void backup(Deploy deploy);

}
