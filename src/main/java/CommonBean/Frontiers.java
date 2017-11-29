package CommonBean;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Frontiers {

    private static Frontiers frontiers;
    private static Queue<Node> queue = new LinkedList<Node>();
    private static HashMap<Integer, Node> frontiersMap = new HashMap<Integer, Node>();

    private Frontiers(){}

    public Frontiers getInstance(){
        if(frontiers == null){
            synchronized (Frontiers.class){
                if(frontiers == null){
                    frontiers = new Frontiers();
                    return frontiers;
                }
            }
        }
        return frontiers;
    }

    public void addQueue(Node node){
        queue.offer(node);
    }

    public void updateFrontiers(){
        for(Node node : queue){
            BasicNode n = (BasicNode) node;
            HashMap<Integer, Node> rd = n.getRasideauDegree();
            for(HashMap.Entry entity : rd.entrySet()){

            }
        }
    }

}
