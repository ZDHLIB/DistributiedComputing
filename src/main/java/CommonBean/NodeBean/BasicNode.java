package CommonBean.NodeBean;

import BlackVirusFinding.GreedyBasedFacade;
import CommonBean.Agents.*;
import jbotsim.Message;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BasicNode extends Node {

    private final static Logger logger = LoggerFactory.getLogger(BasicNode.class);

    private boolean explored = false;
    private BlackVirusAgent blackVirusAgent = null;
    private LeaderAgent leaderAgent = null;
    private ShadowAgent shadowAgent = null;
    private HashMap<Integer,Node > rasideauDegree = new HashMap<Integer, Node>();

    private GreedyBasedFacade greedyBasedFacade = null;

    @Override
    public void onStart() {
        // initialize the node variables
        if(greedyBasedFacade == null) {
            greedyBasedFacade = new GreedyBasedFacade();
        }
        setSize(15);
        System.out.println("Start "+getID());
    }

    @Override
    public void onClock() {

    }


    @Override
    public void onMessage(Message message) {
        // processing of a received message
        Object obj = message.getContent();
        //String: visited message
        if( obj instanceof Integer ){
            greedyBasedFacade.updateResidualDegree(this, obj);

        }
        // Receive explorerAgent, check whether I have been visited or not
        else if( obj instanceof ExporerAgent ){
            // The explorer I received is what I sent
            if( explored ){
                //Update explored map info
                greedyBasedFacade.updateExploredInfo(obj);
                //Start next interation
                greedyBasedFacade.start();
            }
            // I haven't been explored, I am a blackVirus, send clones to neighbours
            else if (blackVirusAgent != null && blackVirusAgent.getActivate()){
                List<Node> neighbours = getNeighbors();
                blackVirusAgent.setTargetNeigs(neighbours);
                blackVirusAgent.setActivate(false);
                greedyBasedFacade.sendClones2Neigbours(this, neighbours);
            }
            // I am a unexplored node and not a black virus, return explorer with my id and neighbours.
            else {
                greedyBasedFacade.returnExplorerAgent(this, obj);
            }

        } else if( obj instanceof BlackVirusAgent){

        } else if( obj instanceof ShadowAgent ){

        }
    }

    @Override
    public void onSelection() {
        //I am a initiator, start the algorithm
        if(getID() == 0){
            //TODO
            greedyBasedFacade.start();
        }
    }


    public HashMap<Integer, Node> getRasideauDegree() {
        return rasideauDegree;
    }

    public void addRasideauDegree(Node node) {
        if(!this.rasideauDegree.containsKey(node.getID())) {
            this.rasideauDegree.put(node.getID(), node);
        }
    }

    public BlackVirusAgent getBlackVirusAgent() {
        return blackVirusAgent;
    }

    public void setBlackVirusAgent(BlackVirusAgent blackVirusAgent) {
        this.blackVirusAgent = blackVirusAgent;
    }

    public Agent getLeaderAgent() {
        return leaderAgent;
    }

    public void setLeaderAgent(LeaderAgent leaderAgent) {
        this.leaderAgent = leaderAgent;
    }

    public Agent getShadowAgent() {
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
