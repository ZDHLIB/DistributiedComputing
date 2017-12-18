package BlackVirusFinding;

import CommonBean.Agents.BlackVirusAgent;
import CommonBean.Agents.ExporerAgent;
import CommonBean.Agents.LeaderAgent;
import CommonBean.Agents.ShadowAgent;
import CommonBean.MyMessage;
import CommonBean.NodeBean.BasicNode;
import CommonBean.StatisticInfo;
import jbotsim.Node;
import jbotsim.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.QuadCurve2D;
import java.util.*;

public abstract class AbstractAgentOperatioin {
    private final static Logger logger = LoggerFactory.getLogger(AbstractAgentOperatioin.class);


    protected static LeaderAgent leaderAgent = LeaderAgent.getInstance();
    protected StatisticInfo statisticInfo = StatisticInfo.getInstance();

    /**
     * get source node and target node
     * list[0]:source
     * list[1]:target
     * @return
     */
    public abstract ArrayList<BasicNode> chooseTarget();

    public static BasicNode findNodeByID(int id, List<Node> neighbours){
        for(Node node : neighbours){
            if( node.getID() == id ){
                return (BasicNode)node;
            }
        }
        return null;
    }


    public static void sendVisited2Neighbours(BasicNode node, List<Node> neighbours){
        for(Node n : neighbours){
            BasicNode bn = (BasicNode) n;
            MyMessage mm = new MyMessage(node.getID(), null);
            node.sendRetry(bn, mm);
        }
    }

    /**
     * Use Floyd to get nearest path
     * @param source
     * @param dest
     * @return
     */
    public static Queue<BasicNode> findNearestPath(int source, int dest){
        Queue<BasicNode> route = new LinkedList<BasicNode>();
        ArrayList<BasicNode> map = leaderAgent.getExploredMap();
        Topology tp = leaderAgent.getTp();
        int size = map.size();
        double[][] mat = new double[size][size];
        BasicNode[][] path = new BasicNode[size][size];

        //Generate adjacent matrix
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if( isNeighbour(map.get(i),map.get(j)) ){
                    mat[i][j] = map.get(i).distance(map.get(j));
                    path[i][j] = map.get(j);
                }else{
                    mat[i][j] = Double.MAX_VALUE;
                    path[i][j] = null;
                }
            }
        }


        for (int k = 0; k < size; ++k) {
            for (int v = 0; v < size; ++v) {
                for (int w = 0; w < size; ++w) {
                    if (mat[v][w] > mat[v][k] + mat[k][w]) {
                        mat[v][w] = mat[v][k] + mat[k][w];
                        path[v][w] = path[v][k];
                    }
                }
            }
        }

        int id_s = getIdinMap(source);
        int id_d = getIdinMap(dest);
        Integer k = path[id_s][id_d].getID();
        BasicNode kNode = path[id_s][id_d];
        while(!k.equals(dest)){
            id_s = getIdinMap(k);
            route.offer(kNode);
            kNode = path[id_s][id_d];
            k = kNode.getID();
        }
        return route;
    }

    private static int getIdinMap(int id){
        ArrayList<BasicNode> map = leaderAgent.getExploredMap();
        for(int i = 0; i < map.size(); i++){
            if(map.get(i).getID() == id){
                return i;
            }
        }
        return 0;
    }

    public static boolean isNeighbour(BasicNode source, BasicNode dest){
        List<Node> neigs = source.getNeighbors();
        for(Node node : neigs){
            Integer a = node.getID();
            Integer b = dest.getID();
            if(a.equals(b)){
                return true;
            }
        }
        return false;
    }

    public static void send(BasicNode source, BasicNode target, Object obj){

        if( AbstractAgentOperatioin.isNeighbour(source, target)) {
            MyMessage mm = new MyMessage(obj,null);
            source.send(target, mm);
        } else {
            logger.info("Find shortest path from {} to {}", source.getID(), target.getID());
            Queue<BasicNode> path = AbstractAgentOperatioin.findNearestPath(source.getID(), target.getID());
            path.add(target);
            logger.info("Sending path:{}", path);
            MyMessage mm = new MyMessage(obj,path);
            source.sendRetry(path.poll(), mm);
        }
    }

    public static void printMap(){
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
            if( shadowAgent.getTarget() != null && shadowAgent.getTarget() != target) {
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
        statisticInfo.setTERMINATE(protectNodes.size());
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

}
