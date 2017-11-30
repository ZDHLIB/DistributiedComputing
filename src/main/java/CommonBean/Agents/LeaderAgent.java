package CommonBean.Agents;

import BlackVirusFinding.Initiator;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LeaderAgent extends Agent {

    private final static Logger logger = LoggerFactory.getLogger(LeaderAgent.class);

    private static LeaderAgent leaderAgent = null;
    private ExporerAgent exporerAgent = null;
    private HashMap<Integer,Node> exploredMap = new HashMap<Integer, Node>();

    //This is the frontiers, if a node is explored, it should be moved
    private HashMap<Node,HashMap<Integer, Node> > hop2Neighbours = new HashMap<Node, HashMap<Integer, Node>>();

    private LeaderAgent(AgentTypeEnum type){
        this.type = type;
    }

    public static LeaderAgent getInstance(){
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

    public void addHop2Neighbours(Node id, HashMap<Integer,Node> neigs){
        if(!hop2Neighbours.containsKey(id) && neigs.size() > 0){
            hop2Neighbours.put(id, neigs);
        }
    }

    /**
     * filter nodes with 0 residual degree
     */
    public void updateHop2Neighbours(){

        Iterator<Node> iter = this.hop2Neighbours.keySet().iterator();
        BasicNode n = null;
        while(iter.hasNext()){
            n = (BasicNode) iter.next();
            if( n.getRasideauDegree().size() <= 0 ) {
                iter.remove();
            }
        }
    }

}
