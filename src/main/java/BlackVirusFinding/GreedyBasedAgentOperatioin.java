package BlackVirusFinding;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.HashMap;
import java.util.Map;

public class GreedyBasedAgentOperatioin extends AbstractAgentOperatioin {

    public BasicNode chooseTarget(HashMap<Integer, Node> frontiersMap){
        BasicNode target = null;
        for(Map.Entry entry : frontiersMap.entrySet()){
            BasicNode node = (BasicNode) entry.getValue();
            if(target == null){
                target = node;
            } else if( target.getRasideauDegree().size() > node.getRasideauDegree().size() ){
                target = node;
            }
        }
        return target;
    }
}
