package BlackVirusFinding;

import CommonBean.Agents.ExporerAgent;
import CommonBean.Agents.ShadowAgent;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;
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
            currentNode.send(leaderAgent.getTarget(), exporerAgent);
            return;
        }

        //Send shadow agents to neighours of target except last one, as it will be protected by leader
        int i = 0;
        for(BasicNode target : protectedNodes){
            ShadowAgent shadowAgent = shadowPosition.get(i);
            //If shadow is already staying at a node, then send it to new place
            if( shadowAgent.getTarget() != null ) {
                shadowAgent.getTarget().send(target, shadowAgent);
                shadowAgent.getTarget().setShadowAgent(null);
                shadowAgent.setTarget(target);
            }else {
                shadowAgent.setTarget(target);
                currentNode.send(target, shadowAgent);
            }
            i++;
        }
        logger.info("Protected nodes' size: {}", protectedNodes.size());

        //Send leader to first one to make sure that the shadow arrives
        currentNode.send(leaderAgent.getNextProtectNode(), leaderAgent);
        currentNode.setLeaderAgent(null);
    }


    public BasicNode getShortestNeigb(BasicNode node){
        BasicNode res = null;
        ArrayList<BasicNode> nodes = getExploredNeighbours(node);
        double d = Double.MAX_VALUE;
        for(BasicNode n : nodes){
            if( node.distance(n) < d ){
                res = n;
                d = node.distance(n);
            }
        }
        return res;
    }
}
