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
    public void startExplorer(){
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

    /**
     * Eliminate the residual nodes of target
     * @param node: leader is here
     * @param target: black virus
     */
    public void startElimination(BasicNode node, BasicNode target){
        ArrayList<BasicNode> residualNodes = target.getRasidualNodes();
        for(BasicNode n : residualNodes){
            node.getLeaderAgent().addProtectNode(n);
        }
        eliminate(node);
    }


    public void dealWithInteger(BasicNode node, Integer id){
        updateResidualDegree(node, id);
        if( node.isExplored() ) {
            leaderAgent.updateHop2Info(node, node.getRasidualNodes());
        }
    }


    public void dealWithExplorerAgent(BasicNode node, ExporerAgent exporerAgent){
        // The explorer I received is what I sent, update explored info, then startExplorer next stage
        if( node.isExplored() ){
            BasicNode target = (BasicNode) exporerAgent.getTarget();
            updateExploredInfo(target);
            startExplorer();
        }
        //I am not explored, so I receive it for the first time
        //If I have been destroyed by black virus
        else if( node.getBlackVirusAgent() != null
                && node.getBlackVirusAgent().isActivate()) {
            List<Node> neighbours = node.getNeighbors();
            node.getBlackVirusAgent().setTargetNeigs(neighbours);
            node.getBlackVirusAgent().setActivate(false);
            sendClones2Neigbours(node, neighbours);
        }
        //I am a safe node, set to be explored, and send visited to neighbours, send explorer back
        else {
            node.setExplored(true);
            List<Node> neighbours = node.getNeighbors();
            AbstractAgentOperatioin.sendVisited2Neighbours(node, neighbours);
            returnExplorerAgent(node, exporerAgent);
        }
    }


    public void dealWithBlackClonesAgent(BasicNode node, BlackVirusAgent blackVirusAgent){
        // I have been protected by shadowAgent
        if( node.getShadowAgent() != null ){
            blackVirusAgent.setActivate(false);
        }
        // I have been protected by leaderAgent, startExplorer eliminate phase
        else if(node.getLeaderAgent() != null){
            blackVirusAgent.setActivate(false);
            //TODO startExplorer eliminate phase
            startElimination(node, node.getLeaderAgent().getTarget());
            node.getLeaderAgent().updateHop2Info(node.getLeaderAgent().getTarget(), node.getLeaderAgent().getTarget().getRasidualNodes());
            startExplorer();
        }
        // I have not been explored, so I have not been protected
        else {
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
                BasicNode protectNode = node.getLeaderAgent().getNextProtectNode();
                node.send(protectNode, node.getLeaderAgent());
                node.setLeaderAgent(null);
            }
        }
        // I have not been explored, if I have been destroyed by clones, then deactivate it
        else {
            if( node.getBlackVirusAgent() != null
                    && node.getBlackVirusAgent().getType().equals(AgentTypeEnum.CLONESVIRUS)){
                node.getBlackVirusAgent().setActivate(false);
            }
        }
    }


    public void dealWithLeaderAgent(BasicNode node){
        if( node.isExplored() ){
            // Make sure shadowAgent has been here, then go to next node
            if( node.getShadowAgent() != null ){
                BasicNode protectNode = node.getLeaderAgent().getNextProtectNode();
                node.send(protectNode, leaderAgent);
                node.setLeaderAgent(null);
            }
            //if it is the last stop, leader just stay here and send explorer to target,
            //or wait for shadowAgent
            else {
                BasicNode protectNode = node.getLeaderAgent().getNextProtectNode();
                if(protectNode == null){
                    node.setLeaderAgent(leaderAgent);
                    ExporerAgent exporerAgent = ExporerAgent.getInstance();
                    exporerAgent.setSource(node);
                    exporerAgent.setTarget(leaderAgent.getTarget());
                    node.send(leaderAgent.getTarget(), exporerAgent);
                }
            }
        }
    }


    public void eliminate(BasicNode node){
        LeaderAgent leaderAgent = LeaderAgent.getInstance();
        Queue<BasicNode> protectNodes = leaderAgent.getProtectedNodes();
        while(!protectNodes.isEmpty()){
            BasicNode item = protectNodes.poll();
            ShadowAgent shadowAgent = item.getShadowAgent();
            if(shadowAgent.getTarget() != null){
                shadowAgent.getTarget().send(item, shadowAgent);
                shadowAgent.getTarget().setShadowAgent(null);
                shadowAgent.setTarget(item);
            }else{
                node.send(item, shadowAgent);
                shadowAgent.setTarget(item);
            }
        }
    }


    /**
     * Receive visited message, update its own residual degree
     * @param node
     */
    public void updateResidualDegree(BasicNode node, Integer visited){
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
     * @param exporerAgent
     */
    public void returnExplorerAgent(BasicNode node, ExporerAgent exporerAgent){
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
     * @param target
     */
    public void updateExploredInfo(BasicNode target){
        leaderAgent.addNewNode(target);
        leaderAgent.updateHop2Info(target, target.getRasidualNodes());
    }
}










