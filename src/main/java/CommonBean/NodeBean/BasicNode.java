package CommonBean.NodeBean;

import CommonBean.Agents.Agent;
import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

public class BasicNode extends Node {

    private Agent blackVirusAgent;
    private Agent cloneVirusAgent;
    private Agent explorerAgent;
    private Agent leaderAgent;
    private Agent shadowAgent;

    @Override
    public void onStart() {
        // initialize the node variables
        setSize(15);
    }

    @Override
    public void onClock() {
        // Start the algorithm

    }

    @Override
    public void onMessage(Message message) {
        // processing of a received message
    }

    @Override
    public void onSelection() {
        // what to do when this node is selected by the user
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
