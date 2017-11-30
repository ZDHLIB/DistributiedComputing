package BlackVirusFinding;

import CommonBean.Agents.BlackVirusAgent;
import CommonBean.Agents.ExporerAgent;
import CommonBean.Agents.LeaderAgent;
import CommonBean.Frontiers;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyBasedFacade {


    private LeaderAgent leaderAgent = LeaderAgent.getInstance();

    /**
     * Start one iteration of the greedy based algorithm
     */
    public void start(){

    }

    /**
     * Receive visited message, update its own residual degree
     * @param node
     */
    public void updateResidualDegree(BasicNode node, Object obj){
        Integer visited = (Integer) obj;
        if(node.getRasideauDegree().containsKey(visited)){
            node.getRasideauDegree().remove(visited);
        }
    }

    /**
     * Send explorerAgent back with its id and neighbours
     * @param node
     * @param obj
     */
    public void returnExplorerAgent(BasicNode node, Object obj){
        ExporerAgent exporerAgent = (ExporerAgent) obj;
        exporerAgent.setTarget(node);
        exporerAgent.setTargetNeigs(node.getRasideauDegree());
        BasicNode sourcenode = (BasicNode) exporerAgent.getSource();
        node.send(sourcenode,exporerAgent);
    }

    /**
     * BlackVirusAgent sends its clones to neighbours
     * @param node
     * @param neighbours
     */
    public void sendClones2Neigbours(BasicNode node, List<Node> neighbours){
        BlackVirusAgent blackVirusAgent = node.getBlackVirusAgent();
        for(int i = 0; i < neighbours.size(); i++){
            BasicNode n = (BasicNode) neighbours.get(i);
            node.send(n,blackVirusAgent.getClones().get(i));
        }
    }

    /**
     * Receive exploreAgent back, and then leader need to update explored map information
     * @param obj
     */
    public void updateExploredInfo(Object obj){
        ExporerAgent explorerAgent = (ExporerAgent) obj;
        BasicNode target = (BasicNode) explorerAgent.getTarget();
        leaderAgent.addNewNode(target);
        leaderAgent.addHop2Neighbours(target, explorerAgent.getTargetNeigs());
        leaderAgent.updateHop2Neighbours();
    }
}










