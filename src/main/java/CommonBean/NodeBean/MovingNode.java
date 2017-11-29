package CommonBean.NodeBean;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

public class MovingNode extends Node {

    @Override
    public void onStart() {
        // initialize the node variables
        this.setID(0000);
        this.setColor(new Color(14, 35, 234));
        this.setDirection(0.5*Math.PI);
    }

    @Override
    public void onClock() {
        // code to be executed by this node in each round
        move();
        wrapLocation();
    }

    @Override
    public void onMessage(Message message) {
        // processing of a received message
    }

    @Override
    public void onSelection() {
        // what to do when this node is selected by the user
    }
}
