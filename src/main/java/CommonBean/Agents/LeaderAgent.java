package CommonBean.Agents;

import BlackVirusFinding.Initiator;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderAgent extends Agent {

    private final static Logger logger = LoggerFactory.getLogger(LeaderAgent.class);

    private static LeaderAgent leaderAgent = null;
    private ExporerAgent exporerAgent = null;
    private HashMap<Integer,Node> exploredMap = new HashMap<Integer, Node>();

    //This is the frontiers, if a node is explored, it should be moved
    private HashMap<Integer,HashMap<Integer, Node> > hop2Neighbours = new HashMap<Integer, HashMap<Integer, Node>>();

    private LeaderAgent(AgentTypeEnum type){
        this.type = type;
    }

    public LeaderAgent getInstance(){
        if(leaderAgent == null){
            synchronized (LeaderAgent.class){
                if(leaderAgent == null){
                    leaderAgent = new LeaderAgent(AgentTypeEnum.LEADER);
                    return leaderAgent;
                }
            }
        }
        return leaderAgent;
    }

    public boolean addNewNode(Node node){
        if(exploredMap.containsKey(node.getID())){
            logger.info("{} has already explored.", node.getID());
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
