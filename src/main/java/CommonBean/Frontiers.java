package CommonBean;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.*;

public class Frontiers {

    private static Frontiers frontiers;
    private static ArrayList<Node> inFrontiers = new ArrayList<Node>();
    private static HashMap<Integer, Node> frontiersMap = new HashMap<Integer, Node>();

    private Frontiers(){}

    public static Frontiers getInstance(){
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

    public void addInFrontiers(Node node){
        inFrontiers.add(node);
    }

    public void updateFrontiers(){
        ArrayList<Integer> removeList = new ArrayList<Integer>();
        for(int i = 0; i < inFrontiers.size(); i++){
            BasicNode n = (BasicNode) inFrontiers.get(i);
            HashMap<Integer, Node> rd = n.getRasideauDegree();
            if(rd.size()>0) {
                for (HashMap.Entry entity : rd.entrySet()) {
                    if(!frontiersMap.containsKey(entity.getKey())){
                        frontiersMap.put((Integer)entity.getKey(),(Node)entity.getValue());
                    }
                }
            }else{
                removeList.add(i);
            }
        }
        for(Integer i : removeList){
            inFrontiers.remove(i);
        }
    }

    public static HashMap<Integer, Node> getFrontiersMap() {
        return frontiersMap;
    }


}
