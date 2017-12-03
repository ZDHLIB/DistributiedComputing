package BlackVirusFinding;

import CommonBean.Agents.*;
import CommonBean.MyMessage;
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
    public void startExplorer(){
        //1. Choose target
        ArrayList<BasicNode> pair = greedyBasedAgentOperatioin.chooseTarget();
        BasicNode source = pair.get(0);
        BasicNode target = pair.get(1);
        leaderAgent.setTarget(target);
        leaderAgent.move2Source(source);
        logger.info("Source {} choose target {} with explored flag: {}",
                source.getID(), target.getID(), target.isExplored());

        //2. Protect explored neighbours of target
        ArrayList<BasicNode> exploredNeigs = greedyBasedAgentOperatioin.getExploredNeighbours(target);

        //3. Assign shadow agents to explored neighbours
        greedyBasedAgentOperatioin.startProtectExploredNeighbours(source, exploredNeigs);

    }

    /**
     * Eliminate the residual nodes of target
     * @param node: leader is here
     * @param target: black virus
     */
    public void startElimination(BasicNode node, BasicNode target){
        logger.info("Start elimination phase.");
        ArrayList<BasicNode> residualNodes = target.getRasidualNodes();
        for(BasicNode n : residualNodes){
            node.getLeaderAgent().addProtectNode(n);
            greedyBasedAgentOperatioin.updateExploredInfo(n);
        }
        if(node.getLeaderAgent().getProtectedNodes().isEmpty()){
            logger.info("No nodes will be destroyed by clones, finished!");
        } else {
            greedyBasedAgentOperatioin.eliminate(node);
        }
    }


    public void dealWithInteger(BasicNode node, Integer id){
        greedyBasedAgentOperatioin.updateResidualDegree(node, id);
        if( node.isExplored() ) {
            leaderAgent.updateHop2Info(node, node.getRasidualNodes());
        }
    }


    public void dealWithExplorerAgent(BasicNode node, ExporerAgent exporerAgent){

        // The explorer I received is what I sent, update explored info, then startExplorer next stage
        if( node.isExplored() ){
            BasicNode target = (BasicNode) exporerAgent.getTarget();
            greedyBasedAgentOperatioin.updateExploredInfo(target);
            startExplorer();
        }
        //I am not explored, so I receive it for the first time
        //If I have been destroyed by black virus
        else if( node.getBlackVirusAgent() != null
                && node.getBlackVirusAgent().isActivate()) {
            List<Node> neighbours = node.getNeighbors();
            AbstractAgentOperatioin.sendVisited2Neighbours(node, neighbours);
            node.getBlackVirusAgent().setTargetNeigs(neighbours);
            node.getBlackVirusAgent().setActivate(false);
            greedyBasedAgentOperatioin.sendClones2Neigbours(node, neighbours);
            greedyBasedAgentOperatioin.updateExploredInfo(node);
            logger.info("I am BlackVirus node {}, and have been deactivated by explorer", node.getID());
        }
        //I am a safe node, set to be explored, and send visited to neighbours, send explorer back
        else {
            node.setExplored(true);
            List<Node> neighbours = node.getNeighbors();
            AbstractAgentOperatioin.sendVisited2Neighbours(node, neighbours);
            greedyBasedAgentOperatioin.returnExplorerAgent(node, exporerAgent);
        }
    }


    public void dealWithBlackClonesAgent(BasicNode node, BlackVirusAgent blackVirusAgent){
        // I have been protected by shadowAgent
        if( node.getShadowAgent() != null ){
            logger.info("{} has shadowAgent, deactivate receiving clone", node.getID());
            blackVirusAgent.setActivate(false);
        }
        // I have been protected by leaderAgent, startExplorer eliminate phase
        else if(node.getLeaderAgent() != null){
            logger.info("{} has leaderAgent, deactivate receiving clone", node.getID());
            blackVirusAgent.setActivate(false);
            startElimination(node, node.getLeaderAgent().getTarget());
        }
        // I have not been explored, so I have not been protected
        else {
            logger.info("{} do not have been protected,destroyed by receiving clone", node.getID());
            node.setBlackVirusAgent(blackVirusAgent);
        }
    }


    public void dealWithShadowAgent(BasicNode node, ShadowAgent shadowAgent){
        if( node.isExplored() ){
            // Waiting for leaderAgent
            if( node.getLeaderAgent() == null ){
                node.setShadowAgent(shadowAgent);
            }
            //Leader has been here for waiting for shadowAgent, then it will go to next protected node
            else {
                node.setShadowAgent(shadowAgent);
                BasicNode protectNode = node.getLeaderAgent().getNextProtectNode();
                AbstractAgentOperatioin.send(node,protectNode, node.getLeaderAgent());
                node.setLeaderAgent(null);
            }
        }
        // I have not been explored, if I have been destroyed by clones, then deactivate it
        else {
            if( node.getBlackVirusAgent() != null
                    && node.getBlackVirusAgent().getType().equals(AgentTypeEnum.CLONESVIRUS)){
                node.getBlackVirusAgent().setActivate(false);
                logger.info("{} node's black virus clone has been deactivate.", node.getID());
            }
        }
    }


    public void dealWithLeaderAgent(BasicNode node){
        // Make sure shadowAgent has been here, then go to next node
        if( node.getShadowAgent() != null ){
            node.getLeaderAgent().setCurrentNode(node);
            BasicNode protectNode = node.getLeaderAgent().getNextProtectNode();
            AbstractAgentOperatioin.send(node,protectNode, leaderAgent);
            node.setLeaderAgent(null);
        }
        //if it is the last stop, leader just stay here and send explorer to target,
        //or wait for shadowAgent
        else {
            if(node.getLeaderAgent().getProtectedNodes().isEmpty()){
                node.setLeaderAgent(leaderAgent);
                node.getLeaderAgent().setCurrentNode(node);
                ExporerAgent exporerAgent = ExporerAgent.getInstance();
                exporerAgent.setSource(node);
                exporerAgent.setTarget(leaderAgent.getTarget());
                MyMessage mm = new MyMessage(exporerAgent, null);
                node.send(leaderAgent.getTarget(), mm);
            }
        }
    }



}










