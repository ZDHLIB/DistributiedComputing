package CommonBean.Agents;

import jbotsim.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderAgent extends Agent {

    private HashMap<Integer,Node> exploredMap = new HashMap<Integer, Node>();
    private HashMap<Integer,HashMap<Integer, Node> > hop2Neighbours = new HashMap<Integer, HashMap<Integer, Node>>();

    public LeaderAgent(AgentTypeEnum type){
        this.type = type;
    }

    public boolean addNewNode(Node node){
        if(exploredMap.containsKey(node.getID())){
            System.out.println(node.getID() + "has already explored.");
        }else{
            exploredMap.put(node.getID(),node);
            return true;
        }
        return false;
    }

    public void addHop2Neighbours(Integer id, ArrayList<Node> nodes){
        if(!hop2Neighbours.containsKey(id)){
            HashMap<Integer, Node> neigs = new HashMap<Integer, Node>();
            for(Node node:nodes){
                neigs.put(node.getID(),node);
            }
            hop2Neighbours.put(id, neigs);
        }
    }
}
