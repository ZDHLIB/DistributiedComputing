package BlackVirusFinding;

import CommonBean.Agents.BlackVirusAgent;
import CommonBean.Agents.ExporerAgent;
import CommonBean.Agents.LeaderAgent;
import CommonBean.Agents.ShadowAgent;
import CommonBean.MyMessage;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ThresholdBasedAgentOperatioin extends AbstractAgentOperatioin {

    private final static Logger logger = LoggerFactory.getLogger(ThresholdBasedAgentOperatioin.class);


    /**
     * Choose target with the shortest distance and its residual degree below the threshold
     * @return
     */
    public ArrayList<BasicNode> chooseTarget(){

        logger.info("Choose target based on threshold based method.");

        ArrayList<BasicNode> res = new ArrayList<BasicNode>();
        BasicNode source = null;
        BasicNode target = null;
        res.add(source);
        res.add(target);
        double dis = Integer.MAX_VALUE;

        HashMap<BasicNode, ArrayList<BasicNode> > frontiers = leaderAgent.getHop2Info();

        printMap();

        ArrayList<BasicNode> tmpMin =  getMinimumResidual(frontiers);
        if(tmpMin.get(1).getRasidualNodes().size() > leaderAgent.getThreshold()){
            logger.info("Set new threshold from {} to {}", leaderAgent.getThreshold(),tmpMin.get(1).getRasidualNodes().size());
            leaderAgent.setThreshold(tmpMin.get(1).getRasidualNodes().size());
            res.set(0,tmpMin.get(0));
            res.set(1,tmpMin.get(1));
            return res;
        }

        logger.info("Unchanged threshold {}, choose the shortest one.", leaderAgent.getThreshold());
        for(Map.Entry entry1 : frontiers.entrySet() ){
            BasicNode tmpSource = (BasicNode) entry1.getKey();
            ArrayList<BasicNode> sourceResidual = (ArrayList<BasicNode>) entry1.getValue();

            for(BasicNode node : sourceResidual){
                int tmpSize = node.getRasidualNodes().size();
                if( tmpSize <= leaderAgent.getThreshold()){
                    node.setExplored(true);
                    leaderAgent.addNewNode(node);
                    int tempDis = findNearestPath(leaderAgent.getCurrentNode().getID(), node.getID()).size();
                    if(tempDis < dis){
                        res.set(0,tmpSource);
                        res.set(1,node);
                        dis = tempDis;
                    }
                    node.setExplored(false);
                    leaderAgent.deleteExploredNode(node);
                }
            }
        }
        return res;
    }

    private ArrayList<BasicNode> getMinimumResidual(HashMap<BasicNode, ArrayList<BasicNode> > frontiers){
        int res = Integer.MAX_VALUE;
        ArrayList<BasicNode> targetNode = new ArrayList<>();
        BasicNode source = null;
        BasicNode target = null;
        targetNode.add(source);
        targetNode.add(target);
        for(Map.Entry entry1 : frontiers.entrySet() ){
            BasicNode tmpSource = (BasicNode) entry1.getKey();
            ArrayList<BasicNode> sourceResidual = (ArrayList<BasicNode>) entry1.getValue();

            for(BasicNode node : sourceResidual){
                int tmpSize = node.getRasidualNodes().size();
                if( tmpSize < res){
                    res = tmpSize;
                    targetNode.set(0,tmpSource);
                    targetNode.set(1,node);
                }
            }
        }
        return targetNode;
    }

}
