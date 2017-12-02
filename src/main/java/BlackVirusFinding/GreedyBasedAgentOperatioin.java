package BlackVirusFinding;

import CommonBean.Agents.AgentTypeEnum;
import CommonBean.Agents.LeaderAgent;
import CommonBean.Agents.ShadowAgent;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyBasedAgentOperatioin extends AbstractAgentOperatioin {


//    public BasicNode chooseTarget(HashMap<Integer, Node> frontiersMap){
//        BasicNode target = null;
//        for(Map.Entry entry : frontiersMap.entrySet()){
//            BasicNode node = (BasicNode) entry.getValue();
//            if(target == null){
//                target = node;
//            } else if( target.getRasideauDegree().size() > node.getRasideauDegree().size() ){
//                target = node;
//            }
//        }
//        return target;
//    }

    public ArrayList<BasicNode> chooseTarget(){
        ArrayList<BasicNode> res = new ArrayList<BasicNode>();
        BasicNode source = null;
        BasicNode target = null;
        res.add(source);
        res.add(target);
        int residual = Integer.MAX_VALUE;
        double distance = Integer.MAX_VALUE;
        HashMap<Node,HashMap<Integer, Node> > frontiers = leaderAgent.getHop2Neighbours();
        for(Map.Entry entry1 : frontiers.entrySet() ){

            BasicNode bn = (BasicNode) entry1.getKey();

            for(Map.Entry entry2 : ((HashMap<Integer, Node>) entry1.getValue()).entrySet()){
                BasicNode neigb = (BasicNode) entry2.getValue();

                //send visited to neighbour to ask neighbour to update its residual degree
                bn.send(neigb, bn.getID());

                int tmpSize = neigb.getRasideauDegree().size();
                if( tmpSize < residual ){
                    res.set( 0, bn );
                    res.set( 1, neigb );
                    distance = bn.distance( neigb );
                    residual = tmpSize;
                }
                // Choose short distance
                else if( tmpSize == residual ){
                    if( bn.distance( neigb ) < distance ){
                        res.set( 0, bn );
                        res.set( 1, neigb );
                        distance = bn.distance( neigb );
                        residual = tmpSize;
                    }
                }
            }
        }
        return res;
    }


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
        LeaderAgent leaderAgent = currentNode.getLeaderAgent();
        leaderAgent.getShadowAgents().clear();
        for(int i = 0; i < exploredNeigs.size()-1; i++){
            ShadowAgent shadowAgent = new ShadowAgent(AgentTypeEnum.CLEANER);
            shadowAgent.setTarget(exploredNeigs.get(i));
            leaderAgent.addShadowAgent(shadowAgent);
        }
        if( exploredNeigs.size() > 1 ){

            currentNode.send(leaderAgent.getShadowAgents().get(0).getTarget(), leaderAgent.getShadowAgents().get(0));
        }
        currentNode.send(exploredNeigs.get(0), leaderAgent);
    }
}
