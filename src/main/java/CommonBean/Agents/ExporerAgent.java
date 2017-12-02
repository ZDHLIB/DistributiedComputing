package CommonBean.Agents;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExporerAgent extends Agent {

    private Node source = null;
    private Node target = null;
    private ArrayList<BasicNode> targetNeigs = new ArrayList<BasicNode>();
    private static ExporerAgent exporerAgent = null;

    private ExporerAgent(AgentTypeEnum type){
        this.type = type;
    }


    public ArrayList<BasicNode> getTargetNeigs() {
        return targetNeigs;
    }

    public void setTargetNeigs(ArrayList<BasicNode> neigs) {
        targetNeigs.clear();
        for(BasicNode node : neigs){
            targetNeigs.add(node);
        }
    }

    public static ExporerAgent getInstance(){
        if(exporerAgent == null){
            synchronized (LeaderAgent.class){
                if(exporerAgent == null){
                    exporerAgent = new ExporerAgent(AgentTypeEnum.LEADER);
                    return exporerAgent;
                }
            }
        }
        return exporerAgent;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }
}
