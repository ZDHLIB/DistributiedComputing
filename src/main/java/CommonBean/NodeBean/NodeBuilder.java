package CommonBean.NodeBean;

import CommonBean.Agents.AgentTypeEnum;
import CommonBean.Agents.BlackVirusAgent;
import CommonBean.Agents.LeaderAgent;
import CommonBean.Agents.ShadowAgent;
import jbotsim.Node;

import java.awt.*;

public class NodeBuilder {

    private BasicNode node;

    public NodeBuilder(Class<? extends Node> var1){
        try {
            node = (BasicNode) var1.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public NodeBuilder buildLeaderAgent(){
        node.setLeaderAgent(new LeaderAgent(AgentTypeEnum.LEADER));
        return this;
    }

    public NodeBuilder buildExplorerAgent(){
        node.setExplorerAgent(new LeaderAgent(AgentTypeEnum.EXPLORER));
        return this;
    }

    public NodeBuilder buildShadowAgent(){
        node.setShadowAgent(new ShadowAgent(AgentTypeEnum.LEADER));
        return this;
    }

    public NodeBuilder buildBlackVirusAgent(){
        node.setBlackVirusAgent(new ShadowAgent(AgentTypeEnum.BLACKVIRUS));
        return this;
    }

    public NodeBuilder buildResideauDegree(){
        for(Node n : node.getNeighbors()){
            node.addRasideauDegree(n);
        }
        return this;
    }


    public NodeBuilder buildId(Integer id){
        node.setID(id);
        return this;
    }

    public NodeBuilder buildColor(Color color){
        node.setColor(color);
        return this;
    }

    public NodeBuilder buildCommunicationRange(double range){
        node.setCommunicationRange(range);
        return this;
    }


    public NodeBuilder buildWireless(Boolean b){
        if(b){
            node.enableWireless();
        }else{
            node.disableWireless();
        }
        return this;
    }

    public Node build(){
        return node;
    }

}
