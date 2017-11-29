package CommonBean.Agents;

import jbotsim.Node;

import java.util.ArrayList;

public class ShadowAgent extends Agent {

    private int target = 0;

    public ShadowAgent(AgentTypeEnum type){
        this.type = type;
    }


    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
    
}
