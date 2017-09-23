package CommonBean;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

public class LonerMessageBased extends Node {

    boolean mayBeAlone = true;
    @Override
    public void onClock(){
        setColor(mayBeAlone ? Color.green : Color.red);
        sendAll(new Message());
        mayBeAlone = true;
    }
    @Override
    public void onMessage(Message message) {
        // processing of a received message
        mayBeAlone = false;
    }

}
