package CommonBean;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.TopologyListener;

import java.awt.*;

public class LonerCentralized implements TopologyListener, ConnectivityListener {

    public LonerCentralized(Topology tp){
        tp.addTopologyListener(this);
        tp.addConnectivityListener(this);
    }


    public void onNodeAdded(Node node) {
        node.setColor(node.hasNeighbors()? Color.red:Color.green);
    }


    public void onNodeRemoved(Node node) {
    }


    public void onLinkAdded(Link link) {
        for (Node node : link.endpoints())
            if(node.getColor() != Color.red){
                node.setColor(Color.red);
            }
    }

    public void onLinkRemoved(Link link) {
        for (Node node : link.endpoints())
            if (!node.hasNeighbors()) {
                node.setColor(Color.green);
            }
    }
}
