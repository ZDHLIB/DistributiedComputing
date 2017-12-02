package CommonBean.NodeBean;

import BlackVirusFinding.GreedyBasedFacade;
import CommonBean.Agents.*;
import jbotsim.Message;
import jbotsim.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.provider.SHA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BasicNode extends Node {

    private final static Logger logger = LoggerFactory.getLogger(BasicNode.class);

    private boolean explored = false;
    private BlackVirusAgent blackVirusAgent = BlackVirusAgent.getInstance();
    private LeaderAgent leaderAgent = LeaderAgent.getInstance();
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
        //Destination is not me, forward it
        if( message.getDestination() != this ){
            send(message.getDestination(), message);
            return;
        }
        // processing of a received message
        Object obj = message.getContent();
        //String: visited message
        if( obj instanceof Integer ){
            logger.info("{} Receive visited message from {}", getID(), obj);
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

        }
        // Receive black virus's clone
        else if( obj instanceof BlackVirusAgent){
            BlackVirusAgent cloneVirusAgent = (BlackVirusAgent) obj;
            if( cloneVirusAgent.getType().equals(AgentTypeEnum.CLONESVIRUS) ){
                // I have been protected by shadowAgent, so deactivate receiving clones
                if( this.shadowAgent != null ){
                    cloneVirusAgent.setActivate(false);
                }
                // I am a unexplored node, so destroyed by clones
                else {
                    this.blackVirusAgent = cloneVirusAgent;
                }
            }
        } else if( obj instanceof ShadowAgent ){
            // if I have been destroyed by clones and clone is active, then deactivate clones
            if( this.blackVirusAgent != null
                    && this.blackVirusAgent.getActivate()
                    && this.blackVirusAgent.getType().equals(AgentTypeEnum.CLONESVIRUS)){
                this.blackVirusAgent.setActivate(false);
            } else {
                if( this.leaderAgent != null ) {
                    // leaderAgent will go to next stop

                }
                this.shadowAgent = (ShadowAgent) obj;
            }
        } else if( obj instanceof LeaderAgent ){
            LeaderAgent leaderAgent = (LeaderAgent) obj;
            if( this.shadowAgent != null ){
                // leaderAgent will go to next stop and assign a shadow agent to next stop
                leaderAgent.addStepAssign();
                send( leaderAgent.getShadowAgents().get(leaderAgent.getStepAssign()).getTarget(),
                        leaderAgent.getShadowAgents().get(leaderAgent.getStepAssign()) );
                send( leaderAgent.getShadowAgents().get(leaderAgent.getStepAssign()).getTarget(),
                        leaderAgent);
            }
            //protection finished, send explorer to target
            else if( leaderAgent.getShadowAgents().size() == leaderAgent.getStepAssign() ){
                this.leaderAgent = leaderAgent;
                leaderAgent.resetStepAssign();
                ExporerAgent exporerAgent = ExporerAgent.getInstance();
                exporerAgent.setSource(this);
                exporerAgent.setTarget(this.leaderAgent.getTarget());
                send(this.leaderAgent.getTarget(), ExporerAgent.getInstance());
            }
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

    public LeaderAgent getLeaderAgent() {
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
