package BlackVirusFinding;

import CommonBean.Agents.LeaderAgent;
import CommonBean.NodeBean.BasicNode;
import CommonBean.NodeBean.NodeBuilder;
import CommonBean.StatisticInfo;
import jbotsim.Node;
import jbotsim.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Initiator {

    private final static Logger logger = LoggerFactory.getLogger(Initiator.class);

    private static LeaderAgent leaderAgent = LeaderAgent.getInstance();
    private static StatisticInfo statisticInfo = StatisticInfo.getInstance();

    /**
     * Create a topology with n nodes
     * @param n  # of nodes
     * @param width   # width of topology
     * @param height    # height of topology
     * @param start   # whether startExplorer the clock at the beginning
     * @param var1    # node model of topology
     * @return
     */
    public static Topology initTopology(int n, int width, int height, boolean start, Class<? extends Node> var1){
        logger.info("Start initial topology...");

        BasicNode node = null;
        Random random = new Random();
        leaderAgent.clear();
        Topology tp = new Topology(width,height,start);
        tp.setDefaultNodeModel(var1);

        // randomly set black virus node, do not set it to node 0
        int blackVirusNode = random.nextInt(n-2) + 1;
        for(int i = 0; i < n; i++){
            logger.info("Create node with id {}...", i);

            NodeBuilder nodeBuilder = new NodeBuilder(BasicNode.class);
            nodeBuilder.buildId(i)
                       .buildWireless(true)
                       .buildCommunicationRange(width<height?calRange(width):calRange(height));

            //Initial leader, explorer, shadow agents at node 0
            //Initial node with black virus
            if(i == 0){
                node = buildLeader(nodeBuilder);
            } else if(i == blackVirusNode) {
                node = buildBlackVirus(nodeBuilder);
            } else {
                node = nodeBuilder.build();
            }
            //Random the position
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            tp.addNode(x,y,node);
        }

        //Check connected
        if(!checkConn(tp)){
            tp.clear();
            return null;
        }

        initResidualDegree(tp);

        statisticInfo.setNoEdges(tp.getLinks().size());

        logger.info("Initial topology finished. Sink: 0, blackVirus: {}", blackVirusNode);
        return tp;
    }

    private static BasicNode buildLeader(NodeBuilder nodeBuilder){
        nodeBuilder.buildLeaderAgent();
        BasicNode node = nodeBuilder.build();
        node.setExplored(true);
        leaderAgent.setCurrentNode(node);
        return node;
    }


    private static BasicNode buildBlackVirus(NodeBuilder nodeBuilder){
        nodeBuilder.buildBlackVirusAgent();
        return nodeBuilder.build();
    }


    public static void initResidualDegree(Topology tp){
        List<Node> nodeList = tp.getNodes();

        for(Node node : nodeList){
            BasicNode basicNode = (BasicNode) node;
            basicNode.getRasidualNodes().clear();
            for( Node n2 : basicNode.getNeighbors()) {
                basicNode.addRasideauDegree(n2);
            }
            if(basicNode.getID() == 0){
                initSink(basicNode, tp);
            }
        }
    }


    private static void initSink(BasicNode sinkNode, Topology tp){
        LeaderAgent.getInstance().setTp(tp);
        LeaderAgent.getInstance().addNewNode(sinkNode);
        ArrayList<BasicNode> neigs = new ArrayList<BasicNode>();
        for(Node n : sinkNode.getNeighbors()){
            neigs.add((BasicNode) n);
        }
        LeaderAgent.getInstance().updateHop2Info(sinkNode,neigs);
    }

    /**
     * Calculate communication range
     * @param baseRange
     * @return
     */
    private static double calRange(double baseRange){
        return 0.5*baseRange / Math.sqrt(2);
    }

    /**
     * Check whether all nodes are connected or not
     * @param tp
     * @return
     */
    private static boolean checkConn(Topology tp){
        List<Node> nodes = tp.getNodes();
        Queue<Node> queue = new LinkedList<>();
        int[] flag = new int[nodes.size()];
        int size = 1;

        queue.offer(nodes.get(0));
        flag[nodes.get(0).getID()] = 1;
        while(!queue.isEmpty()){
            Node node = queue.poll();
            List<Node> neigbs = node.getNeighbors();
            for(Node n : neigbs){
                if( flag[n.getID()] != 1 ) {
                    queue.offer(n);
                    size++;
                    flag[n.getID()] = 1;
                }
            }
        }

        if( size == nodes.size()) {
            return true;
        }
        return false;
    }


}
