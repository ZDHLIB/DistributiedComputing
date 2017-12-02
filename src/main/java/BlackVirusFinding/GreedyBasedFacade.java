package BlackVirusFinding;

import CommonBean.Agents.*;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GreedyBasedFacade {

    private final static Logger logger = LoggerFactory.getLogger(GreedyBasedFacade.class);

    private LeaderAgent leaderAgent = LeaderAgent.getInstance();
    private GreedyBasedAgentOperatioin greedyBasedAgentOperatioin = new GreedyBasedAgentOperatioin();

    /**
     * Start one iteration of the greedy based algorithm
     */
    public void start(){
        //1. Choose target
        ArrayList<BasicNode> pair = greedyBasedAgentOperatioin.chooseTarget();
        BasicNode source = pair.get(0);
        BasicNode target = pair.get(1);
        leaderAgent.setTarget(target);
        leaderAgent.move2Source(source);
        logger.info("Choose source: {}, Choose target: {} with explored flag: {}",
                source.getID(), target.getID(), target.isExplored());

        //2. Protect explored neighbours of target
        ArrayList<BasicNode> exploredNeigs = greedyBasedAgentOperatioin.getExploredNeighbours(target);

        //3. Assign shadow agents to explored neighbours
        greedyBasedAgentOperatioin.startProtectExploredNeighbours(source, exploredNeigs);

    }


//    public void eliminate(BasicNode node, HashMap<Integer, Node> residualNeigs){
//        LeaderAgent leaderAgent = LeaderAgent.getInstance();
//        for(Map.Entry entry : residualNeigs.entrySet()){
//            BasicNode n = (BasicNode) entry.getValue();
//            ShadowAgent shadowAgent = new ShadowAgent(AgentTypeEnum.CLEANER);
//            leaderAgent.addShadowAgent(shadowAgent);
//            n.send(n, shadowAgent);
//        }
//    }


    /**
     * Receive visited message, update its own residual degree
     * @param node
     */
    public void updateResidualDegree(BasicNode node, Object obj){
        Integer visited = (Integer) obj;
        ArrayList<BasicNode> neigs = node.getRasidualNodes();
        Iterator<BasicNode> iterator = neigs.iterator();
        while(iterator.hasNext()){
            Integer id = iterator.next().getID();
            if( id.equals(visited) ){
                iterator.remove();
                return;
            }
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
        exporerAgent.setTargetNeigs(node.getRasidualNodes());
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
        leaderAgent.updateHop2Info(target, target.getRasidualNodes());
    }
}










