package CommonBean;

import jbotsim.Node;

import java.awt.*;
import java.awt.geom.Point2D;

public class NodeBuilder {

    private Node node;

    public NodeBuilder(Class<? extends Node> var1){
        try {
            node = var1.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
