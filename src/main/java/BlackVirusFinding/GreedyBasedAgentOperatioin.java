package BlackVirusFinding;

import CommonBean.Agents.BlackVirusAgent;
import CommonBean.Agents.ExporerAgent;
import CommonBean.Agents.LeaderAgent;
import CommonBean.Agents.ShadowAgent;
import CommonBean.MyMessage;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;
import jbotsim.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GreedyBasedAgentOperatioin extends AbstractAgentOperatioin {

    private final static Logger logger = LoggerFactory.getLogger(GreedyBasedAgentOperatioin.class);


    /**
     * Choose target with minimum residual degree and shortest distance
     * @return
     */
    public ArrayList<BasicNode> chooseTarget(){
        ArrayList<BasicNode> res = new ArrayList<BasicNode>();
        BasicNode source = null;
        BasicNode target = null;
        res.add(source);
        res.add(target);
        int residual = Integer.MAX_VALUE;
        double dis = Integer.MAX_VALUE;

        HashMap<BasicNode, ArrayList<BasicNode> > frontiers = leaderAgent.getHop2Info();

        for(Map.Entry entry : frontiers.entrySet()){
            BasicNode nodeSource = (BasicNode) entry.getKey();
            ArrayList<BasicNode> sourceResidual = (ArrayList<BasicNode>) entry.getValue();
            System.out.print(nodeSource.getID()+"={ ");
            for(BasicNode node : sourceResidual){
                System.out.print(node.getID()+" ");
            }
            System.out.println("}");
        }

        for(Map.Entry entry1 : frontiers.entrySet() ){
            BasicNode tmpSource = (BasicNode) entry1.getKey();
            ArrayList<BasicNode> sourceResidual = (ArrayList<BasicNode>) entry1.getValue();

            for(BasicNode node : sourceResidual){
                int tmpSize = node.getRasidualNodes().size();
                if( (tmpSize < residual)
                        || (tmpSize == residual && tmpSource.distance(node) < dis) ){
                    res.set(0,tmpSource);
                    res.set(1,node);
                    dis = tmpSource.distance(node);
                    residual = tmpSize;
                }
            }
        }
        return res;
    }

    /**
     * Get the explored nodes of target, these nodes should be protected before exploring
     * @param node
     * @return
     */
    public ArrayList<BasicNode> getExploredNeighbours(BasicNode node){
        ArrayList<BasicNode> res = new ArrayList<BasicNode>();
        List<Node> neighbors = node.getNeighbors();
        for(Node n : neighbors){
            if( ((BasicNode)n).isExplored() ){
                res.add( (BasicNode)n );
            }
        }
        return res;
    }


    /**
     * Protect explored neighbours of target
     * @param currentNode : the position where the leader currently is
     * @param exploredNeigs : the neighbours should be protected
     */
    public void startProtectExploredNeighbours(BasicNode currentNode, ArrayList<BasicNode> exploredNeigs){

        logger.info("LeaderAgent is currently at node {}", currentNode.getID());
        //1. Tell leader what nodes should be protected
        for(BasicNode node : exploredNeigs){
            if( node == currentNode )
                continue;
            leaderAgent.addProtectNode(node);
        }
        //current node is the last one
        leaderAgent.addProtectNode(currentNode);

        Queue<BasicNode> protectedNodes = leaderAgent.getProtectedNodes();
        ArrayList<ShadowAgent> shadowPosition = leaderAgent.getShadowPosition();

        if( protectedNodes.size() == 1 ){
            leaderAgent.getProtectedNodes().poll();
            ExporerAgent exporerAgent = ExporerAgent.getInstance();
            exporerAgent.setSource(currentNode);
            exporerAgent.setTarget(leaderAgent.getTarget());
            MyMessage mm = new MyMessage(exporerAgent, null);
            currentNode.send(leaderAgent.getTarget(), mm);
            return;
        }

        //Send shadow agents to neighours of target except last one, as it will be protected by leader
        int i = 0;
        for(BasicNode target : protectedNodes){
            ShadowAgent shadowAgent = shadowPosition.get(i);
            //If shadow is already staying at a node, then send it to new place
            if( shadowAgent.getTarget() != null ) {
                AbstractAgentOperatioin.send(shadowAgent.getTarget(),target, shadowAgent);
                shadowAgent.getTarget().setShadowAgent(null);
                shadowAgent.setTarget(target);
            }else {
                shadowAgent.setTarget(target);
                AbstractAgentOperatioin.send(currentNode,target,shadowAgent);
            }
            i++;
            if( i == protectedNodes.size()-1 ){
                break;
            }
        }
        logger.info("Protected nodes' size: {}", protectedNodes.size());

        //Send leader to first one to make sure that the shadow arrives
        AbstractAgentOperatioin.send(currentNode,leaderAgent.getNextProtectNode(), leaderAgent);
        currentNode.setLeaderAgent(null);
    }


    public void eliminate(BasicNode node){
        LeaderAgent leaderAgent = LeaderAgent.getInstance();
        Queue<BasicNode> protectNodes = leaderAgent.getProtectedNodes();
        int i = 0;
        for(BasicNode item : protectNodes){
            ShadowAgent shadowAgent = leaderAgent.getShadowPosition().get(i);
            if(shadowAgent.getTarget() != null){
                logger.info("{} sends shadowAgent to eliminate node {}", shadowAgent.getTarget().getID(), item.getID());
                AbstractAgentOperatioin.send(shadowAgent.getTarget(),item, shadowAgent);
                shadowAgent.getTarget().setShadowAgent(null);
                shadowAgent.setTarget(item);
            }else{
                logger.info("{} sends shadowAgent to eliminate node {}", node.getID(), item.getID());
                AbstractAgentOperatioin.send(node, item, shadowAgent);
                shadowAgent.setTarget(item);
            }
            i++;
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
        MyMessage mm = new MyMessage(exporerAgent, null);
        node.send(sourcenode,mm);
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
            MyMessage mm = new MyMessage(blackVirusAgent.getClones().get(i), null);
            node.send(n,mm);
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

    public boolean checkTermination(){
        if(leaderAgent.getExploredMap().size() == leaderAgent.getTp().getNodes().size()){
            return true;
        }else{
            return false;
        }
    }

}
