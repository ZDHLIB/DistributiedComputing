package BlackVirusFinding;

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
        double distance = Integer.MAX_VALUE;

        HashMap<BasicNode, ArrayList<BasicNode> > frontiers = leaderAgent.getHop2Info();

        for(Map.Entry entry1 : frontiers.entrySet() ){
            BasicNode nodeSource = (BasicNode) entry1.getKey();
            ArrayList<BasicNode> sourceResidual = (ArrayList<BasicNode>) entry1.getValue();

            for(BasicNode node : sourceResidual){
                int tmpSize = node.getRasidualNodes().size();
                if( tmpSize < residual ){
                    res.set(0,nodeSource);
                    res.set(1, node);
                    distance = nodeSource.distance(node);
                    residual = tmpSize;
                }
                // Choose short distance
                else if( tmpSize == residual ){
                    if( nodeSource.distance( node ) < distance ){
                        res.set( 0, nodeSource );
                        res.set( 1, node );
                        distance = nodeSource.distance( node );
                        residual = tmpSize;
                    }
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


        ArrayList<BasicNode> protectedNodes = leaderAgent.getProtectedNodes();
        ArrayList<ShadowAgent> shadowPosition = leaderAgent.getShadowPosition();

        //Send shadow agents to neighours of target except last one, as it will be protected by leader
        for(int i = 0; i < protectedNodes.size()-1; i++){
            ShadowAgent shadowAgent = shadowPosition.get(i);
            BasicNode target = protectedNodes.get(i);
            //If shadow is already staying at a node, then send it to new place
            if( shadowAgent.getTarget() != null ) {
                shadowAgent.getTarget().send(target, shadowAgent);
                shadowAgent.getTarget().setShadowAgent(null);
                shadowAgent.setTarget(target);
            }else {
                shadowAgent.setTarget(target);
                currentNode.send(target, shadowAgent);
            }
        }

        //Send leader to first one to make sure that the shadow arrives
        currentNode.send(protectedNodes.get(0), leaderAgent);
        currentNode.setLeaderAgent(null);
    }


}
