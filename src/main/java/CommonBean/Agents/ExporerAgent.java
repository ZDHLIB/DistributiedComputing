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
    private HashMap<Integer, Node> targetNeigs = new HashMap<Integer, Node>();

    public ExporerAgent(AgentTypeEnum type){
        this.type = type;
    }


    public HashMap<Integer, Node> getTargetNeigs() {
        return targetNeigs;
    }

    public void setTargetNeigs(HashMap<Integer, Node> neigs) {
        targetNeigs.clear();
        for(Map.Entry entry : neigs.entrySet()){
            targetNeigs.put( (Integer) entry.getKey(), (BasicNode) entry.getValue());
        }
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
