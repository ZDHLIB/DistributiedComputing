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

import java.awt.geom.QuadCurve2D;
import java.util.*;

public abstract class AbstractAgentOperatioin {
    private final static Logger logger = LoggerFactory.getLogger(AbstractAgentOperatioin.class);


    protected static LeaderAgent leaderAgent = LeaderAgent.getInstance();

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
            node.send(bn, mm);
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
        int size = tp.getNodes().size();
        int[][] mat = new int[size][size];
        BasicNode[][] path = new BasicNode[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                mat[i][j] = size * 2;
            }
        }

        //Generate adjacent matrix
        for(BasicNode node : map){
            mat[node.getID()][node.getID()] = 1;
            for( Node node1 : node.getNeighbors()){
                BasicNode n = (BasicNode) node1;
                mat[node.getID()][n.getID()] = 1;
                path[node.getID()][n.getID()] = n;
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

        int k = path[source][dest].getID();
        BasicNode kNode = path[source][dest];
        while(k!=dest){
            route.offer(kNode);
            k = path[k][dest].getID();
            kNode = path[k][dest];
        }
        return route;
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
            Queue<BasicNode> path = AbstractAgentOperatioin.findNearestPath(source.getID(), target.getID());
            path.add(target);
            logger.info("Sending path:{}", path);
            MyMessage mm = new MyMessage(obj,path);
            source.send(path.poll(), mm);
        }
    }
}
