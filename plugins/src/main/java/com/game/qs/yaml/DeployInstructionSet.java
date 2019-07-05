package com.game.qs.yaml;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by zun.wei on 2019/5/17 11:37.
 * Description:
 */
public class DeployInstructionSet implements Serializable {

    private String serverId;

    private String[] instructions;


    @Override
    public String toString() {
        return "DeployInstructionSet{" +
                "serverId='" + serverId + '\'' +
                ", instructions=" + Arrays.toString(instructions) +
                '}';
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }
}
