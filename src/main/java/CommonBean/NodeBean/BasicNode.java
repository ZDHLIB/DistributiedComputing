package CommonBean.NodeBean;

import BlackVirusFinding.AbstractAgentOperatioin;
import BlackVirusFinding.AlgorithmFacade;
import CommonBean.Agents.*;
import CommonBean.MyMessage;
import CommonBean.StatisticInfo;
import jbotsim.Message;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class BasicNode extends Node {

    private final static Logger logger = LoggerFactory.getLogger(BasicNode.class);

    private boolean explored = false;
    private BlackVirusAgent blackVirusAgent = null;
    private LeaderAgent leaderAgent = null;
    private ShadowAgent shadowAgent = null;
    private ArrayList<BasicNode> rasidualNodes = new ArrayList<BasicNode>();
    private AlgorithmFacade algorithmFacade = null;
    private StatisticInfo statisticInfo = StatisticInfo.getInstance();

    @Override
    public void onStart() {
        // initialize the node variables
        if(algorithmFacade == null) {
            algorithmFacade = new AlgorithmFacade();
        }
        setSize(15);
        setIcon("/imgs/"+getID()+".png");
    }

    @Override
    public void onClock() {

    }


    @Override
    public void onMessage(Message message) {
        // processing of a received message
        //Collect the NO. of Movements
        statisticInfo.addNO_MOVES();
        MyMessage mm = (MyMessage) message.getContent();
        //Destination is not me, forward it
        if( mm.getPath() != null && !mm.getPath().isEmpty() ){
            BasicNode nextNode = mm.getPath().poll();
            logger.info("{} receives message from {}, and forwards to {}", getID(), message.getSender(), nextNode.getID());
            send(nextNode, mm);
            return;
        }
        Object obj = mm.getObj();
        //String: visited message
        if( obj instanceof Integer ){
            Integer data = (Integer) obj;
            logger.info("{} receives visited from {}", getID(), data);
            algorithmFacade.dealWithInteger(this, data);
        }
        // Receive explorerAgent, check whether I have been visited or not
        else if( obj instanceof ExporerAgent ){
            logger.info("{} receives ExporerAgent from {}", getID(), message.getSender());
            ExporerAgent exporerAgent = (ExporerAgent) obj;
            algorithmFacade.dealWithExplorerAgent(this, exporerAgent);
        }
        // Receive black virus's clone
        else if( obj instanceof BlackVirusAgent){
            logger.info("{} receives BlackVirusAgent from {}", getID(), message.getSender());
            BlackVirusAgent blackVirusAgent = (BlackVirusAgent) obj;
            algorithmFacade.dealWithBlackClonesAgent(this, blackVirusAgent);
        }
        // Receive shadowAgent
        else if( obj instanceof ShadowAgent ){
            logger.info("{} receives ShadowAgent from {}", getID(), message.getSender());
            ShadowAgent shadowAgent = (ShadowAgent) obj;
            algorithmFacade.dealWithShadowAgent(this, shadowAgent);
        }
        // Receive leaderAgent
        else if( obj instanceof LeaderAgent ){
            //Collect NO. movements of LeaderAgent
            statisticInfo.addNO_MOVES_LEADER();
            logger.info("{} receives LeaderAgent from {}", getID(), message.getSender());
            setLeaderAgent(LeaderAgent.getInstance());
            algorithmFacade.dealWithLeaderAgent(this);
        }
    }

    @Override
    public void onSelection() {
        //I am a initiator, startExplorer the algorithm
        if(getID() == 0){
            //TODO
            algorithmFacade.startExplorer();
            AbstractAgentOperatioin.sendVisited2Neighbours(this, this.getNeighbors());
        }
    }


    public ArrayList<BasicNode> getRasidualNodes() {
        return rasidualNodes;
    }

    public void addRasideauDegree(Node node) {
        this.rasidualNodes.add((BasicNode) node);
    }

    public BlackVirusAgent getBlackVirusAgent() {
        return blackVirusAgent;
    }

    public void setBlackVirusAgent(BlackVirusAgent blackVirusAgent) {
        this.blackVirusAgent = blackVirusAgent;
    }

    public LeaderAgent getLeaderAgent() {
        return this.leaderAgent;
    }

    public void setLeaderAgent(LeaderAgent leaderAgent) {
        this.leaderAgent = leaderAgent;
    }

    public ShadowAgent getShadowAgent() {
        return shadowAgent;
    }

    public void setShadowAgent(ShadowAgent shadowAgent) {
        this.shadowAgent = shadowAgent;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }
}
