package BlackVirusFinding;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.ArrayList;
import java.util.List;

public class GreedyBasedFacade {

    /**
     * Start the greedy based algorithm
     * @param sink: sink node
     */
    public void start(BasicNode sink){
        int resideauDegree = Integer.MAX_VALUE;
        BasicNode target;
        List<Node> neighbours = sink.getNeighbors();
        for(Node n:neighbours){
            int s = ((BasicNode)n).getRasideauDegree().size();
            if( s < resideauDegree ){
                resideauDegree = s;
                target = (BasicNode)n;
            }
        }
    }
}
