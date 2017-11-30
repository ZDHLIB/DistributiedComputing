package CommonBean.Agents;

import jbotsim.Node;

import java.util.ArrayList;

public class BlackVirusAgent extends Agent {

    private int target = 0;
    private ArrayList<Integer> targetNeigs = new ArrayList<Integer>();
    private ArrayList<BlackVirusAgent> clones = new ArrayList<BlackVirusAgent>();
    private static BlackVirusAgent blackVirusAgent = null;

    private BlackVirusAgent(AgentTypeEnum type){
        this.type = type;
    }

    public static BlackVirusAgent getInstance(){
        if(blackVirusAgent == null){
            synchronized (BlackVirusAgent.class){
                if(blackVirusAgent == null){
                    blackVirusAgent = new BlackVirusAgent(AgentTypeEnum.BLACKVIRUS);
                    return blackVirusAgent;
                }
            }
        }
        return blackVirusAgent;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public ArrayList<Integer> getTargetNeigs() {
        return targetNeigs;
    }

    public void setTargetNeigs(ArrayList<Node> neigs) {
        targetNeigs.clear();
        clones.clear();
        for(Node node:neigs){
            targetNeigs.add(node.getID());
            //Only black virus can produce clones, clones are not able to clone themselves
            if(this.type == AgentTypeEnum.BLACKVIRUS){
                clones.add(new BlackVirusAgent(AgentTypeEnum.CLONESVIRUS));
            }
        }
    }
}
