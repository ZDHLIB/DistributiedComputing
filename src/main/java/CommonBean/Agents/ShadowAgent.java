package CommonBean.Agents;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.ArrayList;

public class ShadowAgent extends Agent {

    private BasicNode target = null;

    public ShadowAgent(AgentTypeEnum type){
        this.type = type;
    }


    public BasicNode getTarget() {
        return target;
    }

    public void setTarget(BasicNode target) {
        this.target = target;
    }
    
}
