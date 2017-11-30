package BlackVirusFinding;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractAgentOperatioin {

    public abstract BasicNode chooseTarget(HashMap<Integer, Node> frontiersMap);

    public static BasicNode findNodeByID(int id, List<Node> neighbours){
        for(Node node : neighbours){
            if( node.getID() == id ){
                return (BasicNode)node;
            }
        }
        return null;
    }
}
