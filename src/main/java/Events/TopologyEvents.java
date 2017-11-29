package Events;

import BlackVirusFinding.Initiator;
import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.SelectionListener;
import jbotsim.event.TopologyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopologyEvents implements TopologyListener, ConnectivityListener, SelectionListener {

    private final static Logger logger = LoggerFactory.getLogger(TopologyEvents.class);

    public TopologyEvents(Topology tp){
        tp.addTopologyListener(this);
        tp.addConnectivityListener(this);
        tp.addSelectionListener(this);
    }

    public void onLinkAdded(Link link) {

        logger.info("Add link {} >>> {}", link.source, link.destination);
    }

    public void onLinkRemoved(Link link) {

        logger.info("Remove link {} >>> {}", link.source, link.destination);
    }

    public void onNodeAdded(Node node) {

        logger.info("Add Node {}", node.getID());
    }

    public void onNodeRemoved(Node node) {

        logger.info("Remove Node {}", node.getID());
    }

    public void onSelection(Node node) {
        logger.info("Select node : {}",node.getID());
        //sink node is 0, if it's clicked, start the algorithm
        if( node.getID() == 0){

        }
    }
}
