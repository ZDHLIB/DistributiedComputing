package CommonBean.Agents;

import CommonBean.NodeBean.BasicNode;
import CommonBean.StatisticInfo;
import jbotsim.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LeaderAgent extends Agent {

    private final static Logger logger = LoggerFactory.getLogger(LeaderAgent.class);

    private static LeaderAgent leaderAgent = null;
    private BasicNode target = null;
    private BasicNode currentNode = null;
    private Topology tp = null;
    private static Integer threshold = 0;
    private HashMap<BasicNode, ArrayList<BasicNode> > hop2Info = new HashMap<BasicNode, ArrayList<BasicNode> >();
    private ArrayList<ShadowAgent>  shadowPosition = new ArrayList<ShadowAgent>();
    private Queue<BasicNode> protectedNodes = new LinkedList<BasicNode>();
    private ArrayList<BasicNode> exploredMap = new ArrayList<BasicNode>();
    private StatisticInfo statisticInfo = StatisticInfo.getInstance();


    private LeaderAgent(AgentTypeEnum type){
        this.type = type;
    }

    public static LeaderAgent getInstance(){
        if(leaderAgent == null){
            synchronized (LeaderAgent.class){
                if(leaderAgent == null){
                    leaderAgent = new LeaderAgent(AgentTypeEnum.LEADER);
                    return leaderAgent;
                }
            }
        }
        return leaderAgent;
    }

    /**
     * leaderAgetn moves to neighbour of target
     * @param source
     */
    public void move2Source(BasicNode source){
        if(source == currentNode){
            return;
        }
        source.setLeaderAgent(leaderAgent);
        currentNode.setLeaderAgent(null);
        currentNode = source;
    }

    public void addNewNode(BasicNode node){
        if(!exploredMap.contains(node))
            exploredMap.add(node);
    }

    /**
     * update frontiers
     * @param node
     * @param neighbours
     */
    public void updateHop2Info( BasicNode node, ArrayList<BasicNode> neighbours ){
        if( neighbours.size() > 0 ){
            hop2Info.put(node, neighbours);
        }
        Iterator<BasicNode> iter = hop2Info.keySet().iterator();
        BasicNode n = null;
        while(iter.hasNext()){
            n = iter.next();
            if( n.getRasidualNodes().size() <= 0 ){
                iter.remove();
            }
        }
    }

    /**
     * update the nodes should be protected
     * @param node
     */
    public void addProtectNode(BasicNode node){
        protectedNodes.offer(node);
        if( protectedNodes.size() > shadowPosition.size() ){
            int diff = protectedNodes.size() - shadowPosition.size();
            for(int i = 0; i < diff; i++){
                ShadowAgent shadowAgent = new ShadowAgent(AgentTypeEnum.CLEANER);
                shadowPosition.add(shadowAgent);
            }
        }
        if( statisticInfo.getNoShadows() < shadowPosition.size() ) {
            statisticInfo.setNoShadows(shadowPosition.size());
        }
    }

    public BasicNode getNextProtectNode(){
        if( !protectedNodes.isEmpty() ) {
            return protectedNodes.poll();
        }
        return null;
    }

    public void clear(){
        target = null;
        currentNode = null;
        protectedNodes.clear();
        shadowPosition.clear();
        exploredMap.clear();
    }

    public BasicNode getTarget() {
        return target;
    }

    public void setTarget(BasicNode target) {
        this.target = target;
    }

    public BasicNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(BasicNode currentNode) {
        this.currentNode = currentNode;
    }

    public HashMap<BasicNode, ArrayList<BasicNode>> getHop2Info() {
        return hop2Info;
    }

    public void setHop2Info(HashMap<BasicNode, ArrayList<BasicNode>> hop2Info) {
        this.hop2Info = hop2Info;
    }

    public ArrayList<ShadowAgent> getShadowPosition() {
        return shadowPosition;
    }

    public void setShadowPosition(ArrayList<ShadowAgent> shadowPosition) {
        this.shadowPosition = shadowPosition;
    }

    public Queue<BasicNode> getProtectedNodes() {
        return protectedNodes;
    }

    public void setProtectedNodes(Queue<BasicNode> protectedNodes) {
        this.protectedNodes = protectedNodes;
    }

    public ArrayList<BasicNode> getExploredMap() {
        return exploredMap;
    }

    public void setExploredMap(ArrayList<BasicNode> exploredMap) {
        this.exploredMap = exploredMap;
    }

    public Topology getTp() {
        return tp;
    }

    public void setTp(Topology tp) {
        this.tp = tp;
    }

    public static Integer getThreshold() {
        return threshold;
    }

    public static void setThreshold(Integer threshold) {
        LeaderAgent.threshold = threshold;
    }
}
