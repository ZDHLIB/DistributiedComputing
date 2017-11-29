package CommonBean;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

public class BasicNode extends Node {



    @Override
    public void onStart() {
        // initialize the node variables
        setSize(15);
        System.out.println("BasicNode.....");
    }

    @Override
    public void onClock() {
        // code to be executed by this node in each round
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
