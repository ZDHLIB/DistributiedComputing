package BlackVirusFinding;

import CommonBean.BasicNode;
import CommonBean.NodeBuilder;
import jbotsim.Node;
import jbotsim.Topology;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Initiator {

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
        Random random = new Random();
        Topology tp = new Topology(width,height,start);
        tp.setDefaultNodeModel(var1);
        for(int i = 0; i < n; i++){
            Node node = new NodeBuilder(BasicNode.class)
                            .buildId(i)
                            .buildWireless(true)
                            .buildCommunicationRange(width<height?0.5*width:0.5*height)
                            .build();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            tp.addNode(x,y,node);
        }
        return tp;
    }

}
