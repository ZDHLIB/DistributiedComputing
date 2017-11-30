package BlackVirusFinding;

import CommonBean.NodeBean.BasicNode;
import CommonBean.NodeBean.NodeBuilder;
import jbotsim.Node;
import jbotsim.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Random;

public class Initiator {

    private final static Logger logger = LoggerFactory.getLogger(Initiator.class);

    /**
     * Create a topology with n nodes
     * @param n  # of nodes
     * @param width   # width of topology
     * @param height    # height of topology
     * @param start   # whether start the clock at the beginning
     * @param var1    # node model of topology
     * @return
     */
    public static Topology initTopology(int n, int width, int height, boolean start, Class<? extends Node> var1){
        logger.info("Start initial topology...");

        Random random = new Random();
        Topology tp = new Topology(width,height,start);
        tp.setDefaultNodeModel(var1);

        // randomly set black virus node, do not set it to node 0
        int blackVirusNode = random.nextInt(n-2) + 1;

        for(int i = 0; i < n; i++){
            logger.info("Create node with id {}...", i);

            NodeBuilder nodeBuilder = new NodeBuilder(BasicNode.class);
            nodeBuilder.buildId(i)
                       .buildWireless(true)
                       .buildCommunicationRange(width<height?0.5*width:0.5*height)
                       .buildResideauDegree();

            //Initial leader, explorer, shadow agents at node 0
            //Initial node with black virus
            if(i == 0){
                nodeBuilder.buildColor(new Color(123, 27, 7));
                nodeBuilder.buildBlackVirusAgent()
                           .buildLeaderAgent();

            } else if(i == blackVirusNode) {
                nodeBuilder.buildColor(new Color(0, 0, 0));
                nodeBuilder.buildBlackVirusAgent();
            }
            Node node = nodeBuilder.build();

            int x = random.nextInt(width);
            int y = random.nextInt(height);
            tp.addNode(x,y,node);
        }


        logger.info("Initial topology finished...");
        return tp;
    }

}
