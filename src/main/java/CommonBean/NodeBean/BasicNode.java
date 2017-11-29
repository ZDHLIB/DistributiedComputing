package CommonBean.NodeBean;

import CommonBean.Agents.Agent;
import jbotsim.Message;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;


public class BasicNode extends Node {

    private final static Logger logger = LoggerFactory.getLogger(BasicNode.class);

    private Agent blackVirusAgent;
    private Agent cloneVirusAgent;
    private Agent explorerAgent;
    private Agent leaderAgent;
    private Agent shadowAgent;
    private HashMap<Integer,Node > rasideauDegree = new HashMap<Integer, Node>();

    @Override
    public void onStart() {
        // initialize the node variables
        setSize(15);
        System.out.println("Start "+getID());
    }

    @Override
    public void onClock() {

    }


    @Override
    public void onMessage(Message message) {
        // processing of a received message
    }

    @Override
    public void onSelection() {
        // what to do when this node is selected by the user

    }


    public HashMap<Integer, Node> getRasideauDegree() {
        return rasideauDegree;
    }

    public void addRasideauDegree(Node node) {
        if(!this.rasideauDegree.containsKey(node.getID())) {
            this.rasideauDegree.put(node.getID(), node);
        }
    }

    public Agent getBlackVirusAgent() {
        return blackVirusAgent;
    }

    public void setBlackVirusAgent(Agent blackVirusAgent) {
        this.blackVirusAgent = blackVirusAgent;
    }

    public Agent getCloneVirusAgent() {
        return cloneVirusAgent;
    }

    public void setCloneVirusAgent(Agent cloneVirusAgent) {
        this.cloneVirusAgent = cloneVirusAgent;
    }

    public Agent getExplorerAgent() {
        return explorerAgent;
    }

    public void setExplorerAgent(Agent explorerAgent) {
        this.explorerAgent = explorerAgent;
    }

    public Agent getLeaderAgent() {
        return leaderAgent;
    }

    public void setLeaderAgent(Agent leaderAgent) {
        this.leaderAgent = leaderAgent;
    }

    public Agent getShadowAgent() {
        return shadowAgent;
    }

    public void setShadowAgent(Agent shadowAgent) {
        this.shadowAgent = shadowAgent;
    }
}
