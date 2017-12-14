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

import java.util.*;

public class GreedyBasedAgentOperatioin extends AbstractAgentOperatioin {

    private final static Logger logger = LoggerFactory.getLogger(GreedyBasedAgentOperatioin.class);


    /**
     * Choose target with minimum residual degree and shortest distance
     * @return
     */
    public ArrayList<BasicNode> chooseTarget(){

        logger.info("Choose target based on greedy based method.");

        ArrayList<BasicNode> res = new ArrayList<BasicNode>();
        BasicNode source = null;
        BasicNode target = null;
        res.add(source);
        res.add(target);
        int residual = Integer.MAX_VALUE;
        double dis = Integer.MAX_VALUE;

        HashMap<BasicNode, ArrayList<BasicNode> > frontiers = leaderAgent.getHop2Info();

        printMap();

        for(Map.Entry entry1 : frontiers.entrySet() ){
            BasicNode tmpSource = (BasicNode) entry1.getKey();
            ArrayList<BasicNode> sourceResidual = (ArrayList<BasicNode>) entry1.getValue();

            for(BasicNode node : sourceResidual){
                int tmpSize = node.getRasidualNodes().size();
                if( (tmpSize < residual)
                        || (tmpSize == residual && tmpSource.distance(node) < dis) ){
                    res.set(0,tmpSource);
                    res.set(1,node);
                    dis = tmpSource.distance(node);
                    residual = tmpSize;
                }
            }
        }
        return res;
    }
}
