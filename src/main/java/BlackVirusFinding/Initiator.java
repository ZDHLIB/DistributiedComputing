package BlackVirusFinding;

import CommonBean.Agents.LeaderAgent;
import CommonBean.NodeBean.BasicNode;
import CommonBean.NodeBean.NodeBuilder;
import jbotsim.Node;
import jbotsim.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
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

        BasicNode node = null;
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
                       .buildCommunicationRange(width<height?0.5*width:0.5*height);

            //Initial leader, explorer, shadow agents at node 0
            //Initial node with black virus
            if(i == 0){
                nodeBuilder.buildColor(new Color(123, 27, 7));
                nodeBuilder.buildLeaderAgent();
                node = nodeBuilder.build();
            } else if(i == blackVirusNode) {
                nodeBuilder.buildColor(new Color(0, 0, 0));
                nodeBuilder.buildBlackVirusAgent();
                node = nodeBuilder.build();
            } else {
                node = nodeBuilder.build();
            }


            int x = random.nextInt(width);
            int y = random.nextInt(height);
            tp.addNode(x,y,node);
        }

        initResidualDegree(tp);

        logger.info("Initial topology finished...");
        return tp;
    }


    private static void initSink(BasicNode node){

        LeaderAgent.getInstance().addNewNode(node);
        HashMap<Integer, Node> neigs = new HashMap<Integer, Node>();
        for(Node n : node.getNeighbors()){
            neigs.put(n.getID(), n);
        }
        LeaderAgent.getInstance().addHop2Neighbours(node, neigs);
    }

    public static void initResidualDegree(Topology tp){
        List<Node> nodeList = tp.getNodes();

        for(Node node : nodeList){
            BasicNode basicNode = (BasicNode) node;
            basicNode.getRasideauDegree().clear();
            for( Node n2 : basicNode.getNeighbors()) {
                basicNode.addRasideauDegree(n2);
            }
            if(basicNode.getID() == 0){
                initSink(basicNode);
            }
        }
    }



}
