package com.game.qs.process;

import com.game.qs.yaml.Deploy;

/**
 * Created by zun.wei on 2019/5/18 17:52.
 * Description:
 */
public interface IUploadPackageService {

    /**
     *  上传app 到远程服务器中
     * @param deploy 配置信息
     */
    void uploadApps(Deploy deploy);

}
