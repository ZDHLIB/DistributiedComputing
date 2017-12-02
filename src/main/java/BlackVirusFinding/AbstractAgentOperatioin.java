package BlackVirusFinding;

import CommonBean.Agents.LeaderAgent;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractAgentOperatioin {
    protected LeaderAgent leaderAgent = LeaderAgent.getInstance();

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
}
