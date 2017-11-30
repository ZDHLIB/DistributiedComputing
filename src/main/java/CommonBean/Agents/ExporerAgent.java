package CommonBean.Agents;

import jbotsim.Node;

import java.util.ArrayList;

public class ExporerAgent extends Agent {

    private int source = 0;
    private int target = 0;
    private ArrayList<Integer> targetNeigs = new ArrayList<Integer>();

    public ExporerAgent(AgentTypeEnum type){
        this.type = type;
    }


    public ArrayList<Integer> getTargetNeigs() {
        return targetNeigs;
    }

    public void setTargetNeigs(ArrayList<Node> neigs) {
        targetNeigs.clear();
        for(Node node:neigs){
            targetNeigs.add(node.getID());
        }
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
