package CommonBean.NodeBean;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

public class HighwayCar extends Node {

    double speed = 1;

    public HighwayCar(){
        setDirection(0); // Eastward
        setIcon("/imgs/car.png");
        setSize(50);
    }
    public void onClock() {
        move(speed);
        wrapLocation();
    }
}
