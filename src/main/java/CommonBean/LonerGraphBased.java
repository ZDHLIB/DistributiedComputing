package CommonBean;

import jbotsim.Link;
import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

public class LonerGraphBased extends Node {

    @Override
    public void onStart(){
        setColor(Color.green);
    }

    @Override
    public void onLinkAdded(Link link){
        setColor(Color.red);
    }

    @Override
    public void onLinkRemoved(Link link){
        if(!hasNeighbors()){
            setColor(Color.green);
        }
    }
}
